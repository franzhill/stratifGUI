package main;

import main.common.MyFileUtils;
import main.ex.DirException;

import java.io.File;
import java.nio.file.Files;

/**
 * Execute all bat files contained in a dir
 */
public class BatFolderExecutor
{
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


  private BatFolderExecutor(String dirPath)
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
    // Move to WIP
    File fwip = new File(wip + File.separator + f.getName());
    f.renameTo(fwip);  // Moves the actual underlying system file

    // Execute
    SysCommand syscommand = new SysCommand(fwip.getAbsolutePath());
    int retValue = syscommand.execute();

    if (retValue == 0)
    { // Move bat file to Done
      // TODO
    }
    else
    { // Else to Error
      // TODO
    }
  }
}
