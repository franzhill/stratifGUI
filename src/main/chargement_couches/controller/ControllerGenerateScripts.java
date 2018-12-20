package main.chargement_couches.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import main.Gui;
import main.chargement_couches.model.FileDep;
import main.chargement_couches.model.ModelLoad;
import main.common.controller.AController;
import main.ex.ExecutionException;
import org.apache.commons.io.FileUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ControllerGenerateScripts extends AController
{
    public ControllerGenerateScripts(Gui gui, ModelLoad model)
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

      // Do
      try
      { do_();
        gui.loggerGui.info("Tous les scripts ont été générés.");
      }
      catch (ExecutionException e1)
      { gui.showMessageError("Erreur lors du chargement des couches spécifiées. Vérifier les paramètres fournis, et réessayer, ou consulter les logs.", e1);
        return;
      }
    }


    protected void preDoChecks()
    {

    }


    @Override
    protected void do_() throws ExecutionException
    {
      loadFileDeps();
    }


    private void emptyWorkDir() throws ExecutionException
    {
      if (gui.chbEmptyWorkDirFirst.isSelected())
      { try
        { FileUtils.cleanDirectory(new File(model.getTempFolderPath()));
        }
        catch (IOException e)
        {  throw new ExecutionException(String.format("Impossible de vider le répertoire des scripts (%s). Vérifier que ce répertoire existe.", model.getTempFolderPath()), e);
        }
      }
    }


    private void loadFileDeps() throws ExecutionException
    {
      emptyWorkDir();

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
        throw new ExecutionException(String.format("Impossible de charger le template pour fabriquer le script bat de chargement, {%s}.", template_path));
      }

      // Provide templating engine with data for interpolation
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("model", model);

      // Process each file
      for (FileDep fd : model.depFiles) // TODo rename FileDep in DepFile or depFiles in fileDeps
      {
        logger.debug("Processing file : " + fd.toString());
        if (fd.departement.isEmpty())
        { throw new ExecutionException(String.format("Département manquant pour le fichier {%s}.", fd.file.getAbsolutePath()));
        }

        // Provide templating engine with remaining data for interpolation
        data.put("fd"   , fd);

        // Interpolate and output file
        Writer filewriter;
        String filepath= String.format(model.getTempFolderPath() + File.separator + "chargement_couche_%s_%s_%s.bat", model.couche.type, fd.getName(), fd.departement); // TODO put in conf file
        File   f = new File(filepath);
        logger.debug("filepath=" + filepath);
        try
        { filewriter = new FileWriter(f);
        }
        catch (IOException e)
        { throw new ExecutionException(String.format("Erreur d'accès pour écriture au fichier bat {%s}.", filepath), e);
        }
        try
        { template.process(data, filewriter);
          filewriter.flush();
          filewriter.close();
        }
        catch (Exception e)
        { throw new ExecutionException(String.format("Erreur lors de la fabrication du script bat de chargement à partir du template, pour type de couche = {%s}, département = {%s}.", model.couche.type, fd.departement), e);
        }
        gui.loggerGui.info("Génération du script : " + f.getName() + " : DONE. ");
      }

    }

}
