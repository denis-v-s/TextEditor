package application.controllers;

import java.net.URL;
import java.util.*;

import application.services.StatsCalculator;
import javafx.fxml.*;
import javafx.scene.chart.*;
import javafx.scene.control.ProgressIndicator;

public class StatsViewController implements Initializable {
  @FXML LineChart<String, Double> cLineChart;
  
  public void updateChart() {
    cLineChart.getData().clear();
    StatsCalculator calculator = new StatsCalculator();
           
    final LinkedHashMap<String, Double> singleLoopStats = (LinkedHashMap<String, Double>) calculator.singleLoopAlgorithm();
    final LinkedHashMap<String, Double> threeLoopStats = (LinkedHashMap<String, Double>) calculator.threeLoopAlgorithm();
    XYChart.Series<String, Double> seriesA = new XYChart.Series<>();
    seriesA.setName("Single Loop");
    XYChart.Series<String, Double> seriesB = new XYChart.Series<>();
    seriesB.setName("Three Loops");
    
    for (String key : singleLoopStats.keySet()) {
      seriesA.getData().add(new XYChart.Data<>(key, singleLoopStats.get(key)));      
    }
    
    for (String key : threeLoopStats.keySet()) {
      seriesB.getData().add(new XYChart.Data<>(key, threeLoopStats.get(key)));      
    }
    
    cLineChart.getData().add(seriesA);
    cLineChart.getData().add(seriesB);
  }
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    updateChart();
  }

}
