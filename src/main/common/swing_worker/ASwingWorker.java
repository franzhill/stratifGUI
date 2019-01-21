package main.common.swing_worker;


import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.common.model.AModel;
import main.common.tool.swingWorker.LogMessage;
import main.utils.MyExceptionUtils;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


/**
 * By using a Swing worker, we'll avoid blocking the thread in which actionPerformed() is executed
 * (the swing worker is executed in a separate thread).
 * Some knots have to be tied here and there though (e.g. to warn the calling thread of the progress of
 * the swing worker thread).
 *
 * The parametrized <Integer, LogMessage> is for <result of execution ot this worker, type of information that the worker will use to inform (update) the application with its progress>
 */
public abstract class ASwingWorker<M extends AModel> extends SwingWorker<Integer, LogMessage> implements PropertyChangeListener
{
  protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

  protected final Gui          gui;
  protected final M            model;
  protected final JButton      actionButton;
  protected final JProgressBar progressBar;


  /**
   *
   * @param gui
   * @param model
   * @param actionButton the action button originating in the launch of this workers
   * @param progressBar  pass null if no progressBar to manage
   */
  public ASwingWorker(Gui gui, M model, JButton actionButton, JProgressBar progressBar)
  { this.gui          = gui;
    this.model        = model;
    this.actionButton = actionButton;
    // Since null is an acceptable value, and to avoid having to programm defensively against null, we'll assign a dummy object
    this.progressBar  = (progressBar != null) ? progressBar
                                              : new JProgressBar();
  }


  @Override
  protected final Integer doInBackground() throws Exception
  { start();
    return doInBackground_();
  }


  protected final void start()
  { resetProgressBar();
    actionButton.setEnabled(false);  // If we disable button then if swingworker does not finish properly (i;e. done() doesn't get called) then user will not be able to call it again
    start_();
  }


  @Override
  protected final void done()
  { logger.debug("");
    setProgress(100);
    progressBar.setValue(100);   // might be redundant with the above?
    actionButton.setEnabled(true);
    done_();
  }


  /**
   * Open for overriding
   * Extending classes should add here behaviour to be executed on doInBackground()
   */
  protected abstract Integer doInBackground_() throws Exception;


  /**
   * Open for overriding
   * Extending classes may add here behaviour to be executed on start()
   */
  protected void start_() {};


  /**
   * Open for overriding
   * Extending classes may add here behaviour to be executed on done()
   */
  protected void done_() {};


  /**
   * This gets called "automatically" regularly by the event dispatcher thread, not by the thread this worker will be executing in.
   */
  @Override
  protected void process(List< LogMessage> chunks)
  {
    // Messages received from the doInBackground() (when invoking the publish() method)
    for (LogMessage lm : chunks)
    {  gui.loggerGui.log(lm.getLevel(), lm.getMessage());
    }
  }


  private static void failIfInterrupted() throws InterruptedException
  {
    if (Thread.currentThread().isInterrupted())
    { throw new InterruptedException("Interrupted while executing/generating/other files...");
    }
  }


  /**
   * Used to show progress in the progress bar
   * @param nb_files_done
   * @param total nb of files to be processed
   */
  public void setProgressFiles(int nb_files_done, int total)
  {
    setProgress(Math.floorDiv(nb_files_done, total));
  }



  // TODO doesn't seem to work very well
  protected void resetProgressBar()
  { //if (progressBar != null)
    //{
      // Now reinitialize progress bar
      try
      {
        Thread.sleep(2000); // To allow user to see the bar is complete before it is reset
      }
      catch (InterruptedException e)
      {
        logger.error("Thread was interrupted while waiting to reset progress bar : " + MyExceptionUtils.getStackMessages(e));
      }
      progressBar.setValue(0);
      progressBar.setStringPainted(false);  // not sure
    //}
  }


  /**
   * Invoked "automatically" when task's progress property changes.
   * Used to show progress in the progress bar
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("progress" == evt.getPropertyName())
    {
      int progress = (Integer) evt.getNewValue();
      //if (progressBar != null)
      { progressBar.setValue(progress);
      }
    }
  }
}