package org.mk0934.simulator;

/**
 * Utility functions
 * @author Maciej Kumorek
 */
public class Utilities {

    /**
     * Print log message if in interactive mode
     */
    public static void log(String text) {
        if(!Globals.IsVerbose) {
            return;
        }

        System.out.println(text);
    }

    /**
     * Print log message if in interactive mode and append a tag
     * @param tag Tag to be appended
     * @param text message
     */
    public static void log(String tag, String text) {
        Utilities.log(tag + ": " + text);
    }
}
