import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

// This class was gratuitously stolen from the internet.  It was not well commented.
// Because it extends application, it will also crash if called more than once.  ClientPanel is written to handle this.
public class BarChrt extends Application {

    final static String Option1 = "Option 1";
    final static String Option2 = "Option 2";
    final static String Option3 = "Option 3";
    final static String Option4 = "Option 4";

    private static double op1value;
    private static double op2value;
    private static double op3value;
    private static double op4value;

    // Required for JavaFX initialization
    @Override public void init() throws Exception{
        super.init();
        Application.Parameters parameters = getParameters();
    }

    // Builds the bar graph window
    @Override public void start(final Stage stage) {
        stage.setTitle("Vote Result");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Vote Result");
        xAxis.setLabel("");
        yAxis.setLabel("% Chosen");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("");
        bc.setLegendVisible(false);
        series1.getData().add(new XYChart.Data(Option1, this.op1value));
        series1.getData().add(new XYChart.Data(Option2, this.op2value));
        series1.getData().add(new XYChart.Data(Option3, this.op3value));
        series1.getData().add(new XYChart.Data(Option4, this.op4value));

        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.show();
            }
        });
    }

    // Enter values and display the graph
    public void draw(double x,double y,double z,double k) {
        this.op1value = x;
        this.op2value = y;
        this.op3value = z;
        this.op4value = k;

        launch();
    }
}