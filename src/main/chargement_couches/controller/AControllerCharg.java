package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.model.ModelCouche;
import main.common.controller.AController;
import main.common.model.AModel;

public abstract class AControllerCharg extends AController<ModelCharg>
{
// DOES NOT WORK
//
//  /**
//   * This field effectively hides the one declared in parent class.
//   * Not considered a best practise, but it's the only way I've found
//   * to avoid having to explicitly cast every mention to this.model to a ModelCharg
//   */
//  protected ModelCharg model;


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


  protected abstract void updateModel__();


  private void updateModelCouche()
  {
    String coucheType = gui.getRdoCoucheSelected().getActionCommand();  // TODO getRdoCoucheSelected() could be null if so this means couche was not selected by user => warning popup on GUI

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
