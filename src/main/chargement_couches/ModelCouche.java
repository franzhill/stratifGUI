package main.chargement_couches;

import lombok.Getter;

/**
 * The @ Getter annotations are there to let Freemarker be able to access the properties
 */
public class ModelCouche
{
  /**
   * Kinds of layer (couche)
   */
  public final static String TOPO    = "TOPO";
  public final static String ALTI    = "ALTI";
  public final static String AUTRE   = "AUTRE";
  public static final String FONCIER = "FONCIER";


  /**
   * Kind of layer (couche) to be loaded
   */
  //@MagicConstant(stringValues = {ModelCouche.TOPO, ModelCouche.ALTI, ModelCouche.FONCIER, ModelCouche.AUTRE})  //  IntelliJ annotation
  public String type;

  /**
   * Straightforward departement, or detection pattern
   */
  @Getter public String dep;
  @Getter public String detectFiles;
  @Getter public String fileExt    ;
  @Getter public String schema     ;
  @Getter public String table      ;
  @Getter public String binCmd;

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
  public ModelCouche(String type, String dep, String detectFiles, String fileExt, String schema, String table, String binCmd)
  {
    this.type = type;
    this.dep = dep;
    this.detectFiles = detectFiles;
    this.fileExt = fileExt;
    this.schema = schema;
    this.table = table;
    this.binCmd = binCmd;
  }
}
