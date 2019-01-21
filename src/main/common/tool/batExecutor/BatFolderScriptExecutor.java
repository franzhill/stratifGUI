package main.common.tool.batExecutor;

import main.common._excp.DirException;
import main.common._excp.ExecutionException;
import main.common.tool.exec.SysCommand;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.utils.MyExceptionUtils;
import main.utils.MyFileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * Execute a single bat script contained in the "bat folder"
 * @author fhill
 */
public class BatFolderScriptExecutor implements Runnable
{
  private Logger logger = LogManager.getLogger(BatFolderScriptExecutor.class);

  /**
   * Dir containing all bat files (path of)
   * TODO change to File
   */
  protected String dir;

  /**
   * Work In Progress folder (path of)
   * Move bat files here just before executing them
   */
  protected File wip;

  /**
   * Done folder (path of)
   * Move bat files here when done (sucessfully)
   */
  protected File done;

  /**
   * Done folder (path of)
   * Move bat files here when execution failed
   */
  protected File error;

  /**
   * Will handle the output from the system command calls (e.g. write messages in the GUI)
   */
  protected IOutputHandler outputHandler;

  protected File f;

  /**
   * To log messages from this thread or subsequent child threads
   */
  private Logger guiLogger;

  /**
   *
   * @param dirPath
   * @param logger
   * @param outputHandler
   * @param f bat script
   */
  public BatFolderScriptExecutor(String dirPath, Logger logger, IOutputHandler outputHandler, File f)
  {
    this.dir            = dirPath;
    this.guiLogger      = logger;
    this.outputHandler  = outputHandler;
    this.f              = f;

    this.wip  = new File(this.dir + File.separator + "EN_COURS");
    this.done = new File(this.dir + File.separator + "FINI");
    this.error= new File(this.dir + File.separator + "ERREUR");

    this.wip  .mkdir();
    this.done .mkdir();
    this.error.mkdir();
  }


  @Override
  public void run()
  {
    try
    {
      File dir = new File(this.dir);
      File[] directoryListing = dir.listFiles();  // might return null  if not directory, or if an I/O error occurs.
      if (directoryListing == null)
      { // Handle the case where dir is not really a directory.
        // Checking dir.isDirectory() above would not be sufficient
        // to avoid race conditions with another process that deletes
        // directories.
        throw new DirException(String.format("Erreur de lecture du répertoire des .bat spécifié : [%s]", dir.getAbsolutePath()));
      }
      else
      {
        // If it's a bat file, execute it
        if (MyFileUtils.isBatfile(f))
        {
          guiLogger.info("Exécution du script : " + f.getName() + "...");
          logger.debug("Executing : " + f.getName() + "...");
          logger.debug("Moving file to WIP...");

          // Move to WIP
          try
          { f = MyFileUtils.moveToDirectory(f, this.wip, true);
          }
          catch (Exception e)
          { throw new ExecutionException(String.format("Impossible de déplacer le fichier [%s] dans le répertoire WIP.", f.getAbsolutePath()), e); //, ExceptionUtils.getStackTrace(e));  // TODO trad + amélioration
          }
          logger.debug("Done.");
          logger.debug("File after move : " + f.getAbsolutePath());

          // Execute
          logger.debug("Executing...");
          SysCommand syscommand = new SysCommand(f.getAbsolutePath(), this.outputHandler);
          int retValue = syscommand.execute();
          if (retValue == 0)
          { // Move bat file to Done
            guiLogger.info("Fin (OK) de l'éxécution du script : " + f.getName() + "...");
            logger.debug("retval=0, eveything OK, moving to DONE...");
            try
            { f = MyFileUtils.moveToDirectory(f, this.done, true);
            }
            catch (Exception e)
            { throw new ExecutionException(String.format("Impossible de déplacer le fichier [%s] dans le répertoire DONE.", f.getAbsolutePath()), e);
            }
          }
          else
          { // Else to Error
            logger.debug(String.format("retval=%s, NOK, moving to ERROR...", retValue));
            guiLogger.info("Fin (ERREUR) de l'éxécution du script : " + f.getName() + "...");
            try
            { f = MyFileUtils.moveToDirectory(f, this.error, true);
            }
            catch (Exception e)
            { throw new ExecutionException(String.format("Impossible de déplacer le fichier [%s] dans le répertoire DONE.", f.getAbsolutePath()), e);
            }
          }
        }
      }
    }

    catch (Exception e)
    { String msg = e.getMessage() + "\n" + MyExceptionUtils.getStackMessages(e);
      logger.error(msg);
      guiLogger.error(msg);
      return;
    }
  }


}
