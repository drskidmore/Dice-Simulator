import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

public class main {
  // https://stackoverflow.com/questions/7522022/how-to-delete-stuff-printed-to-console-by-system-out-println
  public static void clearLine() {
    System.out.print(String.format("\033[%dA", 1)); // Move up
    System.out.print("\033[2K"); // Erase line content
  }

  public static int inputRequest(String message, int minimum) {
    Scanner inputScanner = new Scanner(System.in);
    System.out.printf("%s (must be >= %d) > ", message, minimum);

    try {
      int returnValue = Integer.parseInt(inputScanner.nextLine());

      if (returnValue < minimum) {
        clearLine();
        return -1;
      } else {
        return returnValue;
      }
    } catch (Exception e) {
      clearLine();
      return -1;
    }
  }

  public static int simulation(int diceCount, int diceSize) {
    int sigma = 0;

    for (int i = 0; i < diceCount; i++) {
      sigma += randomNumber(1, diceSize);
    }

    return sigma;
  }
  
  /*
    Returns a random int given a minimum and maximum
  */
  public static int randomNumber(int minimaInt, int maximaInt) {
    Random rng = new Random(); // Random number generator

    int generatedBase = rng.nextInt(maximaInt - minimaInt + 1);
    int generatedIntercept = generatedBase + minimaInt;

    return generatedIntercept;
  }
  
  public static void printData(int[] data, int diceSize, int diceCount, int maxLength) {
    int iter = 0;
    double maxLengthInt = Math.floor(Math.log10(maxLength));
    
    for (int i : data) {
      String distribution = "";
      String spaces = "";
      
      for (int j = 0; j < i; j++) {
        distribution += "*";
      }
      
      for (double k = Math.floor(Math.log10(diceCount+iter)); k < maxLengthInt; k++) {
        spaces += " ";
        break;
      }
      
      System.out.printf("%s%d | %s | %d\n", spaces, diceCount+iter, distribution, i);
      iter++;
    }
  }
  
  public static void printStats(int[] data, int diceSize, int diceCount) {
    int count = -1;
    
    double mean = -1;
    int median = -1;
    ArrayList<Integer> mode = new ArrayList<Integer>();
    
    int iter    = 0;
    
    // calculate average
    for (int i : data) {
      mean += (i*(diceCount+iter));
      count += i;
      iter++;
    }
    
    mean /= count;
    
    // calculate median
    double medianIndex = Math.floor(count / 2);
    count = 0;
    iter = 0;
    
    for (int j : data) {
      if (count + j > medianIndex && median == -1) {
        median = diceCount+iter;
      }
      
      count += j;
      iter++;
    }
    
    // calculate mode
    int modeMax = -1;
    iter = 0;
    
    for (int k : data) {
      if (k >= modeMax) {
        if (k > modeMax) {
          mode.removeAll(mode);
        }
        
        mode.add(diceCount+iter);
        modeMax = k;
      }
      
      iter++;
    }
    
    System.out.printf("Count   | %d\n", count);
    System.out.printf("Average | %f\n", mean);
    System.out.printf("Median  | %d\n", median);
    System.out.printf("Mode    | %s\n", Arrays.toString(mode.toArray()));
  }


  public static void main(String[] args) {
    System.out.print("Welcome to Dice Simulator DX!\nCopyright (C) 2020 Darren R. Skidmore. All rights reserved.\n\n");

    int diceCount   = -1;
    int diceSize    = -1;
    int simulations = -1;

    while (diceCount < 0) {
      diceCount = inputRequest("How many dice do you want to roll?", 2);
    }

    while (diceSize < 0) {
      diceSize = inputRequest("How many hedrons do the dice have?", 2);
    }

    while (simulations < 0) {
      simulations = inputRequest("How many simulations shall we do? ", 1);
    }
    
    int[] sampleData = new int[ (diceSize*diceCount)-(diceCount-1) ];

    for (int i = 0; i < simulations; i++) {
      sampleData[ simulation(diceCount, diceSize)-(diceCount) ] += 1;
    }
    
    System.out.print("\n=== RESULTS ===\n");
    
    printData(sampleData, diceSize, diceCount, diceSize*diceCount);
    
    System.out.print("\n=== STATISTICS ===\n");
    
    printStats(sampleData, diceSize, diceCount);
  }
}