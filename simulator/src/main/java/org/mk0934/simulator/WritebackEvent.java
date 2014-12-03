package org.mk0934.simulator;

import org.mk0934.simulator.instructions.DecodedInstruction;

/**
 * On write-back event
 *
 * @author Maciej Kumorek
 */
public interface WritebackEvent {

    void onWriteBack(DecodedInstruction writtenInstruction);
}
