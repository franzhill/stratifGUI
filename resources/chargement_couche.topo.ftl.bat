:: Stops the shell echoing every command
@ECHO OFF


set DEP=${fd.departement}
set FIC=${fd.name}

:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

set SQL_FILE_INDEX="${model.workFolder}"\index_${model.couche.type}_${fd.name}_${fd.departement}.sql
set SQL_FILE="${model.workFolder}"\${model.couche.type}_${fd.name}_${fd.departement}.sql


echo "Write SQL command to create index for file..."
echo CREATE INDEX n_${fd.name}_geom_idx ON ${model.couche.schema}.${model.couche.table} USING GIST (geom); >> %SQL_FILE_INDEX%


echo "Write SQL command to load file in DB..."
"${model.postgresqlBinPath}"\${model.couche.binCmd} ${fd.absPath} ${model.couche.schema}.${model.couche.table} >> %SQL_FILE%



:: Execute commands
:: TODO no attempt to optimise anything whatsoever has been made yet
echo "Executing commands..."
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %SQL_FILE%
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %SQL_FILE_INDEX%





