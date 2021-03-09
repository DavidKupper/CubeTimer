package de.davidkupper.CubeTimer;

import android.text.AutoText;

public class Attempt implements Comparable<Attempt> {
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

    public long getRealTime() {
        if(dnf)
            return -1;
        if(plus2)
            return time + 2;
        return time;
    }

    public boolean isDnf() {
        return dnf;
    }

    public void toggleDnf() {
        if(!plus2)
            dnf = !dnf;
    }

    public boolean isPlus2() {
        return plus2;
    }

    public void togglePlus2() {
        if(!dnf)
            plus2 = !plus2;
    }

    @Override
    public int compareTo(Attempt otherAttempt) {
        if(this.time < otherAttempt.getRealTime())
            return -1;
        else if(this.time == otherAttempt.getRealTime())
            return 0;
        else
            return 1;
    }
}
