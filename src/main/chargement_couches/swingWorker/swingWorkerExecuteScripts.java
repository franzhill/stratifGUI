package main.chargement_couches.swingWorker;


import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.chargement_couches.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.common._excp.DirException;
import main.common.swingWorker.ASwingWorker;
import main.common.tool.swingWorker.LogMessage;
import main.common.tool.exec.outputHandler.IOutputHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.List;


/**
 * From the thread this worker will be executing in, we'll launch the scripts that will load the
 * data into the DB. (we can multi-thread these executions).
 *
 * @author fhill
 */
public class SwingWorkerExecuteScripts extends ASwingWorker
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

  /**
   *
   * @param folder see MultiThreadedBatFolderExecutor
   * @param outputHandler see MultiThreadedBatFolderExecutor
   * @param nbThreads see MultiThreadedBatFolderExecutor
   */
  public SwingWorkerExecuteScripts(Gui gui, ModelLoad model, File folder, IOutputHandler outputHandler, int nbThreads)
  {
    super(gui, model);
    this.folder = folder;
    this.outputHandler = outputHandler;
    this.nbThreads = nbThreads;
  }


  @Override
  protected Integer doInBackground_() throws Exception
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts..."));

    setProgress(1);

    // TODO : code from the bfe could actually be here. This would allow us to monitor (and feedback) progress to user.
    MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(this.folder, gui.loggerGui, this.outputHandler, nbThreads);
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
    return 1;
  }



  @Override
  protected void start_()
  { gui.buttExecuteScripts.setEnabled(false);
  }

  @Override
  protected void done_()
  { gui.buttExecuteScripts.setEnabled(true);
  }


}