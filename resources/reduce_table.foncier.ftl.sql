-- <#-- (Freemarker comment)
--
-- Ce fichier contient les commandes SQL permetttant de "réduire" la table de données issue de la couche "foncier".
-- (càd supprimer les colonnes non utilisées pour la stratification).
--
-- Ce fichier est "appelé" par le script bat 'chargement.foncier...bat' (pour le département en cours),
-- une fois les données issues de la couche foncier chargées en BD.
--
-- Techniquement, ce fichier n'est pas un SQL, il s'agit d'un "template Freemarker" c'est à dire qu'il sera prétraité
-- par Java avant d'être un fichier SQL pur. Ce prétraitement permet de remplacer les "placholders" du type "${...}".
-- Exemple :
--   "${fd.schemaTable}" sera remplacé par le vrai nom de la table, par exemple  "ff.d16_2017_pnb10_parcelle"
--
-- En ce qui concerne la "réduction" à proprement parler (suppression des colonnes inutiles) la manière la plus simple
-- de procéder est de lister (écrire) dans le DROP tous les champs de la table d'origine (>130 champs) puis de commenter
-- les champs qui doivent être conservés.
--
-- (Freemarker comment end) -->


ALTER TABLE ${fd.schemaTable}
DROP COLUMN IF EXISTS    ccopre        CASCADE,
-- DROP COLUMN IF EXISTS ccosec ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS dnupla ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS dcntpa ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS idpar  ,   A CONSERVER => commenté
DROP COLUMN IF EXISTS    idsec         CASCADE,
DROP COLUMN IF EXISTS    idprocpte     CASCADE,
DROP COLUMN IF EXISTS    idparref      CASCADE,
DROP COLUMN IF EXISTS    idsecref      CASCADE,
DROP COLUMN IF EXISTS    idvoie        CASCADE,
DROP COLUMN IF EXISTS    idcom         CASCADE,
DROP COLUMN IF EXISTS    idcomtxt      CASCADE,
-- DROP COLUMN IF EXISTS ccodep ,   A CONSERVER => commenté
DROP COLUMN IF EXISTS    ccodir        CASCADE,
-- DROP COLUMN IF EXISTS ccocom ,   A CONSERVER => commenté
DROP COLUMN IF EXISTS    ccopre        CASCADE,
-- DROP COLUMN IF EXISTS ccosec ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS dnupla ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS dcntpa ,   A CONSERVER => commenté
DROP COLUMN IF EXISTS    dsrpar        CASCADE,
DROP COLUMN IF EXISTS    dnupro        CASCADE,
DROP COLUMN IF EXISTS    jdatat        CASCADE,
DROP COLUMN IF EXISTS    jdatatv       CASCADE,
DROP COLUMN IF EXISTS    dreflf        CASCADE,
DROP COLUMN IF EXISTS    gpdl          CASCADE,
DROP COLUMN IF EXISTS    cprsecr       CASCADE,
DROP COLUMN IF EXISTS    ccosecr       CASCADE,
DROP COLUMN IF EXISTS    dnuplar       CASCADE,
DROP COLUMN IF EXISTS    dnupdl        CASCADE,
DROP COLUMN IF EXISTS    gurbpa        CASCADE,
DROP COLUMN IF EXISTS    dparpi        CASCADE,
DROP COLUMN IF EXISTS    ccoarp        CASCADE,
DROP COLUMN IF EXISTS    gparnf        CASCADE,
DROP COLUMN IF EXISTS    gparbat       CASCADE,
DROP COLUMN IF EXISTS    dnuvoi        CASCADE,
DROP COLUMN IF EXISTS    dindic        CASCADE,
DROP COLUMN IF EXISTS    ccovoi        CASCADE,
DROP COLUMN IF EXISTS    ccoriv        CASCADE,
DROP COLUMN IF EXISTS    ccocif        CASCADE,
DROP COLUMN IF EXISTS    cconvo        CASCADE,
DROP COLUMN IF EXISTS    dvoilib       CASCADE,
DROP COLUMN IF EXISTS    idparm        CASCADE,
DROP COLUMN IF EXISTS    ccocomm       CASCADE,
DROP COLUMN IF EXISTS    ccoprem       CASCADE,
DROP COLUMN IF EXISTS    ccosecm       CASCADE,
DROP COLUMN IF EXISTS    dnuplam       CASCADE,
DROP COLUMN IF EXISTS    type          CASCADE,
DROP COLUMN IF EXISTS    typetxt       CASCADE,
DROP COLUMN IF EXISTS    ccoifp        CASCADE,
DROP COLUMN IF EXISTS    jdatatan      CASCADE,
DROP COLUMN IF EXISTS    jannatmin     CASCADE,
DROP COLUMN IF EXISTS    jannatmax     CASCADE,
DROP COLUMN IF EXISTS    jannatminh    CASCADE,
DROP COLUMN IF EXISTS    jannatmaxh    CASCADE,
DROP COLUMN IF EXISTS    janbilmin     CASCADE,
DROP COLUMN IF EXISTS    nsuf          CASCADE,
DROP COLUMN IF EXISTS    ssuf          CASCADE,
-- DROP COLUMN IF EXISTS cgrnumd ,   A CONSERVER => commenté
DROP COLUMN IF EXISTS    cgrnumdtxt    CASCADE,
DROP COLUMN IF EXISTS    dcntsfd       CASCADE,
DROP COLUMN IF EXISTS    dcntarti      CASCADE,
DROP COLUMN IF EXISTS    dcntnaf       CASCADE,
DROP COLUMN IF EXISTS    dcnt01        CASCADE,
DROP COLUMN IF EXISTS    dcnt02        CASCADE,
DROP COLUMN IF EXISTS    dcnt03        CASCADE,
DROP COLUMN IF EXISTS    dcnt04        CASCADE,
DROP COLUMN IF EXISTS    dcnt05        CASCADE,
DROP COLUMN IF EXISTS    dcnt06        CASCADE,
DROP COLUMN IF EXISTS    dcnt07        CASCADE,
DROP COLUMN IF EXISTS    dcnt08        CASCADE,
DROP COLUMN IF EXISTS    dcnt09        CASCADE,
DROP COLUMN IF EXISTS    dcnt10        CASCADE,
DROP COLUMN IF EXISTS    dcnt11        CASCADE,
DROP COLUMN IF EXISTS    dcnt12        CASCADE,
DROP COLUMN IF EXISTS    dcnt13        CASCADE,
DROP COLUMN IF EXISTS    schemrem      CASCADE,
DROP COLUMN IF EXISTS    nlocal        CASCADE,
DROP COLUMN IF EXISTS    nlocmaison    CASCADE,
DROP COLUMN IF EXISTS    nlocappt      CASCADE,
DROP COLUMN IF EXISTS    nloclog       CASCADE,
DROP COLUMN IF EXISTS    nloccom       CASCADE,
DROP COLUMN IF EXISTS    nloccomrdc    CASCADE,
DROP COLUMN IF EXISTS    nloccomter    CASCADE,
DROP COLUMN IF EXISTS    ncomtersd     CASCADE,
DROP COLUMN IF EXISTS    ncomterdep    CASCADE,
DROP COLUMN IF EXISTS    nloccomsec    CASCADE,
DROP COLUMN IF EXISTS    nlocdep       CASCADE,
DROP COLUMN IF EXISTS    nlocburx      CASCADE,
DROP COLUMN IF EXISTS    tlocdomin     CASCADE,
DROP COLUMN IF EXISTS    nbat          CASCADE,
DROP COLUMN IF EXISTS    nlochab       CASCADE,
DROP COLUMN IF EXISTS    nlogh         CASCADE,
DROP COLUMN IF EXISTS    npevph        CASCADE,
DROP COLUMN IF EXISTS    stoth         CASCADE,
DROP COLUMN IF EXISTS    stotdsueic    CASCADE,
DROP COLUMN IF EXISTS    nloghvac      CASCADE,
DROP COLUMN IF EXISTS    nloghmeu      CASCADE,
DROP COLUMN IF EXISTS    nloghloue     CASCADE,
DROP COLUMN IF EXISTS    nloghpp       CASCADE,
DROP COLUMN IF EXISTS    nloghautre    CASCADE,
DROP COLUMN IF EXISTS    nloghnonh     CASCADE,
DROP COLUMN IF EXISTS    nhabvacant    CASCADE,
DROP COLUMN IF EXISTS    nactvacant    CASCADE,
DROP COLUMN IF EXISTS    nloghvac2a    CASCADE,
DROP COLUMN IF EXISTS    nactvac2a     CASCADE,
DROP COLUMN IF EXISTS    nloghvac5a    CASCADE,
DROP COLUMN IF EXISTS    nactvac5a     CASCADE,
DROP COLUMN IF EXISTS    nmediocre     CASCADE,
DROP COLUMN IF EXISTS    nloghlm       CASCADE,
DROP COLUMN IF EXISTS    nloghlls      CASCADE,
DROP COLUMN IF EXISTS    npevp         CASCADE,
DROP COLUMN IF EXISTS    sprobati      CASCADE,
DROP COLUMN IF EXISTS    sprotot       CASCADE,
DROP COLUMN IF EXISTS    npevd         CASCADE,
DROP COLUMN IF EXISTS    stotd         CASCADE,
DROP COLUMN IF EXISTS    spevtot       CASCADE,
DROP COLUMN IF EXISTS    tpevdom_s     CASCADE,
DROP COLUMN IF EXISTS    nlot          CASCADE,
DROP COLUMN IF EXISTS    pdlmp         CASCADE,
DROP COLUMN IF EXISTS    ctpdl         CASCADE,
DROP COLUMN IF EXISTS    typecopro2    CASCADE,
DROP COLUMN IF EXISTS    ncp           CASCADE,
DROP COLUMN IF EXISTS    ndroit        CASCADE,
DROP COLUMN IF EXISTS    ndroitindi    CASCADE,
DROP COLUMN IF EXISTS    ndroitpro     CASCADE,
DROP COLUMN IF EXISTS    ndroitges     CASCADE,
DROP COLUMN IF EXISTS    typprop       CASCADE,
DROP COLUMN IF EXISTS    typproptxt    CASCADE,
DROP COLUMN IF EXISTS    typproppro    CASCADE,
DROP COLUMN IF EXISTS    typpropprotxt CASCADE,
DROP COLUMN IF EXISTS    typpropges    CASCADE,
DROP COLUMN IF EXISTS    typpropgestxt CASCADE,
DROP COLUMN IF EXISTS    locprop       CASCADE,
DROP COLUMN IF EXISTS    locproptxt    CASCADE,
DROP COLUMN IF EXISTS    geomloc       CASCADE,
-- DROP COLUMN IF EXISTS geompar,    A CONSERVER => commenté
DROP COLUMN IF EXISTS    source_geo    CASCADE --,  Suppression de la dernière virgule avant le point virgule, ne pas oublier !
-- DROP COLUMN IF EXISTS vecteur ,   A CONSERVER => commenté
-- DROP COLUMN IF EXISTS contour     A CONSERVER => commenté
;


-- Ménage
-- ALTER TABLE DROP COLUMN ne fait que désactiver les colonnes sans récupérer l'espace qu'elles prenaient.
-- Pour ce faire il faut :
VACUUM FULL ${fd.schemaTable};

-- Index
CREATE INDEX ${fd.table}_idx ON ${fd.schemaTable} USING gist (geompar);

