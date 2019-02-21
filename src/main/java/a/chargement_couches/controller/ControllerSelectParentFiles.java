package main.java.a.chargement_couches.controller;

import main.java.Gui;
import main.java.a.chargement_couches.model.ModelCharg;
import main.java.common.controller.ControllerSelectFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Handles parent files selection dialog and display of selected file(s) in associated text field, and model update
 */
public class ControllerSelectParentFiles extends ControllerSelectFile
{
    /**
     * See parent constructor
     */
    public ControllerSelectParentFiles(Gui gui, ModelCharg model, JTextComponent textComponent, int mode, boolean enableMultiSelection, @Nullable String homeDir)
    {
        super(gui, textComponent, mode, enableMultiSelection, homeDir, model);
    }


    @Override
    protected void processHook(JFileChooser fc)
    {
        ((ModelCharg) model).parents = new LinkedList<File>(Arrays.asList(fc.getSelectedFiles()));
    }



}
