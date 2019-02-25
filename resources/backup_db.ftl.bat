:: Stops the shell echoing every command
@ECHO OFF



:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Chemin absolu de la sauvegarde
set BCKP_FOLDER="${model.parentDir}\${model.name}"

:: -d database volontairement omis ici
set DB_PARAMS=-p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user}

:: Suppression préalable du contenu du répertoire de svg
:: En effet si celui-ci n'est pas vide, pg_dump sort en erreur
echo "Suppression de l'ancienne sauvegarde de même nom si elle existe..."
IF EXIST %BCKP_FOLDER% RMDIR %BCKP_FOLDER% /S /Q && MD %BCKP_FOLDER%

MD %BCKP_FOLDER%\data


:: L'utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc)
:: (recommandé par rapport au format par défaut texte SQL) nécéssite que la
:: bibliothèque de compression zlib soit disponible sur le système.

:: Fonctionnalité de sauvegarde en parallèle de pg_dump : "pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Vous pouvez contrôler le degré de parallélisme avec le paramètre -j.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire." (=> donc pas le format custom c)
::
:: Nota : jouer avec les options -a et -c sur pg_dump et pg_restore ne fonctionnera pas dans notre contexte où nous
:: n'avons pas les droits pour créer des schemas. La restitution de la création des schemas (= sans l'option -a)
:: va de pair avec celles des tables, des sqéuences etc.
:: D'où l'approche en 2 temps suivante :



echo "Sauvegarde..."
::"${model.postgresqlBinPath}"\pg_dump -v -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user} -j ${model.nbThreads} <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fd  -f %BCKP_FOLDER% ${model.modelDb.name}
::"${model.postgresqlBinPath}"\pg_dump -v -a -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user}  <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fc  -f %BCKP_FOLDER% ${model.modelDb.name}

echo "> Sauvegarde de la structure des tables and co..."
"${model.postgresqlBinPath}"\pg_dump %DB_PARAMS% -v --schema-only --no-owner --clean <#list model.schemas as schema> --schema=${schema} <#else></#list> -f %BCKP_FOLDER%\structure ${model.modelDb.name}

:: Suppression des lignes commençant par CREATE SCHEMA et ALTER SCHEMA et DROP SCHEMA et tout un tas de trucs dont on n'a pas besoin
:: Nota : /c:"ALTER DEFAULT PRIVILEGES FOR ROLE [^ ]* IN SCHEMA"  ne marche pas ...
echo ">> Suppression des commandes relatives au schema..."
findstr /v  /c:"CREATE SCHEMA" /c:"ALTER SCHEMA" /c:"DROP SCHEMA" /c:"REVOKE ALL ON SCHEMA" /c:"REVOKE ALL ON SCHEMA" /c:"GRANT ALL ON SCHEMA" /c:"GRANT ALL ON SCHEMA" /c:"GRANT USAGE ON SCHEMA" /c:"GRANT USAGE ON SCHEMA" /c:"SET SESSION AUTHORIZATION" /c:"GRANT ALL ON SCHEMA" /c:"RESET SESSION AUTHORIZATION;" /c:"ALTER DEFAULT PRIVILEGES FOR ROLE" %BCKP_FOLDER%\structure > %BCKP_FOLDER%\structure2

echo "> Sauvegarde des données ..."
"${model.postgresqlBinPath}"\pg_dump %DB_PARAMS% -v --data-only -j ${model.nbThreads} <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fd -f %BCKP_FOLDER%\data ${model.modelDb.name}


echo "Sauvegarde terminée."


