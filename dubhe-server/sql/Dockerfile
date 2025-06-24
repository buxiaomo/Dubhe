FROM docker.io/library/mysql:5.7
COPY ./01-Dubhe-DDL.sql /docker-entrypoint-initdb.d/01-Dubhe-DDL.sql
COPY ./02-Dubhe-DML.sql /docker-entrypoint-initdb.d/02-Dubhe-DML.sql
COPY ./09-Dubhe-Patch.sql /docker-entrypoint-initdb.d/09-Dubhe-Patch.sql
COPY ./10-Dubhe-Boot-Update-Cloud.sql /docker-entrypoint-initdb.d/10-Dubhe-Boot-Update-Cloud.sql
COPY ./11-Dubhe-Patch-3.0.sql /docker-entrypoint-initdb.d/11-Dubhe-Patch-3.0.sql