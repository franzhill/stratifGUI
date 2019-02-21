package main.java.a.stratification.controller;

import main.java.Gui;
import main.java.common.controller.AController;
import main.java.a.stratification.model.ModelStrat;

public abstract class AControllerStrat extends AController<ModelStrat>
{

  public AControllerStrat(Gui gui, ModelStrat model)
  { super(gui, model);
    //# this.model = model;
  }

  @Override
  protected final void updateModel_()
  {
    updateModel__();
  }

  /**
   * Open for overriding
   */
  protected void updateModel__() {};
}
