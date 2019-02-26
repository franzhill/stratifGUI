:: Stops the shell echoing every command
@ECHO OFF



:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Chemin absolu de la sauvegarde
set BCKP_FOLDER="${model.bckpFolder}"
set DB_PARAMS=-p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user} -d ${model.modelDb.name}

:: L'utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc)
:: (recommandé par rapport au format par défaut texte SQL) nécéssite que la
:: bibliothèque de compression zlib soit disponible sur le système.

:: Fonctionnalité de sauvegarde en parallèle de pg_dump : "pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Vous pouvez contrôler le degré de parallélisme avec le paramètre -j.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire." (=> donc pas le format custom c)
:: -c : clean (drop) database objects before recreating

:: Nota : voir bakcup_db.ftl.bat pour plus d'information sur la méthodologie de sauvegarde.

echo "Restauration de : " %BCKP_FOLDER% "..."

echo "> Restauration de la structure..."
"${model.postgresqlBinPath}"\psql %DB_PARAMS% -f %BCKP_FOLDER%\structure2

echo "> Restauration des données..."
"${model.postgresqlBinPath}"\pg_restore -v %DB_PARAMS% -j ${model.nbThreads} %BCKP_FOLDER%\data


echo "Restauration terminée."

