package main.common.controller;

import main.Gui;
import main.chargement_couches.model.ModelCouche;
import main.chargement_couches.model.ModelLoad;
import main.common.model.ModelDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AController implements ActionListener
{
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    protected Gui gui;
    protected ModelLoad model;


    public AController(Gui gui, ModelLoad model)
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

    /**
     * Update the whole model, from what's in the GUI
     */
    protected void updateModel()
    {
        updateModelCouche();
    }


    protected void do_() throws Exception {};



  protected void updateModelDb()
  {
    // Get the model info from the (pseudo-MVC) View
    model.modelDb = new ModelDb(gui.txtDbHostname.getText(),
                                gui.txtDbPort    .getText(),
                                gui.txtDbUser    .getText(),
                                gui.txtDbPassword.getText(),
                                gui.txtDbName    .getText() );
  }


    protected void updateModelCouche()
    {
      String coucheType = gui.getRdoCoucheSelected().getActionCommand();  // TODO getRdoCoucheSelected() could be null if so this means couche was not selected by user => warning popup on GUI

      // Set on model
      model.couche = new ModelCouche( coucheType,
                                      gui.rdoDepDetect.isSelected() ? gui.txtDetectDep  .getText()
                                                                    : gui.txtDep        .getText(),
                                      gui.chbDetectFiles.isSelected() ? gui.txtDetectFiles.getText() : "",
                                      gui.txtFileExt    .getText(),
                                      gui.txtSchema     .getText(),
                                      gui.txtTable      .getText(),
                                      gui.txtLoadCmd    .getText()
      );
    }
}
