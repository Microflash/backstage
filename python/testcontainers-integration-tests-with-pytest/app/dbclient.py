import atexit
from contextlib import contextmanager
from dataclasses import dataclass

from psycopg import Connection, connect

from app.conf import conf


@dataclass
class ConnectionContext:
    _connection: Connection

    def __init__(self, db_url):
        conn = connect(conninfo=db_url, autocommit=True)
        object.__setattr__(self, "_connection", conn)
        atexit.register(conn.close)

    @contextmanager
    def cursor(self):
        with self._connection.cursor() as cursor:
            yield cursor


connection = ConnectionContext(conf.db_url)
