package org.mk0934.simulator.instructions;

import org.mk0934.simulator.Processor;
import org.mk0934.simulator.WritebackEvent;

import java.lang.reflect.Array;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Maciej Kumorek on 10/24/2014.
 */
public abstract class DecodedInstruction {

    protected Operand op;

    // Original instruction
    private EncodedInstruction encodedInstruction;

    private Vector<WritebackEvent> writebackEventListeners;

    public DecodedInstruction(Operand op, EncodedInstruction encodedInstruction) {
        this.op = op;
        this.encodedInstruction = encodedInstruction;
    }

    public abstract void execute(Processor processor);

    public void writeBack(Processor processor) {

        this.doWriteBack(processor);
        this.onWriteBackExecuted();
    }

    private void onWriteBackExecuted() {

        // Raise the events
        if(this.writebackEventListeners != null) {
            WritebackEvent[] listenersToNotify =
                    this.writebackEventListeners.toArray(new WritebackEvent[0]);

            for (WritebackEvent eventListener : listenersToNotify) {
                eventListener.onWriteBack(this);
            }
        }
    }

    protected abstract void doWriteBack(Processor processor);

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

    public Instruction getEncodedInstruction() {
        return encodedInstruction;
    }

    public String getEncodedInstructionString() {
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

    public void addWriteBackListener(WritebackEvent eventListener, BranchInstruction branchInstruction) {

        if(this.writebackEventListeners == null) {
            this.writebackEventListeners = new Vector<>();
        }

        if(!this.writebackEventListeners.contains(eventListener)) {
            this.writebackEventListeners.add(eventListener);
        }
    }

    public void removeWriteBackListener(WritebackEvent eventListener) {

        if(this.writebackEventListeners != null
                && this.writebackEventListeners.contains(eventListener)) {

            this.writebackEventListeners.remove(eventListener);
        }
    }
}
