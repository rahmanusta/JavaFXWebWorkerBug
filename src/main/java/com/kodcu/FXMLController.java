package com.kodcu;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FXMLController implements Initializable {

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    @FXML
    private WebView webView;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        final WebEngine engine = webView.getEngine();
        engine.load("http://asciidocfx.com/webworker.html");
    }

   /*
   // I will not crash
    @WebkitCall(from = "webworker.html")
    public void processData(JSObject data) {
        String name = (String) data.getMember("name");
    }
    */

    @WebkitCall(from = "webworker.html")
    public void processData(JSObject data) {
        executorService.submit(() -> {
            String name = (String) data.getMember("name");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final WebEngine engine = webView.getEngine();
        engine.getLoadWorker().stateProperty().addListener((observableValue, state, t1) -> {
            if (t1 == Worker.State.SUCCEEDED) {
                final JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);
            }
        });
    }
}
