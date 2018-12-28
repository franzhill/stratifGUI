:: Stops the shell echoing every command
@ECHO OFF


:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Dump de la table qui nous intéresse, extrait du dump sql foncier
set SQL_FILE_DUMP_EXTRACT="${fd.sqlFile1}"

:: Commandes SQL permettant d'extraire juste les champs voulus du dump de la table ci-dessus + création d'index
set SQL_FILE_REDUCE_TABLE="${fd.sqlFile2}"


:: Note concernant l'exécution du 2è fichier :
::  parce que l'on exécute une instruction VACUUM dans ce 2è jeu de commandes,
::  et que celle-ci ne peut être exécutée que séparement (sous peine de "VACUUM cannot be executed from a function or multi-command string")
::  il faut charger le 2è jeu de commande par un < et non un -f
::
:: -v ON_ERROR_STOP=1  : s'arrête dès la première erreur
"${model.postgresqlBinPath}"\psql -v ON_ERROR_STOP=1 -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %SQL_FILE_DUMP_EXTRACT%
"${model.postgresqlBinPath}"\psql -v ON_ERROR_STOP=1 -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} < %SQL_FILE_REDUCE_TABLE%
