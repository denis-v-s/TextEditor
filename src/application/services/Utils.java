package application.services;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

public class Utils {
  private static int words = 0;
  private static int sentences = 0;
  private static int syllables = 0;
  
  public static double getFleschScore() {
    return 206.835 - 1.015 * ((double) words / sentences) - 84.6 * ((double) syllables / words);
  }
  
  public static int getWordCount(String text) {
    words = (text.isEmpty() || text == null) 
          ? 0
          : text.split("\\s+").length;
    
    return words;
  }
  
  public static int getSentenceCount(String text) {
    text = text.trim();
    sentences = (text.isEmpty() || text == null) 
        ? 0
        : text.split("[!?.]+").length;
    
    return sentences;
  }
  
  public static int getSyllableCount(String text) {
    syllables = 0;
    // split words by space, or a hyphen
    for (String word : text.split("(\\s+|-)")) {
      syllables += countSyllablesInAWord(word);
    }
    
    return syllables;
  }
  
  private static int countSyllablesInAWord(String word) {
    int count = 0;
    word = word.toLowerCase();
    boolean lastWasVowel = false;
    HashSet<Character> vowels = new HashSet<Character>();
    vowels.addAll(Arrays.asList('a','e','i','o','u','y'));

    for(char c : word.toCharArray()) {
       if(vowels.contains(c)) {
          if(!lastWasVowel) {
            count++;            
          }
          if (c != 'y') {
            lastWasVowel = true;            
          }
       }
       else {
         lastWasVowel = false;                              
       }
    }

    if ((word.endsWith("e") || word.endsWith("es") || word.endsWith("ed")) && !word.endsWith("le") && word.length() > 2) {
      count--;      
    }

    return count;
  }
  
  public static void saveChunks(String text) {
    getWordCount(text);
    
    // write 20 files from 5% to 100% of original words
    for (int i = 1; i <= 20; i++) {
      int numberOfWords = (int) ((0.05 * i) * words);
      String content = getNWords(text, numberOfWords);
      
      String fileName = String.format("file (%d).txt", i);
      String fullFileName = Paths.get("").toAbsolutePath().resolve("outputData").resolve(fileName).toString();
      
      try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fullFileName))) {
        writer.write(content);
      }
      catch (Exception e) {
        System.out.println("error writing the file: " + e.getMessage());
      }
    }
  }
  
  // @params:
  // * text: main text
  // * n: number of words to save
  private static String getNWords(String text, int n) {
    StringBuilder content = new StringBuilder();
    int i = 0;
    for (String word : text.split("\\s+")) {
      content.append(word + " ");
      if (++i == n) break;
    }
    
    return content.toString();
  }
}
