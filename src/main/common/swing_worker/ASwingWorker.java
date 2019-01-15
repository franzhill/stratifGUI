package main.common.swing_worker;


import main.Gui;
import main.chargement_couches.model.ModelCharg;
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
public abstract class ASwingWorker extends SwingWorker<Integer, LogMessage> implements PropertyChangeListener
{
  protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

  protected Gui gui;
  protected ModelCharg model;

  public ASwingWorker(Gui gui, ModelCharg model)
  { this.gui   = gui;
    this.model = model;
  }


  @Override
  protected Integer doInBackground() throws Exception
  { start();
    return doInBackground_();
  }

  protected void start()
  { resetProgressBar();
    start_();
  }

  @Override
  protected void done()
  { setProgress(100);
    gui.progbCouche.setValue(100);   // might be redundant with the above?
    done_();
  }


  protected abstract Integer doInBackground_() throws Exception;

  protected abstract void start_();

  protected abstract void done_();


  @Override
  /**
   * This gets called regularly by the event dispatcher thread, not by the thread this worker will be executing in.
   */
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
  public void setProgress_(int nb_files_done, int total)
  {
    setProgress(Math.floorDiv(nb_files_done, total));
  }




  protected void resetProgressBar()
  {
    // Now reinitialize progress bar
    try
    { Thread.sleep(2000); // To allow user to see the bar is complete before it is reset
    }
    catch (InterruptedException e)
    { logger.error("Thread was interrupted while waiting to reset progress bar : " + MyExceptionUtils.getStackMessages(e));
    }
    gui.progbCouche.setValue(0);
    gui.progbCouche.setStringPainted(false);  // not sure
  }


  /**
   * Invoked when task's progress property changes.
   * Used to show progress in the progress bar
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("progress" == evt.getPropertyName())
    {
      int progress = (Integer) evt.getNewValue();
      gui.progbCouche.setValue(progress);
    }
  }
}