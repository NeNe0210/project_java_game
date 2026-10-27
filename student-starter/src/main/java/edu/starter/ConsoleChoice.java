package edu.starter;
import java.util.Scanner;
/** Optional small UI utility; never put battle rules here. */
public final class ConsoleChoice {
 private ConsoleChoice(){}
 public static int read(Scanner in, int min, int max){
  if(!in.hasNextLine())return -1;
  try {int v=Integer.parseInt(in.nextLine().trim());return v>=min&&v<=max?v:-1;}
  catch(NumberFormatException ex){return -1;}
 }
}
