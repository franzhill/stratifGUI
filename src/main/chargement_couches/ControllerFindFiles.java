package main.chargement_couches;

import main.Gui;
import main.common.AController;
import main.common.FileFinder;
import main.common.MyFileUtils;
import main.ex.DepExtractionException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Finds all files (shapefiles etc.) corresponding to GUI input settings
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
        {   String msg = String.format("Aucun fichier ou répertoire sélectionné !");
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
              gui.showMessageError(String.format("Aucun fichier trouvé avec les critères actuels."));
            }
          }
          catch (DepExtractionException excp)
          {
            gui.showMessageError(String.format("Impossible de détecter un département : " + excp.getMessage()));
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
    private void loadZipFile(File f)
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
    }

}
