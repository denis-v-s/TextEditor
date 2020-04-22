package tests;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import application.services.Utils;

public class UtilsWordCountTests {
  @Test
  public void word_count_is_0() {
    assertThat(Utils.getWordCount(""), equalTo(0));
  }
  
  @Test
  public void word_count_returns_2() {
    assertThat(Utils.getWordCount("Hello World"), equalTo(2));
  }
  
  @Test
  public void word_count_returns_2_w_extra_characters() {
    assertThat(Utils.getWordCount("?Hello. World!"), equalTo(2));
  }
  
}
