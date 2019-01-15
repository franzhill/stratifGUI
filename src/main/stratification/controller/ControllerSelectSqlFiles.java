package main.stratification.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.common.controller.ControllerSelectFile;
import main.stratification.model.ModelStrat;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class ControllerSelectSqlFiles extends ControllerSelectFile
{
  public ControllerSelectSqlFiles(Gui gui, ModelStrat model, JTextComponent textComponent, int mode, boolean enableMultiSelection, @Nullable String homeDir)
  {
    super(gui, textComponent, mode, enableMultiSelection, homeDir);
    this.model = model;
  }

  @Override
  protected void processHook(JFileChooser fc)
  {   // Another way than having the exact Model type as a parameter (see AController<M extends AModel>)
    // is to simply cast the model as the Model type.
    ((ModelStrat) model).sqlFiles = new ArrayList<>(Arrays.asList(fc.getSelectedFiles()));
  }



}
