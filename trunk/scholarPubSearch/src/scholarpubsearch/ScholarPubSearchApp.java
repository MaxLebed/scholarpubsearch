/*
 * ScholarPubSearchApp.java
 */

package scholarpubsearch;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ScholarPubSearchApp extends SingleFrameApplication {

    //private UserInputAgent myAgent;

   /**
     * At startup create and show the main frame of the application.
     */ 
    @Override protected void startup() {
        
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ScholarPubSearchApp
     */
    public static ScholarPubSearchApp getApplication() {
        return Application.getInstance(ScholarPubSearchApp.class);
    }

    /**
     * Main method launching the application.
     */
    public void launchApplication() {
        //myAgent = a;
        launch(ScholarPubSearchApp.class, new String[0]);
    }
}
