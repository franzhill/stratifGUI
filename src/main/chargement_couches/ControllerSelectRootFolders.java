package main.chargement_couches;

import main.Gui;
import main.common.AController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class ControllerSelectRootFolders extends AController
{

    public ControllerSelectRootFolders(Gui gui, ModelLoad model)
    {   super(gui, model);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {   gui.loggerGui.debug("Button buttSelectZipFiles was pressed");

        // We're hitting the limits of the MVC design here ...
        // If we want to keep it simple, we'll prompt for selection of depFiles from here,
        // instead of forwarding that demand to a view...

        // Open file selection dialog box
        JFileChooser fc = new JFileChooser();
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(true);                 // let user select multiple depFiles
        int fcRetVal = fc.showOpenDialog(gui.rootPanel);       // Show the dialog; wait until dialog is closed

        // Retrieve the selected depFiles.
        if (fcRetVal == JFileChooser.APPROVE_OPTION)
        {
            // Same here
            // In pure MVC we'd do something like
            //   view.clearDisplayOfSelectedFiles()
            //   model.setSelectedFiles(...)
            //   view.displaySelectedFiles(model.getSelectedFiles)
            //   ...
            // But we're trying to be quick and pragmatic here, not write some overengineered java nuthouse
            // So we'll be cutting a few corners here and there ... ;o)

            model.parents = new LinkedList<File>(Arrays.asList(fc.getSelectedFiles()));
            gui.displaySelectedFiles(model.parents);

        }
        else
        {   logger.debug("Open command cancelled by user.");
        }
    }

}
