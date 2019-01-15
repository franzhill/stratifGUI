package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelCharg;
import main.common.controller.AController;
import main.common.tool.DbConnector;
import main.common._excp.DbConnectionException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.event.ActionEvent;


public class ControllerTestDbConnectivity extends AController
{
    public ControllerTestDbConnectivity(Gui gui, ModelCharg model)
    {   super(gui, model);
    }

    @Override
    public void doo()
    {   gui.loggerGui.info("Test de connectivité ...");

        DbConnector dbc = new DbConnector(model.modelDb);
        try
        {
            dbc.getConn();
            gui.showMessageInfo("Connexion à la BD OK.");
        }
        catch (DbConnectionException ex)
        {
            gui.showMessageInfo("Echec de la connexion à la BD ! \n\n Infos techniques : \n" + ExceptionUtils.getStackTrace(ex));
            logger.error("failure to connect to the DB : DbConnectionException message = " + ex.getMessage() +
                         "\n Exception stack trace = \n " + ExceptionUtils.getStackTrace(ex) );
        }
    }





    @Override
    protected void updateModel_()
    { // Nothing
    }

}
