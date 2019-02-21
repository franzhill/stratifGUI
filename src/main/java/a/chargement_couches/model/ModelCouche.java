package main.java.a.chargement_couches.model;

import lombok.Getter;

/**
 * The @ Getter annotations are there to let Freemarker be able to access the properties
 */
public class ModelCouche
{
  /**
   * Kinds of layers (couche)
   */
  public final static String TOPO    = "TOPO";
  public final static String ALTI    = "ALTI";
  public final static String AUTRE   = "AUTRE";
  public static final String FONCIER = "FONCIER";
  public static final String CVI     = "CVI";
  public static final String FORET   = "FORET";

  // The getters are for use by Freemarker

  /**
   * Kind of layer (couche) to be loaded
   */
  //@MagicConstant(stringValues = {ModelCouche.TOPO, ModelCouche.ALTI, ModelCouche.FONCIER, ModelCouche.AUTRE})  //  IntelliJ annotation // does not seem to work
  @Getter public String type;

  /**
   * Straightforward departement, or detection pattern
   */
  @Getter public String dep;
  @Getter public String detectFiles;
  @Getter public String fileExt    ;
  @Getter public String schema     ;
  @Getter public String table      ;
  @Getter public String binCmd     ;
  @Getter public String schemaTableSource ;

  /**
   *
   * @param type
   * @param dep
   * @param detectFiles
   * @param fileExt
   * @param schema
   * @param table
   * @param binCmd
   */
  public ModelCouche(String type, String dep, String detectFiles, String fileExt, String schema, String table, String binCmd, String schemaTableSource)
  {
    this.type = type;
    this.dep = dep;
    this.detectFiles = detectFiles;
    this.fileExt = fileExt;
    this.schema = schema;
    this.table = table;
    this.binCmd = binCmd;
    this.schemaTableSource = schemaTableSource;
  }
}
