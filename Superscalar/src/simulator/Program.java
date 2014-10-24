package simulator;

import simulator.instructions.DecodedInstruction;
import simulator.instructions.EncodedInstruction;
import simulator.instructions.Instruction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej Kumorek on 10/16/2014.
 */
public class Program {

    private List<Instruction> instructions = new ArrayList<Instruction>();

    /**
     * Load program instructions from a file into memory
     * @param inputProgramName Input file name
     */
    public Program(String inputProgramName) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader(new File(inputProgramName)));

        String line;

        while((line = bf.readLine()) != null) {

            line = line.trim();

            // Ignore comment lines
            if(line.startsWith(";")) {
                continue;
            }

            // If label line, parse next
            if(line.endsWith(":")) {

                String label = line;
                line = bf.readLine();

                // If there is no next, fail
                if(line == null) {
                    throw new RuntimeException("No instruction after label");
                }

                instructions.add(new EncodedInstruction(line, label));
            }
            else {
                // Just get instruction
                instructions.add(new EncodedInstruction(line));
            }
        }

        // Replace labels in instructions with addresses
    }

    public Iterable<? extends Instruction> getInstructionList() {
        return this.instructions;
    }
}
