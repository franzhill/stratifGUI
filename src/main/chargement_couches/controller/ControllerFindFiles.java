package main.chargement_couches.controller;

import main.Gui;
import main.chargement_couches.model.ModelLoad;
import main.common.controller.AController;
import main.chargement_couches.tool.FileFinder;
import main.common._excp.DepExtractionException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.event.ActionEvent;
import java.io.File;


/**
 * Find all files (shapefiles etc.) corresponding to GUI input settings
 */
public class ControllerFindFiles extends AController
{

    public ControllerFindFiles(Gui gui, ModelLoad model)
    {   super(gui, model);
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        gui.loggerGui.info("Button findFiles was pressed");

        // First, reinitialise the list of found files :
        // - in the model
        model.depFiles.clear();
        // - in the GUI
        gui.displayFoundFiles();

        // Then update parts of the model
        //   - where user might have modified input
        //   - that directly impact the present action
        updateModelCouche();

        if (model.parents.isEmpty())
        {   String msg = "Aucun fichier ou répertoire sélectionné !";
            logger.error(msg);
            gui.showMessageError(msg);
            return;
        }
        else
        {
          FileFinder finder = new FileFinder(model.parents, model.couche.fileExt, model.couche.detectFiles, model.couche.dep, gui.rdoDepDetect.isSelected()); // TODO

          try
          {
            model.depFiles = finder.find();
            if (model.depFiles.isEmpty())
            {
              gui.showMessageError("Aucun fichier trouvé avec les critères actuels.");
            }
          }
          catch (DepExtractionException excp)
          {
            gui.showMessageError("Impossible de détecter un département : " + excp.getMessage());
            return;
          }
        }

        // Update (pseudo) View
        gui.displayFoundFiles();

        logger.debug("model.couche.type= " + model.couche.type);

    }






    /**
     * Handle a zip file
     * Note
     * Unzipping zip depFiles : e.g. BDT_2-2_SHP_LAMB93_D018-ED181.7z takes ~ 10s to unzip
     * @deprecated
     */
   /* private void loadZipFile(File f)
    {
        logger.debug(String.format("Loading file {%s}...", f.toString() ));

        try
        {   ZipFile zipFile = new ZipFile(f);
                    zipFile.extractAll(gui.getUnzipDir());
        }
        catch (ZipException e)
        {
            String msg = String.format("Impossible de dézipper {%s} dans {%s}", f.getAbsolutePath(), gui.getUnzipDir());
            logger.error(msg + "\n Stack Trace = \n" + ExceptionUtils.getStackTrace(e));
            gui.showMessageError(msg);
        }
    }*/

}
