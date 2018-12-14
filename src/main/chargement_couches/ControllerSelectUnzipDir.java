package main.chargement_couches;

import main.Gui;
import main.common.AController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ControllerSelectUnzipDir extends AController
{

    public ControllerSelectUnzipDir(Gui gui)
    {   super(gui);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {   gui.loggerGui.debug("Button buttSelectUnzipDir was pressed");

        // Open file selection dialog box
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);                    // let user select only one dir
        int fcRetVal = fc.showOpenDialog(gui.rootPanel);       // Show the dialog; wait until dialog is closed

        // Retrieve the selected depFiles.
        if (fcRetVal == JFileChooser.APPROVE_OPTION)
        {
            gui.displaySelectedUnzipDir(fc.getSelectedFile());
        }
        else
        {   logger.debug("Open command cancelled by user.");
        }
    }

}
