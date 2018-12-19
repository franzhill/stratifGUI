package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.common.controller.AController;

import java.awt.event.ActionEvent;

public class ControllerSelectCouche extends AController
{

  public ControllerSelectCouche(Gui gui, ModelLoad model)
  {   super(gui, model);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    String couche   = e.getActionCommand();
    String coucheLc = e.getActionCommand().toLowerCase();
    logger.debug("Sélection de la couche : " + couche);

    // Display in GUI
    gui.txtDetectDep  .setText(gui.userConfig.getProp("couches." + coucheLc + ".detect_dep")  );
    gui.txtDetectFiles.setText(gui.userConfig.getProp("couches." + coucheLc + ".detect_files"));
    gui.txtFileExt    .setText(gui.userConfig.getProp("couches." + coucheLc + ".file_ext")    );
    gui.txtSchema     .setText(gui.userConfig.getProp("couches." + coucheLc + ".schema")      );
    gui.txtTable      .setText(gui.userConfig.getProp("couches." + coucheLc + ".table")       );
    gui.txtLoadCmd    .setText(gui.userConfig.getProp("couches." + coucheLc + ".bin_cmd")     );
    if (! gui.txtDetectFiles.getText().isEmpty()) { gui.chbDetectFilesSelect(true); }  else {gui.chbDetectFilesSelect(false); }

    // Update model
    updateModelCouche();


    logger.debug("model.couche.type= " + model.couche.type);

  }



}
