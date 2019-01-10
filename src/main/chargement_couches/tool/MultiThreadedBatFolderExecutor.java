package main.chargement_couches.tool;

import main.common._excp.DirException;
import main.common.tool.SysCommand;
import main.common.tool.outputHandler.IOutputHandler;
import main.utils.MyFileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedBatFolderExecutor extends BatFolderExecutor
{
  private int nbThreads = 1;

  /**
   * To log messages from this thread or subsequent child threads
   */
  private Logger guiLogger;

  /**
   *
   * @param folder see parent
   * @param outputHandler see parent
   * @param nbThreads max number of threads to launch at same time to execute scripts found in folder
   */
  public MultiThreadedBatFolderExecutor(File folder, Logger logger, IOutputHandler outputHandler, int nbThreads)
  {
    super(folder.getAbsolutePath(), outputHandler);
    guiLogger = logger;
    this.nbThreads = nbThreads;
  }

  @Override
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
    ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
    for (File f : directoryListing)
    {
      // If it's a bat file, execute it
      if (MyFileUtils.isBatfile(f))
      {
        //Runnable worker = new WorkerThread(new SysCommand(f.getAbsolutePath(), this.outputHandler));
        Runnable worker = new BatFolderScriptExecutor(this.dir, guiLogger, outputHandler, f);
        executor.execute(worker);
      }
    }
    executor.shutdown();  // will wait for all running threads to finish
    while (!executor.isTerminated()) {/* nothing */}
    guiLogger.info("Tous les scripts ont été exécutés. Vérifier le répertoire des scripts pour constater leur bonne exécution.");
    System.out.println("Finished all threads");
  }
}


