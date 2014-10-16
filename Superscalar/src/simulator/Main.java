package simulator;

import java.io.File;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Main {

    /**
     * Entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {

        if(args.length < 1) {
            System.out.println("Please specify input program.");
        }

        String inputProgramName = args[0];

        // Create the program
        Program program = new Program(inputProgramName);

        // Initialize the simulator
        Simuator simulator = new Simuator();

        // Run the simulation
        simulator.Run(program);
    }
}
