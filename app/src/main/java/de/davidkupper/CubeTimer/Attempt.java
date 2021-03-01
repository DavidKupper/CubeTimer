package de.davidkupper.CubeTimer;

import android.text.AutoText;

public class Attempt {
    private long time;
    private boolean dnf = false;
    private boolean plus2 = false;
    private String scramble;

    public Attempt(long time, String scramble) {
        this.time = time;
        this.scramble = scramble;
    }


    public long getTime() {
        return time;
    }

    public boolean isDnf() {
        return dnf;
    }

    public void toggleDnf() {
        dnf = !dnf;
    }

    public boolean isPlus2() {
        return plus2;
    }

    public void togglePlus2() {
        plus2 = !plus2;
    }

}
