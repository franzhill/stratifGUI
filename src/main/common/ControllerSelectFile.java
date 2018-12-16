package main.common;

import main.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Handles file selection dialog and display of selected file(s) in associated text field
 */
public class ControllerSelectFile extends AController
{
    protected JTextField txtField;
    protected int        mode;
    protected boolean    enableMultiSelection;

    /**
     *
     * @param gui
     * @param txtField where the selected files should be displayed
     * @param mode see JFileChooser.setFileSelectionMode(int mode)
     *             ex.: JFileChooser.DIRECTORIES_ONLY
     * @param enableMultiSelection
     */
    public ControllerSelectFile(Gui gui, JTextField txtField, int mode, boolean enableMultiSelection)
    {   super(gui);
        this.txtField = txtField;
        this.mode     = mode;
        this.enableMultiSelection = enableMultiSelection;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {   gui.loggerGui.debug("Button for " + txtField.getName() + " was pressed.");

        // Open file selection dialog box
        JFileChooser fc = new JFileChooser();
                     fc.setFileSelectionMode(mode);
                     fc.setMultiSelectionEnabled(enableMultiSelection);
        int fcRetVal = fc.showOpenDialog(gui.rootPanel);       // Show the dialog; wait until dialog is closed

        // Retrieve the selected depFiles.
        if (fcRetVal == JFileChooser.APPROVE_OPTION)
        {
            txtField.setText(fc.getSelectedFile().getAbsolutePath());
        }
        else
        {   logger.debug("Open command cancelled by user.");
        }
    }

}
