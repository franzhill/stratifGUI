package main.common;

import main.Gui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AModel
{
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    protected Gui gui;


/*    public AModel(Gui gui)
    {
        super();
        this.gui = gui;
    }
*/
}
