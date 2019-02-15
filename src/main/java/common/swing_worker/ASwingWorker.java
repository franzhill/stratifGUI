package main.java.common.swing_worker;


import main.java.Gui;
import main.java.common.model.AModel;
import main.java.common.tool.swingWorker.LogMessage;
import org.jetbrains.annotations.Nullable;
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
 *
 *  TODO changes to the UI should be done in the Event Dispatch Thread, not from within the thread these workers will be running in (=> use SwingUtilities.invokeLater)
 *  (see https://www.javamex.com/tutorials/threads/invokelater.shtml)
 */
public abstract class ASwingWorker<M extends AModel> extends SwingWorker<Integer, LogMessage> implements PropertyChangeListener
{
  protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  protected final Gui          gui;
  protected final M            model;
  protected final JButton      actionButton;
  protected final JButton      cancelButton;
  protected final JProgressBar progressBar;


  /**
   *
   * @param gui
   * @param model
   * @param actionButton the action button originating in the launch of this workers
   * @param progressBar  pass null if no progressBar to manage
   * @param cancelButton  pass null if no cancelButton to manage
   */
  public ASwingWorker(Gui gui, M model, JButton actionButton, @Nullable JProgressBar progressBar, @Nullable JButton cancelButton)
  { this.gui          = gui;
    this.model        = model;
    this.actionButton = actionButton;
    // Since null is an acceptable value, and to avoid having to program defensively against null, we'll assign a dummy object
    this.cancelButton = (cancelButton != null) ? cancelButton : new JButton();
    // Same
    this.progressBar  = (progressBar != null) ? progressBar : new JProgressBar();
  }


  @Override
  protected final Integer doInBackground() throws Exception
  { start();
    return doInBackground_();
  }


  protected final void start()
  { resetProgressBar();
    actionButton.setEnabled(false);  // If we disable button then if swingworker does not finish properly (i;e. done() doesn't get called) then user will not be able to call it again
    cancelButton.setEnabled(true);
    start_();
  }


  /**
   * Invoked on (ie from) the event dispatch thread
   */
  @Override
  protected final void done()
  { logger.debug("");
    setProgress(100);
    progressBar.setValue(100);   // might be redundant with the above?
    actionButton.setEnabled(true);
    cancelButton.setEnabled(false);
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
      { Thread.sleep(2000); // To allow user to see the bar is complete before it is reset
      }
      catch (InterruptedException e)
      { //# logger.error("Thread was interrupted while waiting to reset progress bar : " + MyExceptionUtils.getStackMessages(e));
        Thread.currentThread().interrupt(); // see https://stackoverflow.com/questions/1087475/when-does-javas-thread-sleep-throw-interruptedexception
      }
      progressBar.setValue(0);
      progressBar.setStringPainted(false);  // not sure
    //}
  }


  /**
   * Invoked "automatically" (by the EDT) when task's progress property changes.
   * Used to show progress in the progress bar
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("progress" == evt.getPropertyName())
    {
      int progress = (Integer) evt.getNewValue();
      //if (progressBar != null)
      { progressBar.setValue(progress);   // TODO changes to the UI should be done in the Event Dispatch Thread, not here (=> use SwingUtilities.invokeLater)
      }
    }
  }
}