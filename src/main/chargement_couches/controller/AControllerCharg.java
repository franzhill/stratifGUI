package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.model.ModelCouche;
import main.common.controller.AController;
import main.common.model.AModel;

public abstract class AControllerCharg extends AController<ModelCharg>
{
  public AControllerCharg(Gui gui, ModelCharg model)
  { super(gui);
    this.model = model;
  }


  @Override
  protected final void updateModel_()
  {
    updateModelCouche();
    updateModel__();
  }

  /**
   * Called by updateModel() eventually.
   * In this function, the parts of the model needed for the action should be updated
   * with what was given by the user through the GUI.
   * So in an nutshell : update parts of the model
   *   - where user might have modified input
   *   - that directly impact the present action
   */
  protected abstract void updateModel__();


  private void updateModelCouche()
  {
    if ( gui.getRdoCoucheSelected() ==  null )
    { // User hasn't selected the couche yet => return (and deal with the problem later along the processing of the action)
      return;
    }
    String coucheType = gui.getRdoCoucheSelected().getActionCommand();

    // Set on model
    model.couche = new ModelCouche(
            coucheType,
            gui.rdoDepDetect.isSelected() ? gui.txtDetectDep  .getText()
                                          : gui.txtDep        .getText(),
            gui.chbDetectFiles.isSelected() ? gui.txtDetectFiles.getText() : "",
            gui.txtFileExt    .getText(),
            gui.txtSchema     .getText(),
            gui.txtTable      .getText(),
            gui.txtLoadCmd    .getText(),
            gui.txtSchemaTableSource.getText()
    );
  }

}
