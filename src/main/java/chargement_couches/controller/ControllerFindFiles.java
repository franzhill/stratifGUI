package main.java.chargement_couches.controller;

import main.java.Gui;
import main.java.chargement_couches.model.ModelCharg;
import main.java.common._excp.ExecutionException;
import main.java.common._excp.UserLevelException;
import main.java.chargement_couches.tool.FileFinder;
import main.java.common._excp.DepExtractionException;

import java.awt.event.ActionEvent;


/**
 * Find all files (shapefiles etc.) corresponding to GUI input settings
 */
public class ControllerFindFiles extends AControllerCharg
{
  public ControllerFindFiles(Gui gui, ModelCharg model)
  { super(gui, model);
  }


  @Override
  protected void updateModel__()
  { // Reinitialise the list of found files :
    // - in the model
    model.depFiles.clear();
  }


  @Override
  protected void updateGui(ActionEvent e)
  { // Reinitialise the list of found files :
    // - in the GUI
    gui.displayFoundFiles();  // TODO move somewhere else
  }


  @Override
  protected void preDoChecks() throws Exception
  {
    if (model.parents.isEmpty())
    { throw new ExecutionException("Aucun fichier ou répertoire sélectionné !");
    }
    if (model.couche == null)
    { throw new ExecutionException("Aucune couche sélectionnée !");
    }
  }


  @Override
  protected void doo_() throws UserLevelException
  {
    gui.loggerGui.info ("Recherche des fichiers correspondant aux critères...");
    //# In the case where an execption is thrown, re-anabling the button is not done
    //# => we'll do away with enablig/disabling the button
    //# gui.buttSelectFiles.setEnabled(false);  // TODO maybe reduce coupling to button
    FileFinder finder = new FileFinder(model.parents,
                                       model.couche.fileExt,
                                       model.couche.detectFiles,
                                       model.couche.dep,
                                       gui.rdoDepDetect.isSelected()
                                      ); // TODO
    try
    { model.depFiles = finder.find();
      if (model.depFiles.isEmpty())
      { throw new UserLevelException("Aucun fichier trouvé avec les critères actuels.");
        //gui.showMessageError("Aucun fichier trouvé avec les critères actuels.");
      }
    }
    catch (DepExtractionException excp)
    { throw new UserLevelException("Impossible de détecter un département : " + excp.getMessage());
      //gui.showMessageError("Impossible de détecter un département : " + excp.getMessage());
      //return;
    }

    // Update "View"
    gui.displayFoundFiles();

    logger.debug("model.couche.type= " + model.couche.type);
  }


  @Override
  protected void postDo()
  {
    //# See above
    //#gui.buttSelectFiles.setEnabled(true);

    // In fact, as long as the action is processed in the Event Dispatch Thread (and not deferred to a SwingWorker)
    // all actions on the GUI will be processed after the end of the action
    // So what we're trying to do here is pointless...
  }





  /**
   * Handle a zip file
   * Note
   * Unzipping zip depFiles : e.g. BDT_2-2_SHP_LAMB93_D018-ED181.7z takes ~ 10s to unzip
   * @deprecated
   */
   /* private void loadZipFile(File f)
    {
        logger.debug(String.format("Loading file [%s]...", f.toString() ));

        try
        {   ZipFile zipFile = new ZipFile(f);
                    zipFile.extractAll(gui.getUnzipDir());
        }
        catch (ZipException e)
        {
            String msg = String.format("Impossible de dézipper [%s] dans [%s]", f.getAbsolutePath(), gui.getUnzipDir());
            logger.error(msg + "\n Stack Trace = \n" + ExceptionUtils.getStackTrace(e));
            gui.showMessageError(msg);
        }
    }*/

}
