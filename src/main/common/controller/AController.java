package main.common.controller;

import main.Gui;
import main.common.model.AModel;
import main.common.model.ModelDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AController<M extends AModel> implements ActionListener
{
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    protected Gui    gui;
    protected M      model;



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
    abstract public void actionPerformed(ActionEvent e);



    protected void do_() throws Exception {};


  /**
   * Update the parts of the model that need to be updated from the GUI input.
   * Should be called before the action is done.
   * TODO refactor should be called  by the parent actionPerformed()
   */
  protected final void updateModel()
  {
    updateModelDb();
    updateModel_();
  }

  /**
   * Called by updateModel()
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
                                gui.txtDbPassword.getText(),
                                gui.txtDbName    .getText() );
  }


}
