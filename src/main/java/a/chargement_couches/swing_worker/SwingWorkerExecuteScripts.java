package main.java.a.chargement_couches.swing_worker;


import main.java.Gui;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.common.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.java.common._excp.DirException;
import main.java.common.swing_worker.ASwingWorker;
import main.java.common.tool.swingWorker.LogMessage;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import org.apache.logging.log4j.Level;

import javax.swing.*;
import java.io.File;


/**
 * From the thread this worker will be executing in, we'll launch the scripts that will load the
 * data into the DB. (we can multi-thread these executions).
 *
 * @author fhill
 */
public class SwingWorkerExecuteScripts extends ASwingWorker<ModelCharg>
{
  /**
   * To log messages from processes launched from this thread or child threads
   */
  private IOutputHandler outputHandler;

  /**
   * Folder containing scripts
   */
  private File folder;

  /**
   * Max nb of threads to exectue scripts with.
   */
  private int nbThreads;

  private MultiThreadedBatFolderExecutor bfe;


  /**
   *
   * @param gui
   * @param model
   * @param actionButton
   * @param progressBar  pass null if no progressBar to manage
   * @param folder        see MultiThreadedBatFolderExecutor
   * @param outputHandler see MultiThreadedBatFolderExecutor
   * @param nbThreads     see MultiThreadedBatFolderExecutor
   */
  public SwingWorkerExecuteScripts(Gui gui, ModelCharg model, JButton actionButton, JProgressBar progressBar, JButton cancelButton, File folder, IOutputHandler outputHandler, int nbThreads)
  {
    super(gui, model, actionButton, progressBar, cancelButton);
    this.folder = folder;
    this.outputHandler = outputHandler;
    this.nbThreads = nbThreads;
  }


  @Override
  protected Integer doInBackground_() throws Exception
  { logger.debug("");
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts..."));
    setProgress(1);

    // TODO : code from the bfe could actually be here. This would allow us to monitor (and feedback) progress to user.
    bfe = new MultiThreadedBatFolderExecutor(this.folder, gui.loggerGui, this.outputHandler, nbThreads);
    try
    {
      // More work was done
      //publish(new LogMessage(Level.INFO, "Exécution des scripts en cours..."));
      setProgress(2);
      bfe.execute();
    }
    catch (DirException e)
    { e.printStackTrace(); // TODO handle
    }




    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution des scripts."));
    return 1;
  }


  public void cancel()
  {
    bfe.cancel();
  }

}