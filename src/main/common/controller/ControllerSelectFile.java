package main.common.controller;

import main.Gui;
import main.common.controller.AController;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Handle file selection dialog and display of selected file(s) in associated text field
 */
public class ControllerSelectFile extends AController
{
  protected JTextComponent txtComponent;
  protected int            mode;
  protected boolean        enableMultiSelection;
  protected String         homeDir;

  /**
   * Remember the last dir in which files where picked from, so as to be able to
   * open the file chooser in that same location next time
   */
  protected File           lastDir = null;

  /**
   * If the textComponent is a text field, then enableMultiSelection should be set to false
   * If the textComponent is a text area, then enableMultiSelection should be set to false
   * All other cases are out of boundaries. Warning : no check performed (TODO ?)
   *
   * @param gui
   * @param textComponent where the selected file(s) should be displayed
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
  protected void doo_()
  {   gui.loggerGui.debug("Button for " + txtComponent.getName() + " was pressed.");

    // Open file selection dialog box
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(mode);
    fc.setMultiSelectionEnabled(enableMultiSelection);
    fc.setCurrentDirectory(lastDir == null ? ( homeDir == null ? null               // "Passing in <code>null</code> sets the  file chooser to point to the user's default directory."
                                                               : new File(homeDir)
                                             )
                                           : lastDir
                          );
    int fcRetVal = fc.showOpenDialog(gui.rootPanel);       // Show the dialog; wait until dialog is closed

    // Retrieve the selected files
    if (fcRetVal == JFileChooser.APPROVE_OPTION)
    {
      if (txtComponent instanceof JTextField)
      {
        lastDir = fc.getSelectedFile().getParentFile();
        txtComponent.setText(fc.getSelectedFile().getAbsolutePath());
      }
      else if (txtComponent instanceof JTextArea)
      {
        lastDir = fc.getSelectedFiles()[0].getParentFile();
        // Clear display of selected files
        txtComponent.setText(null);
        // Display selected files
        for (File f : fc.getSelectedFiles())
        {   ((JTextArea) txtComponent).append(f.getName() + "\n");  // TODO get Abst path ?
        }
      }
      // Hook
      processHook(fc);
    }
    else
    {   logger.debug("Open command cancelled by user.");
    }
  }


  @Override
  protected void updateModel_()
  {   // Nothing
  }


  /**
   * Hook to provide possibility for extending classes to do processing on the selected files
   * Open for overriding
   * @param fc
   */
  protected void processHook(JFileChooser fc)
  {
    // Do the processing in extending classes
  }


}
