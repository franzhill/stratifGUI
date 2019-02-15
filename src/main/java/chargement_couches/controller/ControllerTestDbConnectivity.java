package main.java.chargement_couches.controller;

import main.java.Gui;
import main.java.chargement_couches.model.ModelCharg;
import main.java.common._excp.UserLevelException;
import main.java.common.controller.AController;
import main.java.common.tool.DbConnector;
import main.java.common._excp.DbConnectionException;


public class ControllerTestDbConnectivity extends AController
{
    public ControllerTestDbConnectivity(Gui gui, ModelCharg model)
    {   super(gui, model);
    }

    @Override
    protected void doo_() throws UserLevelException
    {   gui.loggerGui.info("Test de connectivité ...");

        DbConnector dbc = new DbConnector(model.modelDb);
        try
        {
            dbc.getConn();
            gui.showMessageInfo("Connexion à la BD OK.");
        }
        catch (DbConnectionException ex)
        {   throw new UserLevelException("Echec de la connexion à la BD !", ex);
            //gui.showMessageInfo("Echec de la connexion à la BD ! \n\n Infos techniques : \n" + ExceptionUtils.getStackTrace(ex));
            //logger.error("failure to connect to the DB : DbConnectionException message = " + ex.getMessage() +
            //             "\n Exception stack trace = \n " + ExceptionUtils.getStackTrace(ex) );
        }
    }





    @Override
    protected void updateModel_()
    { // Nothing
    }

}
