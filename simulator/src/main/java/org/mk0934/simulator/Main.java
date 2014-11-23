package org.mk0934.simulator;

import java.io.IOException;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Main {

    /**
     * Entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        if(args.length < 1) {
            System.out.println("Please specify input program as the parameter.");
            return;
        }

        String inputProgramName = args[0];

        for(int i = 1; i < args.length; i++) {
            configuration.parseConfigurationOption(args[i]);
        }

        // Loaded program
        Program program;

        try {
            // Create the program
            program = new Program(inputProgramName);
        } catch (IOException ex) {
            // Print out the error
            System.out.println("Error reading the input program");
            System.out.print(ex.getMessage());
            return;
        }
        // Initialize the simulator
        Simulator simulator = new Simulator();

        // Run the simulation
        simulator.run(program);
    }
}
