package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;

import java.io.File;

public abstract class AControllerChargGenerateExecute extends AControllerCharg
{
  protected File workFolder;

  public AControllerChargGenerateExecute(Gui gui, ModelCharg model)
  { super(gui, model);
  }


  @Override
  protected final void updateModel__()
  {
    workFolder = new File(model.getTempFolderPath() + File.separator + "CHARG_COUCHE");
    updateModel___();
  }


  /**
   * Open for overriding
   */
  protected void updateModel___() {}


  @Override
  protected boolean preDoChecks()
  {
    // Create workdir if not exists
    workFolder.mkdir();
    return preDoChecks_();
  }


  /**
   * Open for overriding
   */
  protected boolean preDoChecks_() {return true;}

}
