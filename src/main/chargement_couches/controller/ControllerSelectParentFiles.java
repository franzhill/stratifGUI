package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.common.controller.ControllerSelectFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
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
        super(gui, textComponent, mode, enableMultiSelection, homeDir);
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        super.actionPerformed(e);
    }


    @Override
    protected void processHook(JFileChooser fc)
    {
        ((ModelCharg) model).parents = new LinkedList<File>(Arrays.asList(fc.getSelectedFiles()));
    }



}
