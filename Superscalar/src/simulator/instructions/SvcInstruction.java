package simulator.instructions;

import simulator.Processor;
import simulator.Register;
import simulator.RegisterFile;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public class SvcInstruction extends DecodedInstruction {

    public SvcInstruction(Operand op) {
        super(op);
    }

    @Override
    public void execute(Processor processor) {

        processor.setRunning(false);

        // Dump registers
        final RegisterFile registerFile = processor.getRegisterFile();

        System.out.println("Register file dump: ");

        for(int i = 0; i < registerFile.getCount(); i++) {
            Register register = registerFile.getRegister(i);
            int value = register.getValue();
            System.out.print("R" + i + ": 0x" + Integer.toHexString(value).toUpperCase());

            if((i + 1) % 4 == 0) {
                System.out.print("\n");
            } else {
                System.out.print("\t\t");
            }
        }
    }

    @Override
    public void writeBack(Processor processor) {
        return;
    }
}
