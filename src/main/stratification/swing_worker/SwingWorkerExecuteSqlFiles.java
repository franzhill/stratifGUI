package main.stratification.swing_worker;


import main.Gui;
import main.common.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.common._excp.DirException;
import main.common._excp.ExecutionException;
import main.common.swing_worker.ASwingWorker;
import main.common.tool.bat.TemplateProcessor;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.swingWorker.LogMessage;
import main.stratification.model.ModelStrat;
import main.utils.MyFileUtils;
import org.apache.logging.log4j.Level;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;


/**
 * From the thread this worker will be executing in, we'll play the sql files
 * onto the DB. (we can multi-thread).
 *
 * @author fhill
 */
public class SwingWorkerExecuteSqlFiles extends ASwingWorker<ModelStrat>
{
  protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

  /**
   * Folder to generate scripts to.
   */
  private File folder;

  /**
   * To log messages from processes launched from this thread or child threads
   */
  private IOutputHandler outputHandler;

  /**
   * Max nb of threads to execute scripts with.
   */
  private int nbThreads;

  /**
   *
   * @param folder see MultiThreadedBatFolderExecutor
   * @param outputHandler see MultiThreadedBatFolderExecutor
   * @param nbThreads see MultiThreadedBatFolderExecutor
   */
  /**
   *
   * @param gui
   * @param model
   * @param actionButton
   * @param progressBar pass null if no progressBar to manage
   * @param outputHandler
   * @param nbThreads
   */
  public SwingWorkerExecuteSqlFiles(Gui gui, ModelStrat model, JButton actionButton, JProgressBar progressBar, IOutputHandler outputHandler, int nbThreads)
  {
    super(gui, model, actionButton, progressBar);
    this.outputHandler = outputHandler;
    this.nbThreads = nbThreads;
  }


  @Override
  protected Integer doInBackground_()
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts sql..."));

    setProgress(1);

    // Use a MultiThreadedBatFolderExecutor to execute all bat files
    MultiThreadedBatFolderExecutor bfe = new MultiThreadedBatFolderExecutor(model.workFolder, gui.loggerGui, this.outputHandler, nbThreads);
    try
    { publish(new LogMessage(Level.INFO, "Début de l'exécution des scripts sql..."));
      setProgress(0);
      bfe.execute();
    }
    catch (DirException e)
    { e.printStackTrace(); // TODO handle
    }

    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution des scripts sql ."));
    return 1;
  }

}