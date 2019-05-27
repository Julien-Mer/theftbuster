package com.theftbuster.theftbuster.Helpers.Memory.Data;

/**
 * Created by Julien M on 23/01/2019.
 */
public class States {

    private String name;
    private int value;

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public States(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
