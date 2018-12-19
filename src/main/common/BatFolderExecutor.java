package main.common;

import main.utils.MyFileUtils;
import main.ex.DirException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Execute all bat files contained in a dir
 */
public class BatFolderExecutor
{
  private Logger logger = LoggerFactory.getLogger(BatFolderExecutor.class);

  /**
   * Dir containing all bat files (path of)
   */
  private String dir;

  /**
   * Work In Progress folder (path of)
   * Move bat files here just before executing them
   */
  private String wip;

  /**
   * Done folder (path of)
   * Move bat files here when done (sucessfully)
   */
  private String done;

  /**
   * Done folder (path of)
   * Move bat files here when execution failed
   */
  private String error;


  public BatFolderExecutor(String dirPath)
  {
    this.dir  = dirPath;
    this.wip  = this.dir + File.pathSeparator + "WIP";
    this.done = this.dir + File.pathSeparator + "DONE";
    this.error= this.dir + File.pathSeparator + "ERROR";
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
      throw new DirException(String.format("Erreur de lecture du répertoire des .bat spécifié : {%s}", dir.getAbsolutePath()));
    }
    else
    {
      for (File f : directoryListing)
      {
        // If it's a bat file, execute it
        if (MyFileUtils.isBatfile(f))
        {
          executeBat(f);
        }
      }
    }
  }

  private void executeBat(File f)
  {
    logger.debug("f=" + f.getName());
    logger.debug("Moving file to WIP...");
    // Move to WIP
    //File fwip = new File(wip + File.separator + f.getName());

    //f.renameTo(fwip);  // Moves the actual underlying system file // TODO seems not
    logger.debug("Done.");

    // Execute
    logger.debug("Executing...");
    SysCommand syscommand = new SysCommand(f.getAbsolutePath());
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
