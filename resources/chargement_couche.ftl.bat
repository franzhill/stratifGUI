:: Stops the shell echoing every command
@ECHO OFF

:: For late extrapolation within the values of {model.couche.schema} and {model.couche.table}
set DEP=${fd.departement}
set FIC=${fd.name}

:: Write SQL command to create index for file
echo "Write SQL command to create index for file..."
::echo CREATE INDEX n_${fd.name}_geom_idx ON ${model.couche.schema}.${model.couche.table} USING GIST (geom); >> "${model.tempFolderPath}REMOVe1"/index_${fd.name}.sql
echo CREATE INDEX n_${fd.name}_geom_idx ON ${model.couche.schema}.${model.couche.table} USING GIST (geom); >> ./index_${fd.name}_${fd.departement}.sql

:: Write SQL command to load file in DB
echo "Write SQL command to load file in DB..."
"${model.postgresqlBinPath}"\${model.couche.binCmd} ${fd.absPath} ${model.couche.schema}.${model.couche.table} >> ./${fd.name}_${fd.departement}.sql



:: Execute commands
:: TODO no attempt to optimise anything whatsoever has been made yet
echo "Executing commands..."
::cd "${model.tempFolderPath}REMOVe2"
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f index_${fd.name}_${fd.departement}.sql
"${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f ${fd.name}_${fd.departement}.sql





