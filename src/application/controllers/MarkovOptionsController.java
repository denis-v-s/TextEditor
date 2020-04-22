package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.services.Markov;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MarkovOptionsController implements Initializable {
  @FXML TextField txtStartingWord, txtNumberOfWords;
  @FXML Button btnGenerate;
  private TextArea textArea;
  
  public MarkovOptionsController(TextArea textArea) {
    this.textArea = textArea;
  }
  
  public void onGenerateClick() {
    String word = txtStartingWord.getText();
    int n = getNumericValue();
    
    if (!word.isEmpty() && n > 0) {
      Markov m = new Markov(textArea.getText());
      String newText = m.generateRandomText(word, n);
      textArea.clear();
      textArea.setText(newText);
      
      Stage stage = (Stage) btnGenerate.getScene().getWindow();
      stage.close();
    }
  }
  
  private int getNumericValue() {
    try {
      return Integer.parseInt(txtNumberOfWords.getText());
    }
    catch (Exception e) {
      return 0;
    }
  }
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    // TODO Auto-generated method stub
    
  }

}
