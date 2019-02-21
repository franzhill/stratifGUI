:: Stops the shell echoing every command
@ECHO OFF



:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Chemin absolu de la sauvegarde
set BCKP_PATH="${model.parentDir}\${model.name}"

:: Suppression préalable du contenu du répertoire de svg
:: En effet si celui-ci n'est pas vide, pg_dump sort en erreur
echo "Suppression de l'ancienne sauvegarde de même nom si elle existe..."
IF EXIST %BCKP_PATH% RMDIR %BCKP_PATH% /S /Q && MD %BCKP_PATH%



:: L'utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc)
:: (recommandé par rapport au format par défaut texte SQL) nécéssite que la
:: bibliothèque de compression zlib soit disponible sur le système.

:: Fonctionnalité de sauvegarde en parallèle de pg_dump : "pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Vous pouvez contrôler le degré de parallélisme avec le paramètre -j.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire." (=> donc pas le format custom c)
echo "Sauvegarde..."
"${model.postgresqlBinPath}"\pg_dump -v -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user} -j ${model.nbThreads} <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fd  -f %BCKP_PATH% ${model.modelDb.name}

echo "Sauvegarde terminée."





:: --exclude-schema=SCHEMA
:: --exclude-table=TABLE
:: --schema=SCHEMA          dump the named schema(s) only



:: Restaurer avec :
::pg_restore -d ${model.modelDb.name} -j num_jobs repertoire_de_svg

