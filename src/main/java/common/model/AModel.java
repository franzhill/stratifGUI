package main.java.common.model;

import lombok.Getter;
import main.java.Gui;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public abstract class AModel
{
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected Gui gui;

  // The @ Getter annotations are there to let Freemarker be able to access the properties

  /**
   * DB info
   */
  @Getter public ModelDb modelDb;

  /**
   * Full path
   * Private because we need to to some reformating on setter
   */
  @Getter protected String postgresqlBinPath;

  /**
   * Full path
   * Private because we need to to some reformating on setter
   */
  @Getter protected String tempFolderPath;

  /**
   * The effective work folder where scripts will be written to and processed from.
   * Based on tempFolderPath.
   * Example :
   *  - for chargement_couche it will be something like tempFolderPath/CHARG_COUCHE
   *  - for stratification    it will be something like tempFolderPath/STRATIF
   */
  public File workFolder;

  /**
   * Max nb of threads to converse with DB with
   * Private because we need to to some reformating on set()
   */
  @Getter private int nbThreads;

  /**
   * Should be called just before performing action;
   * Finalize model data so that it's ready for action.
   */
  public abstract void finalize();


  /**
   * Used by Freemarker
   * !! Do not use to get the actual object of type File (use direct access instead in that case)
   */
  public String getWorkFolder()
  { return workFolder.getAbsolutePath();
  }


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
   * Design note: do not override directly, override isIncomplete_()
   * @return true when some info necessary to the processing (action) of the "form", is missing
   */
  public final boolean isIncomplete()
  { logger.debug("modelDb.isIncomplete()={}", modelDb.isIncomplete());
    logger.debug("isIncomplete_()={}"       , isIncomplete_()       );
    return  modelDb.isIncomplete() || isIncomplete_();
  }


  /**
   * @return true when some info necessary to the processing (action) of the "form", is missing
   */
  protected abstract boolean  isIncomplete_();



}
