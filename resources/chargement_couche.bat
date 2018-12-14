
:: Variabilised within {model.couche.schema} and {model.couche.table}
set %DEP%=${fd.departement}
set %FIC%=${fd.name}

:: Write SQL command to create index for file
echo CREATE INDEX n_${fd.name}_geom_idx ON ${model.couche.schema}.${model.couche.table} USING GIST (geom); >> "${model.tempFolderPath}"/index_${fd.name}.sql

:: Write SQL command to load file in DB
"${model.postgresqlBinPath}"/${model.couche.binCmd} ${fd.absPath} ${model.couche.schema}.${model.couche.table} >> "${model.tempFolderPath}"/${fd.name}.sql



:: Execute commands
cd "${model.tempFolderPath}"
for %%f in (*.sql) do "${model.postgresqlBinPath}"/psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %%f


