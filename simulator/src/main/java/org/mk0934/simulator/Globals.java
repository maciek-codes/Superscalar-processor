package org.mk0934.simulator;

/**
 * Created by Maciej Kumorek on 11/22/2014.
 */
public class Globals {

    /**
     * Should simulator print all messages
     */
    public static boolean IsVerbose = false;

    /**
     * Should simulation be run in interactive mode
     */
    public static boolean IsInteractive = false;

    public static int execution_units_num = 2;

    /**
     * Should use dynamic branch predictor?
     */
    public static boolean UseDynamicBranchPredictor = true;

    /**
     * Should use static branch predictor?
     */
    public static boolean UseStaticBranchPredictor;
}
