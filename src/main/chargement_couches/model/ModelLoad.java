package main.chargement_couches.model;

import lombok.Getter;
import main.common.model.AModel;
import main.common.model.ModelDb;
import main.utils.MyStringUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fhill
 */
public class ModelLoad extends AModel
{
  // The @ Getter annotations are there to let Freemarker be able to access the properties

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
   * // TODo rename FileDep in DepFile or depFiles in fileDeps
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

  /**
   * Full path
   * Private because we need to to some reformating on set
   */
  @Getter private String foncierDumpExtractFilePath;


  /**
   * Max nb of threads to converse with DB with
   * Private because we need to to some reformating on set
   */
  @Getter private int nbThreads;



  public void setPostgresqlBinPath(String path)
  { // Path may have spaces so add ""
    //postgresqlBinPath = '"' + FilenameUtils.separatorsToSystem(path) + '"';
    // Add " " inside bat template
    postgresqlBinPath = FilenameUtils.separatorsToSystem(path);  // TODO see if even necessary
  }

  public void setTempFolderPath(String path)
  { tempFolderPath = FilenameUtils.separatorsToSystem(path);   // TODO see if even necessary
  }


  public void setNbThreads(String nb)
  { try
    { nbThreads = Integer.parseInt(nb);
    }
    catch (Exception e)
    { logger.warn("Nb de threads indiqué non correct => 1 seul thread d'exécution");
      nbThreads = 1;
    }
  }







  /**
   * True when some info is missing
   * @return
   */
  public boolean isIncomplete()
  {
    return  MyStringUtils.isNullOrEmpty(postgresqlBinPath) ||
            MyStringUtils.isNullOrEmpty(tempFolderPath)    ||
            modelDb.isIncomplete();
  }


  /**
   * Finalise : do all transformations and stuff before final use (i.e. injection in template)
   */

}
