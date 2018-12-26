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
    gui.txtDetectDep        .setText(gui.userConfig.getProp("couches." + coucheLc + ".detect_dep")          );
    gui.txtDetectFiles      .setText(gui.userConfig.getProp("couches." + coucheLc + ".detect_files")        );
    gui.txtFileExt          .setText(gui.userConfig.getProp("couches." + coucheLc + ".file_ext")            );
    gui.txtSchema           .setText(gui.userConfig.getProp("couches." + coucheLc + ".schema")              );
    gui.txtTable            .setText(gui.userConfig.getProp("couches." + coucheLc + ".table")               );
    gui.txtLoadCmd          .setText(gui.userConfig.getProp("couches." + coucheLc + ".bin_cmd")             );
    gui.txtSchemaTableSource.setText(gui.userConfig.getProp("couches." + coucheLc + ".schema_table_source") );

    if (! gui.txtDetectFiles.getText().isEmpty()) { gui.chbDetectFilesSelect(true); }  else {gui.chbDetectFilesSelect(false); }

    gui.txtDetectDep        .setEnabled(true);
    gui.txtDetectFiles      .setEnabled(true);
    gui.txtFileExt          .setEnabled(true);
    gui.txtSchema           .setEnabled(true);
    gui.txtTable            .setEnabled(true);
    gui.txtLoadCmd          .setEnabled(true);
    gui.txtSchemaTableSource.setEnabled(true);

    if (gui.rdoCoucheFoncier.isSelected())
    { gui.txtSchema           .setEnabled(false);
      gui.txtTable            .setEnabled(false);
    }
    else
    { gui.txtSchemaTableSource.setEnabled(false);
    }

    // Update model
    updateModelCouche();


    logger.debug("model.couche.type= " + model.couche.type);

  }



}
