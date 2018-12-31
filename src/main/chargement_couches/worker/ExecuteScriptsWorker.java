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
 * <Integer, LogMessage> = <result of execution ot this worker, type of information that the worker will use to inform (update) the application with its progress>
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


  public ExecuteScriptsWorker(File folder, Logger logger, IOutputHandler outputHandler)
  {
    this.folder = folder;
    guiLogger = logger;
    this.outputHandler = outputHandler;
  }


  @Override
  protected Integer doInBackground() throws Exception
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts..."));
    setProgress(1);


    //int nbThreads = Integer.parseInt(this.gui.userConfig.getProp("couches.max_db_conn", "1"));  // TODO
    int nbThreads = 5;
    MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(this.folder, this.outputHandler, nbThreads);
    try
    { bfe.execute();  // TODO try executing in thread
    }
    catch (DirException e)
    { e.printStackTrace(); // TODO handle
    }


    // More work was done
    publish(new LogMessage(Level.INFO, "Milieu de l'exécution des scripts..."));
    setProgress(10);

    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution des scripts..."));
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