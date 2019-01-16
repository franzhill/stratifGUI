package main.stratification.swing_worker;


import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.common._excp.DirException;
import main.common.swing_worker.ASwingWorker;
import main.common.tool.exec.outputHandler.IOutputHandler;
import main.common.tool.swingWorker.LogMessage;
import main.stratification.model.ModelStrat;
import main.utils.MyFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

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
  /**
   * To log messages from processes launched from this thread or child threads
   */
  private IOutputHandler outputHandler;

  /**
   * Work folder where to copy sql files
   */
  private File folder;

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
   * @param folder
   * @param outputHandler
   * @param nbThreads
   */
  public SwingWorkerExecuteSqlFiles(Gui gui, ModelStrat model, JButton actionButton, JProgressBar progressBar, File folder, IOutputHandler outputHandler, int nbThreads)
  {
    super(gui, model, actionButton, progressBar);
    this.folder = folder;
    this.outputHandler = outputHandler;
    this.nbThreads = nbThreads;
  }


  @Override
  protected Integer doInBackground_() throws Exception
  {
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution des scripts sql..."));

    setProgress(1);

    // Generate all sql files for each departement :
    for(String dep : model.selDeps)
    {
      // Foreach sql file :
      for(File f : model.sqlFiles)
      { // Replace placeholder by dep
        // TODO
        File new_f = new File(""); FileUtils.copyFile(f, new_f);
        // Copy file in
        MyFileUtils.moveToDirectory(new_f, new File(model.getTempFolderPath() + File.separator + "STRATIF" + File.separator + dep), true);
      }
    }

    // Generate all bat files (one per departement) that each executes all sql files for a departement
    // TODO
    // use a MultiThreadedBatFolderExecutor
/*
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
*/
    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution des scripts."));
    return 1;
  }

}