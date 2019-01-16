:: Stops the shell echoing every command
@ECHO OFF


set DEP=${fd.departement}
set FIC=${fd.name}

:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}


set SQL_FILE="${model.tempFolderPath}"\${model.sqlFile}


:: Execute sql commands
echo "Executing SQL commands..."
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %SQL_FILE%






