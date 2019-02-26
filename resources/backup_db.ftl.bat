:: Stops the shell echoing every command
@ECHO OFF

:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}

:: Chemin absolu de la sauvegarde
set BCKP_FOLDER="${model.parentDir}\${model.name}"

:: (-d database volontairement omis ici)
set DB_PARAMS=-p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user}

:: Suppression préalable du contenu du répertoire de sauvegarde.
:: En effet si celui-ci n'est pas vide, pg_dump sort en erreur.
echo "Suppression de l'ancienne sauvegarde de même nom si elle existe..."
IF EXIST %BCKP_FOLDER% RMDIR %BCKP_FOLDER% /S /Q && MD %BCKP_FOLDER%
MD %BCKP_FOLDER%\data


:: Nota : l'utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc)
:: (recommandé pour les grosses sauvegardes par rapport au format par défaut texte SQL) nécéssite que la
:: bibliothèque de compression zlib soit disponible sur le système.

:: Nota : fonctionnalité de sauvegarde en parallèle de pg_dump (-j) : "pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire." (=> donc pas le format custom c)
::
:: Nota : nous fonctionnons dans un contexte bien particulier dans lequel nous ne possédons pas les droits de
:: création/suppression des schemas sur notre base cible (Teruti Prod). Cela rend la sauvegarde et la restauration
:: directe avec pg_dump et pg_restore problématique, même en jouant avec les options -a (data_only) et -c (clean)
:: car nous devons :
:: - supprimer les tables (DROP), les indexes, les séquences ...
:: - les recréer
:: - omettre la suppression/création des schemas
:: - omettre tout ce qui est gestion de droits, utilisateurs etc.
:: ce qu'aucune combinaison d'options ne permet.
:: D'où l'approche en 2 temps suivante :
:: 1) sauvegarde de la structure + édition
:: 2) sauvegarde des données


echo "Sauvegarde..."
:: OLD "${model.postgresqlBinPath}"\pg_dump -v -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user} -j ${model.nbThreads} <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fd  -f %BCKP_FOLDER% ${model.modelDb.name}
:: OLD "${model.postgresqlBinPath}"\pg_dump -v -a -p ${model.modelDb.port} -h ${model.modelDb.hostname} -U ${model.modelDb.user}  <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fc  -f %BCKP_FOLDER% ${model.modelDb.name}

echo "> Sauvegarde de la structure des tables and co..."
"${model.postgresqlBinPath}"\pg_dump %DB_PARAMS% -v --schema-only --no-owner --clean <#list model.schemas as schema> --schema=${schema} <#else></#list> -f %BCKP_FOLDER%\structure ${model.modelDb.name}

:: Suppression des lignes commençant par CREATE SCHEMA et ALTER SCHEMA et DROP SCHEMA et tout un tas de trucs dont on n'a pas besoin
:: Nota : /c:"ALTER DEFAULT PRIVILEGES FOR ROLE [^ ]* IN SCHEMA"  ne marche pas ...
echo ">> Suppression des commandes relatives au schema..."
findstr /v  /c:"CREATE SCHEMA" /c:"ALTER SCHEMA" /c:"DROP SCHEMA" /c:"REVOKE ALL ON SCHEMA" /c:"REVOKE ALL ON SCHEMA" /c:"GRANT ALL ON SCHEMA" /c:"GRANT ALL ON SCHEMA" /c:"GRANT USAGE ON SCHEMA" /c:"GRANT USAGE ON SCHEMA" /c:"SET SESSION AUTHORIZATION" /c:"GRANT ALL ON SCHEMA" /c:"RESET SESSION AUTHORIZATION;" /c:"ALTER DEFAULT PRIVILEGES FOR ROLE" %BCKP_FOLDER%\structure > %BCKP_FOLDER%\structure2

echo "> Sauvegarde des données ..."
"${model.postgresqlBinPath}"\pg_dump %DB_PARAMS% -v --data-only -j ${model.nbThreads} <#list model.schemas as schema> --schema=${schema} <#else></#list> -Fd -f %BCKP_FOLDER%\data ${model.modelDb.name}


echo "Sauvegarde terminée."


