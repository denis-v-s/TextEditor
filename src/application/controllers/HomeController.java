package application.controllers;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.App;
import application.services.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class HomeController implements Initializable {
  @FXML TextArea txtTextArea;
  @FXML Label lblWordCount;
  @FXML Label lblSentenceCount;
  @FXML Label lblSyllabelCount;
  @FXML Label lblFleschScore;

  private App context;
  private Path dir = Paths.get("").toAbsolutePath(); // project directory
  private String inputFile;
  private StringBuilder content = new StringBuilder();
  FileChooser fileChooser;
  
  public HomeController() {
    fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(dir.toFile());
    
    ExtensionFilter extensionFilter = new ExtensionFilter("Text files (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(extensionFilter);
    fileChooser.setSelectedExtensionFilter(extensionFilter);
  }
  
  public void setContext(App context) {
    this.context = context;
  }
  
  public void onOpenClick() {
    File selection = fileChooser.showOpenDialog(context.getStage());
    if (selection != null) {
      inputFile = selection.toString();
      content = new StringBuilder();
      
      try {
        Files.readAllLines(Paths.get(inputFile)).forEach(line -> content.append(line + "\r\n"));
        txtTextArea.setText(content.toString());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    refreshStatusBar();
  }
  
  public void onNewClick() {
    content = new StringBuilder();
    txtTextArea.clear();
    refreshStatusBar();
  }
  
  public void onCloseClick() {
    onNewClick();
  }
  
  public void onSaveClick() {
    File selection = fileChooser.showSaveDialog(context.getStage());
    if (selection != null) {
      content = new StringBuilder(txtTextArea.getText());
      inputFile = selection.toString();
      // save
      try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(inputFile))) {
        writer.write(content.toString());
      }
      catch (Exception e) {
        System.out.println("error: " + e.getMessage());
      }
    }
  }
  
  public void onExitClick() {
    Platform.exit();
  }
  
  public void onCopyClick() {
    txtTextArea.copy();
  }
  
  public void onCutClick() {
    txtTextArea.cut();
    refreshStatusBar();
  }
  
  public void onDeleteClick() {
    int start = txtTextArea.getSelection().getStart();
    int end = txtTextArea.getSelection().getEnd();
    txtTextArea.deleteText(start, end);
    refreshStatusBar();
  }
  
  public void onPasteClick() {
    txtTextArea.paste();
    refreshStatusBar();
  }
  
  public void onMarkovClick() throws IOException {
    // generate a pop up
    final Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/MarkovOptionsView.fxml"));
    loader.setControllerFactory(t -> new MarkovOptionsController(txtTextArea));
    Parent root = loader.load();
    
    stage.setScene(new Scene(root));
    stage.setResizable(false);
    stage.setTitle("Markov Generator Options");
    stage.setAlwaysOnTop(true);
    stage.show();
  }
  
  public void onTruncateClick() {
    String file = dir.resolve("data").resolve("warAndPeace.txt").toString();
    StringBuilder wap = new StringBuilder();
    
    try {
      Files.readAllLines(Paths.get(file)).forEach(line -> wap.append(line + "\r\n"));
      Utils.saveChunks(wap.toString());
      //Utils.saveChunks(content.toString());
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("info");
      alert.setContentText("Truncate operation complete");
      alert.show();
    } 
    catch (IOException e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("error");
      alert.setContentText("There was an error while reading the main file\r\n" + e.getMessage());
      alert.show();
    }
  }
  
  public void onShowClick() {
    
  }
  
  public void onPlotClick() throws IOException {
    final Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/StatsView.fxml"));
    loader.setControllerFactory(t -> new StatsViewController());
    Parent root = loader.load();
    
    stage.setScene(new Scene(root));
    stage.setResizable(false);
    stage.setTitle("Stats");
    stage.setAlwaysOnTop(true);
    stage.show();        
  }
  
  public void onCalculateAllClick() {
    refreshStatusBar();
  }
  
  public void onWordCountClick() {

  }
  
  public void onSentenceCountClick() {

  }
  
  public void onFleschScoreClick() {

  }
  
  private void refreshWordCount() {
    lblWordCount.setText("words: " + Utils.getWordCount(content.toString()));
  }
  
  private void refreshSentenceCount() {
    lblSentenceCount.setText("sentences: " + Utils.getSentenceCount(content.toString()));
  }
  
  private void refreshSyllabelCount() {
    lblSyllabelCount.setText("syllables: " + Utils.getSyllableCount(content.toString()));
  }
  
  private void refreshFleschScore() {
    lblFleschScore.setText(String.format("flesch score: %.2f", Utils.getFleschScore()));
  }
  
  public void refreshStatusBar() {
    content = new StringBuilder(txtTextArea.getText());
    refreshWordCount();
    refreshSentenceCount();
    refreshSyllabelCount();
    refreshFleschScore();
  }
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set test file
    inputFile = dir.resolve("data").resolve("test.txt").toString();
    try {
      Files.readAllLines(Paths.get(inputFile)).forEach(line -> content.append(line + "\r\n"));
      //txtTextArea.setText(fileContent.toString());
    } catch (Exception e) {
      System.out.println("error: " + e.getMessage());
    }
    
    txtTextArea.setText(content.toString());
    refreshStatusBar();
  }

}
