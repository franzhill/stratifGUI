package main.stratification.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.chargement_couches.model.ModelCouche;
import main.common.controller.AController;
import main.stratification.model.ModelStrat;

public abstract class AControllerStrat extends AController<ModelStrat>
{

  public AControllerStrat(Gui gui, ModelStrat model)
  { super(gui);
    this.model = model;
  }



}
