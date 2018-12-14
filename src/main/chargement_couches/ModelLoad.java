package main.chargement_couches;

import lombok.Getter;
import main.common.AModel;
import main.common.ModelDb;
import main.common.StringUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * The @ Getter annotations are there to let Freemarker be able to access the properties
 * @author fhill
 */
public class ModelLoad extends AModel
{
  /**
   * DB info
   */
  @Getter public ModelDb modelDb;

  @Getter public ModelCouche couche;

  /**
   * Parent folders in which to look for files to be loaded
   * Public cause heck we're not sending a rocket in space so I'm not going to be using all that getters and setters crap
   */
  public List<File> parents = new LinkedList<File>();

  /**
   * Files to be loaded in the DB
   * Store all depFiles matching all criteria - candidates for loading in DB
   */
  public List<FileDep> depFiles = new LinkedList<FileDep>();

  /**
   * Full path
   * Private because we need to to some reformating on set
   */
  @Getter private String postgresqlBinPath;

  /**
   * Full path
   * Private because we need to to some reformating on set
   */
  @Getter private String tempFolderPath;


  public void setPostgresqlBinPath(String path)
  { // Path may have spaces so add ""
    //postgresqlBinPath = '"' + FilenameUtils.separatorsToSystem(path) + '"';
    // Add " " inside bat template
    postgresqlBinPath = FilenameUtils.separatorsToSystem(path);
  }

  public void setTempFolderPath(String path)
  { tempFolderPath = FilenameUtils.separatorsToSystem(path);
  }


  /**
   * True when some info is missing
   * @return
   */
  public boolean isIncomplete()
  {
    return  StringUtils.isNullOrEmpty(postgresqlBinPath) ||
            StringUtils.isNullOrEmpty(tempFolderPath)    ||
            modelDb.isIncomplete();
  }


  /**
   * Finalise : do all transformations and stuff before final use (i.e. injection in template)
   */

}
