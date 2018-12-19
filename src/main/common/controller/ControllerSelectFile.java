package main.common.controller;

import main.Gui;
import main.common.controller.AController;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Handles file selection dialog and display of selected file(s) in associated text field
 */
public class ControllerSelectFile extends AController
{
    protected JTextComponent txtComponent;
    protected int            mode;
    protected boolean        enableMultiSelection;
    protected String         homeDir;


    /*protected ControllerSelectFile()
    {
    }*/


    /**
     * If the textComponent is a text field, then enableMultiSelection should be set to false
     * If the textComponent is a text area, then enableMultiSelection should be set to false
     * All other cases are out of boundaries. Warning : no check performed (TODO ?)
     *
     * @param gui
     * @param textComponent where the selected files should be displayed
     * @param mode see JFileChooser.setFileSelectionMode(int mode)
     *             ex.: JFileChooser.DIRECTORIES_ONLY
     * @param enableMultiSelection possible to select multiple files/folders
     * @param homeDir open file chooser in this directory
     */
    public ControllerSelectFile(Gui gui, JTextComponent textComponent, int mode, boolean enableMultiSelection, @Nullable String homeDir)
    {   super(gui);
        this.txtComponent = textComponent;
        this.mode         = mode;
        this.enableMultiSelection = enableMultiSelection;
        this.homeDir      = homeDir;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {   gui.loggerGui.debug("Button for " + txtComponent.getName() + " was pressed.");

        // Open file selection dialog box
        JFileChooser fc = new JFileChooser();
                     fc.setFileSelectionMode(mode);
                     fc.setMultiSelectionEnabled(enableMultiSelection);
                     fc.setCurrentDirectory(homeDir == null ? null : new File(homeDir)); // "Passing in <code>null</code> sets the  file chooser to point to the user's default directory."
        int fcRetVal = fc.showOpenDialog(gui.rootPanel);       // Show the dialog; wait until dialog is closed

        // Retrieve the selected depFiles.
        if (fcRetVal == JFileChooser.APPROVE_OPTION)
        {
            if (txtComponent instanceof JTextField)
            {   txtComponent.setText(fc.getSelectedFile().getAbsolutePath());
            }
            else if (txtComponent instanceof JTextArea)
            {   // Clear display of selected files
                txtComponent.setText(null);
                // Display selected files
                for (File f : fc.getSelectedFiles()) {
                    ((JTextArea) txtComponent).append(f.getName() + "\n");  // TODO get Abst path ?
                }
            }
            // Hook
            processHook(fc);
        }
        else
        {   logger.debug("Open command cancelled by user.");
        }
    }


    /**
     * Hook to provide possibility for extending classes to
     * do processing on the selected files
     * To be overriden by extending classes
     * @param fc
     */
    protected void processHook(JFileChooser fc)
    {
        // Do the processing in extending classes
    }


}
