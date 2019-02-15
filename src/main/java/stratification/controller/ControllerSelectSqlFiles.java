package main.java.stratification.controller;

import main.java.Gui;
import main.java.common.controller.ControllerSelectFile;
import main.java.stratification.model.ModelStrat;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.Arrays;

public class ControllerSelectSqlFiles extends ControllerSelectFile
{
  public ControllerSelectSqlFiles(Gui gui, ModelStrat model, JTextComponent textComponent, int mode, boolean enableMultiSelection, @Nullable String homeDir)
  {
    super(gui, textComponent, mode, enableMultiSelection, homeDir, model);
  }

  @Override
  protected void processHook(JFileChooser fc)
  {   // Another way than having the exact Model type as a parameter (see AController<M extends AModel>)
    // is to simply cast the model as the Model type.
    ((ModelStrat) model).sqlFiles = new ArrayList<>(Arrays.asList(fc.getSelectedFiles()));
  }



}
