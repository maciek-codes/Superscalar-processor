package org.mk0934.simulator;

import java.util.ArrayList;

/**
 * Created by Maciej Kumorek on 9/30/2014.
 */
public class RegisterFile {

    private ArrayList<Register> registers;

    public RegisterFile() {
        this.registers = new ArrayList<Register>();
        // Initialize 16 register
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
        this.registers.add(new Register());
    }

    public Register getRegister(int index) {
        return registers.get(index);
    }

    public int getCount() {
        return this.registers.size();
    }

    public void commit() {
        for(Register register : registers) {
            register.setDirty(false);
        }
    }
}
