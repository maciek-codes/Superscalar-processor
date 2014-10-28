package simulator;

/**
 * Created by Maciej Kumorek on 10/28/2014.
 */
public class StatusRegister extends Register {

    public enum Status {
        EQ(0),
        LT(1),
        LE(2),
        GT(3),
        GE(4);

        private final int value;

        private Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public Status getStatus() {
        return Status.values()[this.getValue()];
    }

    public void setStatus(Status status) {
        this.setValue(status.getValue());
    }
}
