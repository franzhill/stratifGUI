:: Stops the shell echoing every command
@ECHO OFF



:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Chemin absolu de la sauvegarde
set BCKP_FOLDER="${model.bckpFolder}"


:: L'utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc)
:: (recommandé par rapport au format par défaut texte SQL) nécéssite que la
:: bibliothèque de compression zlib soit disponible sur le système.

:: Fonctionnalité de sauvegarde en parallèle de pg_dump : "pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Vous pouvez contrôler le degré de parallélisme avec le paramètre -j.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire." (=> donc pas le format custom c)
echo "Restauration de : " %BCKP_FOLDER% "..."
"${model.postgresqlBinPath}"\pg_restore -v -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user} -d ${model.modelDb.name} -j ${model.nbThreads} %BCKP_FOLDER%

echo "Restauration terminée."

