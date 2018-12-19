package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.common.controller.ControllerSelectFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class ControllerSelectRootFolders extends ControllerSelectFile
{
    /**
     * See parent constuctor
     * @param gui
     * @param model
     * @param textComponent
     * @param mode
     * @param enableMultiSelection
     * @param homeDir
     */
    public ControllerSelectRootFolders(Gui gui, ModelLoad model, JTextComponent textComponent, int mode, boolean enableMultiSelection, @Nullable String homeDir)
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
        model.parents = new LinkedList<File>(Arrays.asList(fc.getSelectedFiles()));
    }



}
