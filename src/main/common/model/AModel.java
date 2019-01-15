package main.common.model;

import lombok.Getter;
import main.Gui;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AModel
{
  protected Logger logger = LoggerFactory.getLogger(AModel.class);

  protected Gui gui;

  // The @ Getter annotations are there to let Freemarker be able to access the properties

  /**
   * DB info
   */
  @Getter public ModelDb modelDb;

  /**
   * Full path
   * Private because we need to to some reformating on set
   */
  @Getter protected String postgresqlBinPath;

  /**
   * Full path
   * Private because we need to to some reformating on set
   */
  @Getter protected String tempFolderPath;

  /**
   * Max nb of threads to converse with DB with
   * Private because we need to to some reformating on set
   */
  @Getter
  private int nbThreads;



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
  { logger.debug("modelDb.isIncomplete()=%b", modelDb.isIncomplete());
    logger.debug("isIncomplete_()=%b"       , isIncomplete_()       );
    return  modelDb.isIncomplete() || isIncomplete_();
  }


  /**
   * @return true when some info necessary to the processing (action) of the "form", is missing
   */
  protected abstract boolean  isIncomplete_();



}
