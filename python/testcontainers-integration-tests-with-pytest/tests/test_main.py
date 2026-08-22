import json
import os

import boto3
import pytest
from floci import FlociContainer
from testcontainers.postgres import PostgresContainer

from tests.mockutils import mock_module

object_key = "/root/text.txt"
test_content = b"Hello from Testcontainers!"


def floci_client(service_name, floci_container):
    return boto3.client(
        service_name,
        endpoint_url=floci_container.get_endpoint(),
        region_name=floci_container.get_region(),
        aws_access_key_id=floci_container.get_access_key(),
        aws_secret_access_key=floci_container.get_secret_key(),
    )


@pytest.fixture(scope="module", autouse=True)
def setup():
    with (
        FlociContainer(image="floci/floci:1.7.0") as floci,
        PostgresContainer(image="postgres:18-alpine") as postgres,
        mock_module(
            "app.aws",
            s3=floci_client("s3", floci),
            secretsmanager=floci_client("secretsmanager", floci),
        ),
    ):
        os.environ["APP_BUCKET_NAME"] = "test-bucket"
        os.environ["APP_DB_NAME"] = postgres.dbname
        os.environ["APP_DB_USER"] = postgres.username
        os.environ["APP_DB_HOST"] = postgres.get_container_host_ip()
        os.environ["APP_DB_PORT"] = str(postgres.get_exposed_port(5432))
        secret_name = "db/secret"
        os.environ["APP_DB_SECRET"] = secret_name

        from app.aws import s3, secretsmanager

        secretsmanager.create_secret(
            Name=secret_name,
            SecretString=json.dumps({"password": postgres.password}),
        )

        from app.conf import conf

        s3.create_bucket(Bucket=conf.bucket_name)

        from app.dbclient import connection

        with connection.cursor() as cursor:
            cursor.execute(
                """
                create table files (
                    id int generated always as identity primary key, 
                    object_key text not null
                );
                """
            )
        yield


def test_read_text_file():
    from app.dbclient import connection

    with connection.cursor() as cursor:
        from app.aws import s3
        from app.conf import conf

        s3.put_object(Bucket=conf.bucket_name, Key=object_key, Body=test_content)
        file_id = cursor.execute(
            """
            insert into files (object_key) values (%(objectKey)s) returning id
            """,
            {"objectKey": object_key},
        ).fetchone()[0]

        from app.main import read_text_file

        assert read_text_file(file_id) == test_content.decode("utf-8")
