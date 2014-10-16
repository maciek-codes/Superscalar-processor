package simulator;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class Processor {

    /**
     * Register file in the processor
     */
    private RegisterFile regFile;

    /**
     * Creates new processor
     */
    public Processor() {
        this.regFile = new RegisterFile();
    }
}
