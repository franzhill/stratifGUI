-- <#-- (Freemarker comment)
--
-- Ce fichier contient les commandes SQL permetttant de créer les tables CVI
-- avant chargement des données.
--
-- (Freemarker comment end) -->


DROP TABLE IF EXISTS ${fd.schemaTable};
CREATE TABLE ${fd.schemaTable}
(
  dep          character varying(3),
  id_pcv       character varying(12) NOT NULL,
  surf_viti_m2 integer,
  CONSTRAINT cvi_2017_pk PRIMARY KEY (id_pcv)
);