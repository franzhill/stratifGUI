package main.chargement_couches;

import main.Gui;
import main.common.AController;
import main.common.DbConnector;
import main.common.ModelDb;
import main.ex.DbConnectionException;

import java.awt.event.ActionEvent;


public class ControllerTestDbConnectivity extends AController
{
    public ControllerTestDbConnectivity(Gui gui, ModelLoad model)
    {   super(gui, model);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {   gui.loggerGui.info("Test de connectivité ...");

        updateModelDb();

        DbConnector dbc = new DbConnector(model.modelDb);
        try
        {
            dbc.getConn();
            gui.showMessageInfo("Connexion à la BD OK.");
        }
        catch (DbConnectionException excp)
        {
            gui.showMessageInfo("Echec de la connexion à la BD !");
        }
    }




}
