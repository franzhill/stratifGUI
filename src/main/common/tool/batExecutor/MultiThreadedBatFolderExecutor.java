package main.common.tool.batExecutor;

import main.common._excp.DirException;
import main.common.tool.exec.StreamGobbler;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.utils.MyFileUtils;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Execute all bat scripts contained in a folder - in multithreaded fashion
 * @author fhill
 */
public class MultiThreadedBatFolderExecutor extends BatFolderExecutor
{
  private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  private int nbThreads = 1;

  /**
   * To log messages from this thread or subsequent child threads
   */
  private Logger guiLogger;

  private List<Future>                  futures = new ArrayList<>();
  private List<BatFolderScriptExecutor> bfses   = new ArrayList<>();


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
      throw new DirException(String.format("Erreur de lecture du répertoire des .bat spécifié : [%s]", dir.getAbsolutePath()));
    }
    ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
    int counter = 0;



    for (File f : directoryListing)
    {
      // If it's a bat file, execute it
      if (MyFileUtils.isBatfile(f))
      { counter++;
        //# Runnable worker = new WorkerThread(new SysCommand(f.getAbsolutePath(), this.outputHandler));
        //# executor.execute(worker);  // this instead of the above would not return a handle ("Future") on the runnable. If we want to be
        // able to cancel the runnables, we need these handles

        BatFolderScriptExecutor bfse = new BatFolderScriptExecutor(this.dir, guiLogger, outputHandler, f);
        bfses  .add(bfse);
        futures.add(executor.submit(bfse));
      }
    }

    executor.shutdown();  // execute tasks, and no new task accepted after this

    // Problem with the hereafter approach: unnecessarily spins the CPU
    //  See https://stackoverflow.com/questions/5227655/java-executorservice-less-efficient-than-manual-thread-executions
    //# while (!executor.isTerminated())
    //# { logger.debug("waiting for executor.isTerminated()");
    //#   /* nothing */
    //# }
    //#
    // Instead we'll be doing the following:
    try
    { executor.awaitTermination(10, TimeUnit.DAYS); // Blocking call. Big timeout because we don't really want to timeout.
    }
    catch (InterruptedException ie)
    { Thread.currentThread().interrupt(); // see https://stackoverflow.com/questions/1087475/when-does-javas-thread-sleep-throw-interruptedexception
    }

    if (counter == 0) {guiLogger.warn("Attention, aucun script trouvé dans le répertoire de travail."); }
    else              {guiLogger.info("Tous les scripts ({}) ont été exécutés.", counter); }
  }


  public void cancel()
  {
    // cancel (close) all threads presently running and future
    for (Future f : futures)
    { f.cancel(true);
    }
    // Also kill the sys commands that these threads have launched
    for (BatFolderScriptExecutor bfse : bfses)
    { bfse.cancel();
    }

  }

}


