package main.java.b.backup.controller;

import main.java.Gui;
import main.java.b.backup.model.ModelBckp;
import main.java.b.common.controller.ControllerBckpRstoExecuteScripts;
import main.java.b.common.controller.ControllerBckpRstoGenerateScripts;
import main.java.b.common.model.ModelBckpRsto;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;


/**
 * Adds nothing to the class it extends.
 * Exists only for the sake of consistency, mirroring the *Generate* classes.
 */
public class ControllerBckpExecuteScripts extends ControllerBckpRstoExecuteScripts
{
  public ControllerBckpExecuteScripts(Gui gui, ModelBckp model)
  {   super(gui, model);
  }
}
