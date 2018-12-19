package main.chargement_couches.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelLoad;
import main.common.BatFolderExecutor;
import main.common.controller.AController;
import main.ex.DirException;
import main.ex.LoadException;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ControllerLoadFiles extends AController
{
    public ControllerLoadFiles(Gui gui, ModelLoad model)
    {   super(gui, model);
    }

    /**
     * TODO Refactor refactor refactor
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      gui.loggerGui.info("Button buttLoadFiles was pressed");


      // Then update parts of the model
      //   - where user might have modified input
      //   - that directly impact the present action
      updateModelDb();
      model.setPostgresqlBinPath(gui.txtPostgresqlBinDir.getText());
      model.setTempFolderPath   (gui.txtTempDir.getText());

      // Save configs (they might have been edited by user)
      // TODO for the time being we won't be saving the config
      //gui.saveUserConfigDisplay();

      // Perform some checks : have all details been provided?
      if (model.isIncomplete())
      {
        gui.showMessageError("Il manque des paramètres. Vérifier que tous les éléments nécéssaires ont été indiqués.");
        return;
      }

      // Do the loading of the layers into DB
      try
      { logger.debug("model.couche.type= " + model.couche.type);
        loadFileDeps();
        executeBats();
      }
      catch (LoadException e1)
      {
        gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
        return;
      }
    }


    private void loadFileDeps() throws LoadException
    {
      // Use Freemarker to generate the bat file that will load the "couche" in DB
      Configuration cfg = new Configuration();   // Freemarker configuration object

      //Load template
      Template template;
      String template_path  = String.format("resources/chargement_couche.%s.ftl.bat", model.couche.type.toLowerCase()) ; // TODO  put in conf file ? / function

      try
      { template = cfg.getTemplate(template_path);
      }
      catch (IOException e)
      { e.printStackTrace(); // TODO handle
        throw new LoadException(String.format("Impossible de charger le template pour fabriquer le script bat de chargement, {%s}.", template_path));
      }

      // Provide templating engine with data for interpolation
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("model", model);

      // Process each file
      for (FileDep fd : model.depFiles) // TODo rename FileDep in DepFile or depFiles in fileDeps
      {
        logger.debug("Processing file : " + fd.toString());
        if (fd.departement.isEmpty())
        { throw new LoadException(String.format("Département manquant pour le fichier {%s}.", fd.file.getAbsolutePath()));
        }

        // Provide templating engine with remaining data for interpolation
        data.put("fd"   , fd);

        // Interpolate and output file
        Writer file;
        String filepath= String.format(model.getTempFolderPath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement); // TODO put in conf file
        logger.debug("filepath=" + filepath);
        try
        { file = new FileWriter(new File(filepath));
        }
        catch (IOException e)
        { throw new LoadException(String.format("Erreur d'accès pour écriture au fichier bat {%s}.", filepath), e);
        }
        try
        { template.process(data, file);
          file.flush();
          file.close();
        }
        catch (Exception e)
        { throw new LoadException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = {%s}, département = {%s}.", model.couche.type, fd.departement), e);
        }
      }
    }


    protected void executeBats()
    {
      BatFolderExecutor bfe = new BatFolderExecutor(model.getTempFolderPath());
      try {
        bfe.execute();
      } catch (DirException e) {
        e.printStackTrace(); // TODO handle
      }
    }

}
