package main.java.common.tool.batExecutor;

import main.java.common.tool.exec.SysCommand;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.utils.MyFileUtils;
import main.java.common._excp.DirException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Execute all bat scripts contained in a folder
 * @author fhill
 */
public class BatFolderExecutor
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Dir containing all bat files (path of)
   */
  protected String dir;

  /**
   * Work In Progress folder (path of)
   * Move bat files here just before executing them
   */
  protected String wip;

  /**
   * Done folder (path of)
   * Move bat files here when done (sucessfully)
   */
  protected String done;

  /**
   * Done folder (path of)
   * Move bat files here when execution failed
   */
  protected String error;

  /**
   * Will handle the output from the system command calls (e.g. write messages in the GUI)
   */
  protected IOutputHandler outputHandler;


  /**
   *
   * @param dirPath see class
   * @param outputHandler see class
   */
  public BatFolderExecutor(String dirPath, IOutputHandler outputHandler)
  {
    this.dir  = dirPath;
    this.wip  = this.dir + File.pathSeparator + "WIP";
    this.done = this.dir + File.pathSeparator + "DONE";
    this.error= this.dir + File.pathSeparator + "ERROR";
    this.outputHandler  = outputHandler;
  }


  public void execute() throws DirException
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
    { int nb_bat_files = 0;
      for (File f : directoryListing)
      {
        // If it's a bat file, execute it
        if (MyFileUtils.isBatfile(f))
        { executeBat(f);
          nb_bat_files++;
        }
      }
      if (nb_bat_files == 0 )
      { throw new DirException(String.format("Aucun fichier .bat trouvé dans le répertoire : [%s]", dir.getAbsolutePath()));
      }
    }
  }

  private void executeBat(File f)
  {
    logger.debug("f=" + f.getName());
    logger.debug("Moving file to WIP...");
    // Move to WIP
    //File fwip = new File(wip + File.separator + f.getName());    // TODO TODO TODO

    //f.renameTo(fwip);  // Moves the actual underlying system file // TODO seems not
    logger.debug("Done.");

    // Execute
    logger.debug("Executing...");
    SysCommand syscommand = new SysCommand(f.getAbsolutePath(), this.outputHandler);
    int retValue = syscommand.execute();

    if (retValue == 0)
    { // Move bat file to Done
      logger.debug("retval=0, eveything OK, moving to DONE...");
      // TODO
    }
    else
    { // Else to Error
      logger.debug(String.format("retval=%s, NOK, moving to ERROR...", retValue));
      // TODO
    }

  }
}
