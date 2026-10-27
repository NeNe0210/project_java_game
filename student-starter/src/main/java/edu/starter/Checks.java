package edu.starter;
/** Optional assertions for verifying state in early workshops. */
public final class Checks {
 private Checks(){}
 public static void that(boolean condition,String message){if(!condition)throw new AssertionError(message);}
 public static int within(int value,int min,int max){return Math.max(min,Math.min(max,value));}
}
