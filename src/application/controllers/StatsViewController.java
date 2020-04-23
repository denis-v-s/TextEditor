package application.controllers;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

import application.services.StatsCalculator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TooltipBuilder;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class StatsViewController implements Initializable {
  @FXML LineChart<String, Double> cLineChart;
  @FXML ProgressIndicator progress;
  @FXML Region shade;
  Task<StatsCalculator> calculator;
  
  public StatsViewController(Task<StatsCalculator> calculator) {
    this.calculator = calculator;
  }
  
  public void onRefreshButtonClick() {
    this.calculator = new StatsCalculator();
    
    new Thread(new Runnable() {
      public void run() {
        calculator.run();
      }
    }).start();
    
    updateChart();
  }
  
  public void updateChart() {
    cLineChart.getData().clear();
    cLineChart.getXAxis().setAnimated(false);
    
    shade.visibleProperty().bind(calculator.runningProperty());
    progress.visibleProperty().bind(calculator.runningProperty());
    progress.progressProperty().bind(calculator.progressProperty());
    
    XYChart.Series<String, Double> seriesA = new XYChart.Series<>();
    seriesA.setName("Single Loop");
    XYChart.Series<String, Double> seriesB = new XYChart.Series<>();
    seriesB.setName("Three Loops");
    
    calculator.setOnSucceeded(e -> {
      try {
        seriesA.setData(calculator.get().getSingleLoopTiming());
        // attach tooltips
        for (Data<String, Double> point : seriesA.getData()) {
          Tooltip t = new Tooltip(point.getYValue().toString());
          hackTooltipStartTiming(t);
          Tooltip.install(point.getNode(), t);
        }
        seriesB.setData(calculator.get().getThreeLoopTiming());
        for (Data<String, Double> point : seriesB.getData()) {
          Tooltip t = new Tooltip(point.getYValue().toString());
          hackTooltipStartTiming(t);
          Tooltip.install(point.getNode(), t);
        }
      } catch (InterruptedException | ExecutionException e1) {
        e1.printStackTrace();
      }
    });
    
    calculator.setOnFailed(e -> System.out.println("something went wrong"));
    
    cLineChart.getData().add(seriesA);
    cLineChart.getData().add(seriesB);
  }
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    updateChart();
  }
  
  public static void hackTooltipStartTiming(Tooltip tooltip) {
    try {
        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
        fieldBehavior.setAccessible(true);
        Object objBehavior = fieldBehavior.get(tooltip);

        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
        fieldTimer.setAccessible(true);
        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

        objTimer.getKeyFrames().clear();
        objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
