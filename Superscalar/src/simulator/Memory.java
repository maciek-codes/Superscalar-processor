package simulator;

import simulator.instructions.EncodedInstruction;
import simulator.instructions.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class Memory {

    /**
     * Values stored in memory, four bytes long
     */
    private ArrayList<Object> values;

    public Memory() {
        this.values = new ArrayList<Object>();
    }

    private int addressToIndex(int address) {
        return address / 4;
    }
    public <T> void saveToMemory(T value, int address) {

        int index = addressToIndex(address);

        if(index >= values.size()) {
            values.add(index, value);
        } else {
            values.set(index, value);
        }
    }

    public Object getFromMemory(int address) {

        int index = addressToIndex(address);

        if(values.size() <= index) {
            return 0x0;
        }

        return values.get(index);
    }

    public void LoadProgram(Program program) {

        int address = 0x0;

        // Store labels and addresses
        HashMap<String, Integer> labelsAddressMap = new HashMap<String, Integer>();


        // Load instructions
        for(Instruction instruction : program.getInstructionList()) {

            // Check if it isn't just memory
            Integer value = tryParse(instruction.getEncodedInstruction());

            if(value == null) {
                this.saveToMemory(instruction, address);
            }
            else {
                this.saveToMemory(value, address);
            }

            // Add label if any
            if(instruction.getLabel() != null) {
                labelsAddressMap.put(instruction.getLabel(), address);
            }

            address += 0x4;
        }

        // All labels
        Set<String> labels = labelsAddressMap.keySet();

        // Resolve labels
        for(int i = 0; i < address; i += 0x4) {
            Object memoryValue = this.getFromMemory(i);
            if(memoryValue instanceof EncodedInstruction) {

                EncodedInstruction instruction = (EncodedInstruction) memoryValue;

                for (String label : labels) {

                    // Ignore empty string
                    if(label.equalsIgnoreCase("")) {
                        continue;
                    }

                    if (instruction.getEncodedInstruction().contains(label)) {
                        instruction.replaceLabelWithAddress(label, labelsAddressMap.get(label));
                    }
                }
            }
        }
    }

    public static Integer tryParse(String text) {

        try {
            if(text.startsWith("0x")) {
                text = text.replace("0x", "\0").trim();
                return Integer.parseInt(text, 16);
            } else {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getMaxAddress() {
        return this.values.size() * 4;
    }
}
