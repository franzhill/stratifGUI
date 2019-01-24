package main.common.controller;

import main.Gui;
import main.common._excp.ExecutionException;
import main.common._excp.UserLevelException;
import main.common.model.AModel;
import main.common.model.ModelDb;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The mother of all controllers
 * @param <M> either the model used for "chargement couches", or the model for "stratification"
 */
public abstract class AController<M extends AModel> implements ActionListener
{
  protected Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

  protected final Gui    gui;
  protected       M      model;


  public AController(Gui gui, M model)
  {   this.gui   = gui;
    this.model = model;
  }


  public AController(Gui gui)
  {
    super();
    this.gui = gui;
  }


  @Override
  public final void actionPerformed(ActionEvent e)
  {
    updateModel();
    updateGui(e);
    try
    { preDoChecks();
    }
    catch (Exception excp)
    { logger.error(ExceptionUtils.getStackTrace(excp));
      gui.showMessageError("Certains des paramètres fournis ne sont pas valides.", excp);
      return;
    }
    doo();
    postDo();
  }


  /**
   * Update the parts of the model that need to be updated from the GUI input.
   * Called before the action is done.
   * Do not override directly, override updateModel_() instead
   */
  private final void updateModel()
  {
    updateModelDb();
    updateModel_();
    model.finalize();
  }


  /**
   * Open for overriding
   */
  protected void updateGui(ActionEvent e) {}


  /**
   * Should be "do()" but do is a reserved word
   * Do not override directly, override doo_() instead
   */
  private final void doo()
  { try
    { doo_();
    }
    catch (UserLevelException ule)
    { String msg = ule.getMessage();
     /* if (ule.getCause() != null)
      { //msg += "\n\n Infos techniques : \n" + ExceptionUtils.getStackTrace(ule.getCause()); // TODO limit size of printed stack
        msg += "\n\n Infos techniques : \n" + ExceptionUtils.getRootCauseMessage(ule.getCause());  // from the doc : "getRootCauseMessage =   Gets a short message summarising the root cause exception"
      }*/
      gui.showMessageError(msg, ule);
    }
  }


  /**
   * Open for overriding
   * Throw exception (with message to display to user via GUI) to abort action.
   * Do nothing to continue.
   * Design note: here we're using Exceptions in the normal flow of events, which some people might find offensive ;o)
   */
  protected void preDoChecks() throws Exception {}


  /**
   * Open for overriding
   */
  protected void doo_() throws UserLevelException {}


  /**
   * Open for overriding
   */
  protected void postDo() {}


  /**
   * Called by updateModel()
   * Open for overriding
   * In this function, the parts of the model needed for the action should be updated
   * with what was given by the user through the GUI.
   * So in an nutshell : update parts of the model
   *   - where user might have modified input
   *   - that directly impact the present action
   */
  protected abstract void updateModel_();


  protected void updateModelDb()
  {
    // Get the model info from the (pseudo-MVC) View
    model.modelDb = new ModelDb(gui.txtDbHostname.getText(),
                                gui.txtDbPort    .getText(),
                                gui.txtDbUser    .getText(),
                                new String(gui.pwdDbPassword.getPassword()),
                                gui.txtDbName    .getText() );
  }


  /**
   * Used by some child controllers, not all.
   * Yeah I know this is not 100% clean, but the inheritance diagram is a case of catch 22.
   * So best here, rather than duplicating at lower levels
   */
  protected final void emptyWorkDir() throws ExecutionException
  {
    if (shouldEmptyWorkDirectory())
    { try
      { FileUtils.cleanDirectory(model.workFolder);
      }
      catch (IllegalArgumentException e)
      { // In some cases the "leaf" directory of the worfFolder may not yet exist => disregard
        logger.info("Le répertoire des scripts ({}), à vider, n'a pas été trouvé. Cela peut être normal (création plus tard).", model.workFolder);
      }
      catch (Exception e)
      {  throw new ExecutionException(String.format("Impossible de vider le répertoire des scripts (%s). Existe-t-il bien ? Un fichier y est peut-être verrouillé ?", model.workFolder.getAbsolutePath()), e);
      }
    }
  }

  /**
   * Open for overriding
   * Should be declared abstract, but can't, cause all children classes won't be needing it. See emptyWorkDir().
   * @return should we empty the work dir ?
   */
  protected boolean shouldEmptyWorkDirectory() { return true; }

}
