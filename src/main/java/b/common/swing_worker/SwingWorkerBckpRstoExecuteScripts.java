package main.java.b.backup.swing_worker;


import main.java.Gui;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.b.backup.model.ModelBckp;
import main.java.common._excp.DirException;
import main.java.common.swing_worker.ASwingWorker;
import main.java.common.tool.batExecutor.BatFolderExecutor;
import main.java.common.tool.batExecutor.MultiThreadedBatFolderExecutor;
import main.java.common.tool.exec.outputHandler.IOutputHandler;
import main.java.common.tool.swingWorker.LogMessage;
import org.apache.logging.log4j.Level;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * See other swing workers for some info
 *
 * @author fhill
 */
public class SwingWorkerBckpExecuteScripts extends ASwingWorker<ModelBckp>
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
   *
   * @param gui
   * @param model
   * @param actionButton
   * @param progressBar  pass null if no progressBar to manage
   * @param folder
   * @param outputHandler
   */
  public SwingWorkerBckpExecuteScripts(Gui gui, ModelBckp model, JButton actionButton, JProgressBar progressBar, JButton cancelButton, File folder, IOutputHandler outputHandler)
  {
    super(gui, model, actionButton, progressBar, cancelButton);
    this.folder = folder;
    this.outputHandler = outputHandler;
  }


  @Override
  protected Integer doInBackground_() throws Exception
  { logger.debug("");
    // Start
    publish(new LogMessage(Level.INFO, "Démarrage de l'exécution du script..."));
    setProgress(1);

    BatFolderExecutor bfe = new BatFolderExecutor(this.folder.getAbsolutePath(), this.outputHandler);
    try
    {
      // More work was done
      //publish(new LogMessage(Level.INFO, "Exécution des scripts en cours..."));
      setProgress(2);
      bfe.execute();
    }
    catch (DirException e)
    { //#e.printStackTrace(); // TODO handle
      //#throw new ExecutionException("", e);  // No real need to throw the execption, we can't catch it in the Event Dispatch Thread
      publish(new LogMessage(Level.WARN, e.getMessage()));
    }

    // Complete
    publish(new LogMessage(Level.INFO, "Fin de l'exécution du script."));
    return 1;
  }

}