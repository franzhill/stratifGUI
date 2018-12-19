:: Stops the shell echoing every command
@ECHO OFF

:: For late extrapolation within the values of {model.couche.schema} and {model.couche.table}
set DEP=${fd.departement}
set FIC=${fd.name}

set SQL_FILE="${model.tempFolderPath}"\${fd.name}_${fd.departement}.sql


:: Write SQL command to load file in DB
echo "Write SQL command to load file in DB..."
"${model.postgresqlBinPath}"\${model.couche.binCmd} ${fd.absPath} ${model.couche.schema}.${model.couche.table} > %SQL_FILE%


:: Execute commands
:: TODO no attempt to optimise anything whatsoever has been made yet
echo "Executing commands..."
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %SQL_FILE%



