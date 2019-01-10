package main.chargement_couches.worker;


import main.chargement_couches.tool.MultiThreadedBatFolderExecutor;
import main.common._excp.DirException;
import main.common.tool.log.LogMessage;
import main.common.tool.outputHandler.IOutputHandler;
import main.common.tool.outputHandler.OutputHandlerGui;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.List;


/**
 * By using a Swing worker, we'll avoid blocking the thread in which actionPerformed() is executed
 * (the swing worker is executed in a separate thread).
 * Some knots have to be tied here and there though (e.g. to warn the calling thread of the progress of
 * the swing worker thread).
 *
 * From the thread this worker will be executing in, we'll launch the scripts that will load the
 * data into the DB. (we can multi-thread these executions).
 *
 *
 * The parametrized <Integer, LogMessage> is for <result of execution ot this worker, type of information that the worker will use to inform (update) the application with its progress>
 */
public class ExecuteScriptsWorker extends SwingWorker<Integer, LogMessage>
{
  /**
   * To log messages from this thread or subsequent child threads
   */
  private Logger guiLogger;

  /**
   * To log messages from processes lauched from this thread or child threads
   */
  private IOutputHandler outputHandler;

  private File folder;

  private int nbThreads;

  /**
   *
   * @param folder see MultiThreadedBatFolderExecutor
   * @param logger see class
   * @param outputHandler see MultiThreadedBatFolderExecutor
   * @param nbThreads see MultiThreadedBatFolderExecutor
   */
  public ExecuteScriptsWorker(File folder, Logger logger, IOutputHandler outputHandler, int nbThreads)
  {
    this.folder = folder;
    guiLogger = logger;
    this.outputHandler = outputHandler;
    this.nbThreads = nbThreads;
  }


  @Override
  protected Integer doInBackground() throws Exception
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts..."));
    setProgress(1);

    // TODO : code from the bfe could actually be here. This would allow us to monitor (and feedback) progress to user.
    MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(this.folder, guiLogger, this.outputHandler, nbThreads);
    try
    {
      // More work was done
      publish(new LogMessage(Level.INFO, "Début de l'exécution des scripts en cours..."));
      setProgress(0);

      bfe.execute();
    }
    catch (DirException e)
    { e.printStackTrace(); // TODO handle
    }



    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution des scripts."));
    setProgress(100);
    return 1;
  }

  @Override
  /**
   * This gets called regularly by the event dispatcher thread, not by the thread this worker will be executing in.
   */
  protected void process(List< LogMessage> chunks)
  {
    // Messages received from the doInBackground() (when invoking the publish() method)
    for (LogMessage lm : chunks)
    {  guiLogger.log(lm.getLevel(), lm.getMessage());
    }
  }


  private static void failIfInterrupted() throws InterruptedException {
    if (Thread.currentThread().isInterrupted()) {
      throw new InterruptedException("Interrupted while executing files...");
    }
  }
}