package main.java.chargement_couches.controller;

import main.java.Gui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EXPERIMENTAL
 */
public class ControllerCancelExecuteScripts implements ActionListener
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Gui gui;
  private ControllerExecuteScripts ces;

  public ControllerCancelExecuteScripts(Gui gui, ControllerExecuteScripts ces)
  { this.gui   = gui;
    this.ces   = ces;
  }


  @Override
  public final void actionPerformed(ActionEvent e)
  {
    ces.cancel();
  }
}
