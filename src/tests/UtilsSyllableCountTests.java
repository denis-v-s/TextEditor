package tests;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.HashSet;

import application.services.Utils;


public class UtilsSyllableCountTests {
  @Test
  public void syllable_count_returns_0() {
    assertThat(Utils.getSyllableCount(""), equalTo(0));
  }
  
  @Test
  public void syllable_count_returns_1() {
    assertThat(Utils.getSyllableCount("a"), equalTo(1));
    assertThat(Utils.getSyllableCount("we"), equalTo(1));
    assertThat(Utils.getSyllableCount("look"), equalTo(1));
    assertThat(Utils.getSyllableCount("take"), equalTo(1));
  }
  
  @Test
  public void syllable_count_returns_2() {
    assertThat(Utils.getSyllableCount("rule"), equalTo(2));
    assertThat(Utils.getSyllableCount("again"), equalTo(2));
  }
  
  @Test
  public void syllable_count_returns_3() {
    assertThat(Utils.getSyllableCount("worrying"), equalTo(3));
    assertThat(Utils.getSyllableCount("ominous"), equalTo(3));
    assertThat(Utils.getSyllableCount("nerve-racking"), equalTo(3));
  }
  
  @Test
  public void t() {
    String text = "ominous";
    char prevChar = '\0';
    HashSet<Character> vowels = new HashSet<Character>();
    vowels.addAll(Arrays.asList('a','e','i','o','u','y'));
    boolean lastWasVowel = false;
    int syllables = 0;
    char[] chars = text.toCharArray();
    int words = 0;
    StringBuilder wordBuilder = new StringBuilder(); // build each word in order to be able to use .endsWith function
    boolean wordComplete = false;
    String word = "";
    int i = 0;
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
      else {
        wordBuilder.append(c);
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
     else { // this character isn't a vowel
       lastWasVowel = false;                            
     }
      

      if (wordComplete) {
        if ((word.endsWith("e") || word.endsWith("es") || word.endsWith("ed")) && !word.endsWith("le") && word.length() > 2) {
          syllables--;
          wordComplete = false;
        }        
      }
      prevChar = c;
      i++;
    }
    
    assertThat(syllables, equalTo(3));
  }
}
