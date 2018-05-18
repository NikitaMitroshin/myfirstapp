import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


public class FirstApp extends Application {

    public static final String JSON_FILE_URL = "http://echo.jsontest.com/key/value/one/two.json";
    public static final String JSON_ERROR = "error during JSON request";

    @Override
    public void start(Stage primaryStage) {
        FlowPane pane = new FlowPane();
        createElements(pane);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane, 200, 250);

        primaryStage.setTitle("Get Values For Fields:");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createElements(FlowPane pane) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");

        TextField oneField = new TextField();
        Label oneLabel = new Label("One: ");
        oneField.setVisible(false);

        TextField twoField = new TextField();
        twoField.setVisible(false);
        Label twoLabel = new Label("Key: ");
        oneField.setVisible(false);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String textForButton = getTextForField(JSON_FILE_URL);
                if (JSON_ERROR.equals(textForButton)) {
                    oneField.setVisible(true);
                    oneField.setText(textForButton);
                    return;
                }
                Gson gson = new Gson();
                JsonExample jsonExample = gson.fromJson(textForButton, JsonExample.class);
                oneField.setVisible(true);
                oneField.setText(jsonExample.getOne());
                twoField.setVisible(true);
                twoField.setText(jsonExample.getKey());
            }
        });

        List<Node> children = pane.getChildren();

        addElementToPanel(children, btn, pane);
        addElementToPanel(children, oneLabel, pane);
        addElementToPanel(children, oneField, pane);
        addElementToPanel(children, twoLabel, pane);
        addElementToPanel(children, twoField, pane);
    }

    public static void addElementToPanel(List<Node> children, Node element, FlowPane pane) {
        children.add(element);
        Region nextLineRegion = getNextLineRegion();
        pane.getChildren().add(nextLineRegion);
    }

    public static Region getNextLineRegion() {
        Region nextLineRegion = new Region();
        nextLineRegion.setPrefSize(Double.MAX_VALUE, 0.0);
        return nextLineRegion;
    }

    public static String getTextForField(String url) {
        String json = JSON_ERROR;
        try {
            json = readJsonFromUrl(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return json;
        }
        return json;
    }

    public static String readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream();
             BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")))) {
            return readAll(rd);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
