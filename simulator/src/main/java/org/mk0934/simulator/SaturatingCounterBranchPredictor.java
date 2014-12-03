package org.mk0934.simulator;

import org.mk0934.simulator.instructions.BranchInstruction;

import java.util.HashMap;

/**
 * Created by Maciej Kumorek on 12/3/2014.
 */
public class SaturatingCounterBranchPredictor implements BranchPredictor {

    private enum State {
        StronglyNotTaken,
        WeaklyNotTaken,
        WeaklyTaken,
        StronglyTaken
    }

    private HashMap<Integer, State> branchState;

    private final Processor processor;

    public SaturatingCounterBranchPredictor(Processor processor) {
        this.processor = processor;
        this.branchState = new HashMap<>();
    }

    @Override
    public BranchPredictorResult predictBranch(BranchInstruction branchInstruction) {

        BranchPredictorResult result = new BranchPredictorResult();

        // Get current address
        int currentAddress = this.processor.getMemory().getInstructionAddress(branchInstruction);
        State state;

        // Check if we had a prediction
        if(this.branchState.containsKey(currentAddress)) {
            state = this.branchState.get(currentAddress);
        } else {
            state = State.StronglyTaken;
            // Add default prediction
            this.branchState.put(currentAddress, State.StronglyTaken);
        }


        // Address for not taken branch
        int alternateAddress = currentAddress + 0x4;
        boolean shouldTake = true;
        int addressToTake = branchInstruction.getAddressToJump();

        if(state == State.StronglyNotTaken || state == State.WeaklyNotTaken) {
            shouldTake = false;
            int t = addressToTake;
            addressToTake = alternateAddress;
            alternateAddress = t;
        }

        result.setAlternativeAddress(alternateAddress);
        result.setAddressPredicted(addressToTake);
        result.setShouldTake(shouldTake);

        return result;
    }

    @Override
    public void updatePredictor(BranchInstruction branchInstruction, boolean wasTaken) {

        // Get current address of the branch
        int instructionAddress = this.processor.getMemory().getInstructionAddress(branchInstruction);

        // Get state
        State state = this.branchState.get(instructionAddress);

        // Counter state machine update function
        switch(state)
        {
            case StronglyNotTaken:
                if(wasTaken) {
                    this.branchState.put(instructionAddress, State.WeaklyNotTaken);
                }
                break;
            case WeaklyNotTaken:
                if(wasTaken) {
                    this.branchState.put(instructionAddress, State.WeaklyTaken);
                } else {
                    this.branchState.put(instructionAddress, State.StronglyNotTaken);
                }
                break;
            case WeaklyTaken:
                if(wasTaken) {
                    this.branchState.put(instructionAddress, State.StronglyTaken);
                } else {
                    this.branchState.put(instructionAddress, State.WeaklyNotTaken);
                }
                break;
            case StronglyTaken:
                if(!wasTaken) {
                    this.branchState.put(instructionAddress, State.WeaklyTaken);
                }
            default:
                break;
        }
    }
}
