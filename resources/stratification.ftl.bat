:: Stops the shell echoing every command
@ECHO OFF


:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Répertoire contenant tous les fichiers sql à jouer (pour le département en cours)
set SQL_FILE_DIR="${model.workFolder}"\${dep}

:: Executer tous les scripts sql contenus dans le répertoire


:: Execute sql commands
echo "Exécution des fichiers sql..."

cd %SQL_FILE_DIR%
:: Apparamment, la commande suivante les traite dans l'ordre alphabétique, tant mieux c'est ce que l'on veut ;o)
::for %%f in (*.sql) do echo executing %%f ...
for %%f in (*.sql) do (echo "Execution de %%f ..." & "${model.postgresqlBinPath}"\psql -p ${model.modelDb.port} -d ${model.modelDb.name}  -h ${model.modelDb.hostname} -U ${model.modelDb.user} -f %%f)






