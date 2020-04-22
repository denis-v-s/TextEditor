package tests;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import application.services.Utils;

public class UtilsSentenceCountTests {
  @Test
  public void sentence_count_returns_0() {
    assertThat(Utils.getSentenceCount(""), equalTo(0));
  }
  
  @Test
  public void sentence_count_returns_1() {
    assertThat(Utils.getSentenceCount("Hello World"), equalTo(1));
  }
  
  @Test
  public void sentence_count_returns_2() {
    assertThat(Utils.getSentenceCount("Hello... World!"), equalTo(2));
  }
  
  @Test
  public void sentence_count_returns_2_2() {
    assertThat(Utils.getSentenceCount("Hello?! World!"), equalTo(2));
  }
  
  @Test
  public void t() {
    String text = "H!!!W!";
    int sentences = 0;
    char prevChar = '\0';
    for (char c : text.toCharArray()) {
      c = Character.toLowerCase(c);
      // detect sentence. Account for potential cases of . ! ? ... !!! ??? !? ?! etc
      if ((c == '.' || c == '!' || c == '?') && (c != prevChar && prevChar != '!' && prevChar != '?')) {
        sentences++;
      }
      prevChar = c;
    }
    
    assertThat(sentences, equalTo(2));
  }
}
