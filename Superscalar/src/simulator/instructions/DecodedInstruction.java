package simulator.instructions;

import simulator.Processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public abstract class DecodedInstruction {

    protected Operand op;

    public DecodedInstruction(Operand op) {
        this.op = op;
    }

    public abstract void execute(Processor processor);

    public abstract void writeBack(Processor processor);

    public Operand getOperand() {
        return this.op;
    }

    /**
     * Remove r from register name and convert to int
     * @return
     */
    protected int parseRegisterNumber(String registerName) {
        final Pattern numPattern = Pattern.compile("\\d\\d?");

        if(registerName == null) {
            throw new NullPointerException("registerName");
        }

        Matcher matcher = numPattern.matcher(registerName);

        if(!matcher.find()) {
            throw new RuntimeException("Invalid register name: " + registerName);
        }

        return Integer.parseInt(matcher.group(0));
    }
}
