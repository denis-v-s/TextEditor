package tests;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import application.services.Markov;

public class UtilsMarkovTests {
  @Test
  public void markov() {
    String text = "link1 car link2 boat link1 house link2 yard car boat house";
    Markov m = new Markov(text);
    
    String result = m.generateRandomText("car", 5);
    assertThat(result.split(" ").length, equalTo(5));
  }
}
