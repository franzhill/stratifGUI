:: Stops the shell echoing every command
@ECHO OFF

set DEP=${fd.departement}
set FIC=${fd.name}

:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

set CSV_FILE="${fd.absPath}"

:: Contient des références à %DEP% et %FIC% :
set TABLE=${model.couche.schema}.${model.couche.table}

:: Commande SQL permettant de créer la table CVI
set SQL_FILE_CREATE_TABLE="${fd.sqlFile2}"

:: Cf https://stackoverflow.com/questions/28602647/postgresql-csv-import-from-command-line
:: -v ON_ERROR_STOP=1  : s'arrête dès la première erreur
"${model.postgresqlBinPath}"\psql -v ON_ERROR_STOP=1 -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} < %SQL_FILE_CREATE_TABLE%
"${model.postgresqlBinPath}"\psql -v ON_ERROR_STOP=1 -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -c "\copy %TABLE% FROM '%CSV_FILE%' delimiter ',' csv HEADER"


