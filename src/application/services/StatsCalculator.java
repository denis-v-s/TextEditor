package application.services;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart.Data;

public class StatsCalculator extends Task<StatsCalculator> {
  private Path dir = Paths.get("").toAbsolutePath().resolve("outputData");
  private ObservableList<Data<String, Double>> threeLoopTiming;
  private ObservableList<Data<String, Double>> singleLoopTiming;
    
  public ObservableList<Data<String, Double>> getSingleLoopTiming() {
    return this.singleLoopTiming;
  }
  
  public ObservableList<Data<String, Double>> getThreeLoopTiming() {
    return this.threeLoopTiming;
  }
  
  private void setSingleLoopTimingDataPoints(LinkedHashMap<String, Double> loopTiming) {
    singleLoopTiming = FXCollections.observableList(new ArrayList<>());
    
    for (String key : loopTiming.keySet()) {
      singleLoopTiming.add(new Data<>(key, loopTiming.get(key)));
    }
  }
  private void setThreeLoopTimingDataPoints(LinkedHashMap<String, Double> loopTiming) {
    threeLoopTiming = FXCollections.observableList(new ArrayList<>());
    
    for (String key : loopTiming.keySet()) {
      threeLoopTiming.add(new Data<>(key, loopTiming.get(key)));
    }
    System.out.println();
  }
  
  private void threeLoopAlgorithm() {        
    LinkedHashMap<String, Double> loopTiming = new LinkedHashMap<>();
    
    for (int i = 1; i <= 20; i++) {
      String fileName = "file (" + i + ").txt";
      String fullFileName = dir.resolve(fileName).toString();
      int words = 0;
      int sentences = 0;
      int syllables = 0;
      double fleschScore = 0;
      StringBuilder content = new StringBuilder();
      
      try {
        Files.readAllLines(Paths.get(fullFileName)).forEach(line -> content.append(line + "\r\n"));
        String text = content.toString();
        
        double start = System.nanoTime();
        words = Utils.getWordCount(text);
        sentences = Utils.getSentenceCount(text);
        syllables = Utils.getSyllableCount(text);
        Utils.getFleschScore();
        double end = System.nanoTime();
        
        double runtime = getRunTimeInMilliSeconds(start, end);
        loopTiming.put(fileName, runtime);
        super.updateProgress(i, 20);
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    this.setThreeLoopTimingDataPoints(loopTiming);
  }
  
  private void singleLoopAlgorithm() {
    LinkedHashMap<String, Double> loopTiming = new LinkedHashMap<>();
    
    String fileName;
    String fullFileName;
    
    for (int i = 1; i <= 20; i++) {
      fileName = "file (" + i + ").txt";
      fullFileName = dir.resolve(fileName).toString();
      StringBuilder content = new StringBuilder();
      
      try {
        Files.readAllLines(Paths.get(fullFileName)).forEach(line -> content.append(line + "\r\n"));
        String text = content.toString();
        double start = System.nanoTime();
        
        // calculate words, sentences, syllables, and flesch within a single loop
        char prevChar = '\0';
        int words = 0;
        int sentences = 0;
        int syllables = 0;
        double fleschScore = 0;
        boolean lastWasVowel = false;
        HashSet<Character> vowels = new HashSet<Character>();
        vowels.addAll(Arrays.asList('a','e','i','o','u','y'));
        String word = "";
        StringBuilder wordBuilder = new StringBuilder();
        boolean wordComplete = false;
        int j = 0;
        
        for (char c : text.toCharArray()) {
          c = Character.toLowerCase(c);

          // detect words. Account for multi-spaces
          if ((c == ' ' && c != prevChar) || text.length() == i + 1 ) {
            words++;
            wordComplete = true;
            if (text.length() == i + 1) wordBuilder.append(c);
            word = wordBuilder.toString();
            wordBuilder = new StringBuilder();
          }
          else if (c == '-') {
            wordComplete = true;
            word = wordBuilder.toString();
            wordBuilder = new StringBuilder();
          }
          else {
            wordBuilder.append(c);
          }
          
          // detect sentence. Account for potential cases of . ! ? ... !!! ??? !? ?! etc
          if ((c == '.' || c == '!' || c == '?') && (c != prevChar && prevChar != '!' && prevChar != '?')) {
            sentences++;
          }
          
          // detect syllables
          if(vowels.contains(c)) {
            if(!lastWasVowel) {
              syllables++;            
            }
            if (c != 'y') {
              lastWasVowel = true;            
            }
         }
         else {
           lastWasVowel = false;                            
         }
          if (wordComplete) {
            if ((word.endsWith("e") || word.endsWith("es") || word.endsWith("ed")) && !word.endsWith("le") && word.length() > 2) {
              syllables--;
              wordComplete = false;
            }        
          }
          prevChar = c;
          j++;
        }
        
        fleschScore = 206.835 - 1.015 * ((double) words / sentences) - 84.6 * ((double) syllables / words);
        double end = System.nanoTime();
        
        double runtime = getRunTimeInMilliSeconds(start, end);
        loopTiming.put(fileName, runtime);
        super.updateProgress(i, 20);
        //System.out.println(String.format("(Single loop) %s - words: %d, sentences: %d, syllables: %d", fileName, words, sentences, syllables));
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    this.setSingleLoopTimingDataPoints(loopTiming);
  }
  
  //gets the difference, and converts to seconds
  private double getRunTimeInMilliSeconds(double start, double end) {
    return (double) (end - start) / 1_000_000;
  }

  @Override
  protected StatsCalculator call() throws Exception {
    singleLoopAlgorithm();
    threeLoopAlgorithm();
    return this;
  }
}
