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
        arg = arg.toLowerCase();

        if(arg.equals("-v")) {
            Globals.IsVerbose = true;
        } else if(arg.equals("-i")) {
            Globals.IsInteractive = true;
            Globals.IsVerbose = true;
        } else if(arg.startsWith("-e")) {
            int executionUnitNumber = Integer.parseInt(arg.replace("-e", ""));
            if(executionUnitNumber != 1
                    && executionUnitNumber != 2
                    && executionUnitNumber != 4) {
                System.out.println(
                        String.format(
                                "Invalid option: %s. Execution unit number can be only 1, 2 or 4. Using 2 (default)",
                                arg));
            } else {
                Globals.execution_units_num = executionUnitNumber;
                System.out.println(String.format("Using %d execution units", Globals.execution_units_num));
            }
        } else {
            System.out.println(String.format("Unknown parameter: %s", arg));
        }
    }
}
