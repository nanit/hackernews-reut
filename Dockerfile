FROM postgres:9.5.10
ADD create_nanit.sql /docker-entrypoint-initdb.d/
