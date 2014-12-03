package org.mk0934.simulator;

import org.mk0934.simulator.instructions.BranchInstruction;

/**
 * Interface for branch predictor
 */
public interface BranchPredictor {

    BranchPredictorResult predictBranch(BranchInstruction branchInstruction);
    void updatePredictor(BranchInstruction branchInstruction, boolean wasTaken);
}
