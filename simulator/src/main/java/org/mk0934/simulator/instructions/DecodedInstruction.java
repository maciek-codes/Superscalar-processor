package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.Register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public abstract class DecodedInstruction {

    protected Operand op;

    // Original instruction
    private EncodedInstruction encodedInstruction;

    public DecodedInstruction(Operand op, EncodedInstruction encodedInstruction) {
        this.op = op;
        this.encodedInstruction = encodedInstruction;
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

    public String getEncodedInstruction() {
        return encodedInstruction.getEncodedInstruction();
    }

    public abstract Integer getDestinationRegisterNumber();

    public abstract Integer getSecondSourceRegisterNumber();

    public abstract Integer getFirstSourceRegisterNumber();

    /**
     * Get latency of instructions
     * @return number of cycles latency
     */
    public abstract int getLatency();
}
