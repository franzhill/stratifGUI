:: Stops the shell echoing every command
@ECHO OFF



:: Variable d'env pour pgsql
set PGPASSWORD=${model.modelDb.password}




:: Utilisation du format de sauvegarde personnalisé de pg_dump (custom) (option -Fc):
:: nécéssite que la bibliothèque de compression zlib soit disponible sur le système
echo "Sauvegarde..."
"${model.postgresqlBinPath}"\pgdump -Fc ${model.modelDb.name} > nom_fichier


:: Suppression préalable du contenu du répertoire de svg
:: En effet si celui-ci n'est pas vide, pg_dump sort en erreur
RMDIR repertoire_de_svg /S /Q
MD repertoire_de_svg


:: fonctionnalité de sauvegarde en parallèle de pg_dump.  Pour accélérer la sauvegarde d'une grosse base de données,
:: vous pouvez utiliser le mode parallélisé de pg_dump. Cela sauvegardera plusieurs tables à la fois.
:: Vous pouvez contrôler le degré de parallélisme avec le paramètre -j.
:: Les sauvegardes en parallèle n'acceptent que le format répertoire. (=> donc pas le format custom c)

pg_dump -j num_jobs -Fd -f repertoire_de_svg ${model.modelDb.name}

 --exclude-schema=SCHEMA
 --exclude-table=TABLE
 --schema=SCHEMA          dump the named schema(s) only



:: Restaurer avec :
pg_restore -d ${model.modelDb.name} -j num_jobs repertoire_de_svg

