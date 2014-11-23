package org.mk0934.simulator;

/**
 * Simulator configuration options
 */
public class Configuration {

    public void parseConfigurationOption(String arg)
    {
        if(arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException("arg");
        }

        if(arg.equalsIgnoreCase("-v")) {
            Globals.IsVerbose = true;
        } else if(arg.equalsIgnoreCase("-i")) {
            Globals.IsInteractive = true;
            Globals.IsVerbose = true;
        } else {
            System.out.println(String.format("Unknown parameter: %s", arg));
        }
    }
}
