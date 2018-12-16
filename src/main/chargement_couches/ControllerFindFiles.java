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
   * @deprecated
   * @param e
   */
  public void DEPactionPerformed(ActionEvent e)
    {   gui.loggerGui.info("Button buttLoadFiles was pressed");

        if (model.parents.isEmpty())
        {   String msg = String.format("Aucun fichier ou répertoire sélectionné !");
            logger.error(msg);
            gui.showMessageError(msg);
            return;
        }

        for(File f : model.parents)
        {   logger.debug("Processing file : " + f.getAbsolutePath());

            // If file is a shapefile
            if (MyFileUtils.isShapefile(f))
            {
                logger.debug("File identified as SHAPEFILE");
                gui.showMessageError("Le chargement d'un fichier Shapefile simple n'est pas encore supporté.");
            }
            else if (f.isDirectory() && ! gui.txtDetectDep.getText().isEmpty())
            {   // Extract the "departement"
                String pattern = gui.txtDetectDep.getText();   // TODO access through multiple layers, not very nice I admit
                String searchIn= f.getName();

                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(searchIn);

                // if we don't find a match, fail
                if (! m.find())
                {   gui.showMessageError(String.format("Impossible de trouver un département dans le le fichier {%s} en utilsant le pattern {%s}", searchIn, pattern ));
                    return;
                }
                // else : found a match for departement
                else
                {   String dep = m.group(1);  // we're only looking for one group
                    gui.loggerGui.info(String.format("Trouvé le département : {%s} dans le fichier {%s}", dep, searchIn));

                    // Find all depFiles corresponding to the file detection patterns given by user
                    if (gui.txtDetectFiles.getText().isEmpty())
                    {   // Select all files at any level under the parent folder
                        model.depFiles = FileDep.addDep(dep, (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
                    }
                    else
                    {   List<String> patterns = Arrays.asList(StringUtils.stripAll(gui.txtDetectFiles.getText().split(",")));

                        // List all depFiles at any level in folder
                        List<File> files = (List<File>) FileUtils.listFiles(f, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                        for (File ff : files)
                        {   for (String patt : patterns)
                            {   // TODO only search on the part of the file name that is under the "root" dir !
                                p = Pattern.compile(patt);
                                m = p.matcher(FilenameUtils.separatorsToUnix(ff.getAbsolutePath()));

                                logger.debug(String.format("Looking for pattern {%s} in file {%s}", patt, ff));
                                if (m.find())  // Retain file
                                {   logger.debug(String.format("Retaining file : {%s}", ff));
                                    model.depFiles.add(new FileDep(dep, ff));
                                }
                            }
                        }
                    }


                }
            }
        }

        // Update (pseudo) View
        gui.displayFoundFiles();



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
