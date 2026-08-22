import json

from pydantic import model_validator
from pydantic_settings import BaseSettings, SettingsConfigDict

from app.aws import secretsmanager


class Configuration(BaseSettings):
    bucket_name: str | None = None
    db_url: str | None = None
    db_name: str | None = None
    db_user: str | None = None
    db_host: str | None = None
    db_port: str | None = None
    db_secret: str | None = None

    model_config = SettingsConfigDict(env_prefix="APP_", env_file=".env", env_file_encoding="utf-8", extra="ignore")

    @model_validator(mode="after")
    def construct_db_url(self) -> Configuration:
        if self.db_url is None and self.db_secret is not None:
            secret = json.loads(secretsmanager.get_secret_value(SecretId=self.db_secret)["SecretString"])

            self.db_url = (
                f"dbname={self.db_name} "
                f"user={self.db_user} "
                f"password={secret['password']} "
                f"host={self.db_host} "
                f"port={self.db_port}"
            )

        return self


conf = Configuration()
