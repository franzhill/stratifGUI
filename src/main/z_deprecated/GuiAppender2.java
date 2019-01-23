package main.z_deprecated;
import main.Gui;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;


/**
 * @deprecated
 *
 */
public class GuiAppender2 extends AbstractAppender
{
    protected Gui gui;

    protected GuiAppender2(String name, Gui gui, Filter filter)
    {   super(name, filter, null);
        System.out.println("GuiAppender");
        this.gui = gui;
    }


    public static GuiAppender2 createAppender(String name, Gui gui, Filter filter)
    {   System.out.println("createAppender()");
        if (name == null) {
            LOGGER.error("No name provided for GuiAppender");
            return null;
        }
        return new GuiAppender2(name, gui, filter);
    }

    @Override
    public void append(LogEvent event)
    {   gui.logInGui(event.toString());
    }
}
