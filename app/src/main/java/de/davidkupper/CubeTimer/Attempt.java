package de.davidkupper.CubeTimer;

import android.text.AutoText;

import java.io.Serializable;

public class Attempt implements Comparable<Attempt>, Serializable {
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

    public String getTimeString() {
        String s = timeToString(time);
        if(plus2)
            s += " +2";
        return s;
    }

    public long getRealTime() {
        if(dnf)
            throw new IllegalStateException("This attempt is DNF");
        if(plus2)
            return time + 2000;
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

    public String getScramble() {
        return scramble;
    }

    public static String timeToString(long time) {
        if (time == -1)
            return "--:--.---";
        else if (time < 0)
            throw new IllegalArgumentException("time has to be > 0, or -1 for '--:--.---'");
        int minutes = (int) (time / 60000);
        int seconds = (int) ((time / 1000) - (minutes * 60));
        int millis = (int) (time - (minutes * 60000) - (seconds * 1000));
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    @Override
    public String toString() {
        return "Time: " + time + "; Scramble: " + scramble;
    }

    @Override
    public int compareTo(Attempt otherAttempt) {
        if(this.isDnf()) {
            if(otherAttempt.isDnf())
                return 0;
            else
                return -1;
        }
        else if(otherAttempt.isDnf())
            return 1;
        else {
            if (this.getRealTime() < otherAttempt.getRealTime())
                return -1;
            else if (this.getRealTime() == otherAttempt.getRealTime())
                return 0;
            else
                return 1;
        }
    }
}
