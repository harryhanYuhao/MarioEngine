package util;

public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime(){  // returns time in seconds
        return (System.nanoTime()-timeStarted)/1000000000.0f;
    }
}
