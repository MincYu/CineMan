package jp.co.worksap.intern.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import jp.co.worksap.intern.ui.main.Main;
import jp.co.worksap.intern.ui.market.MemberClassifyController;
import jp.co.worksap.intern.ui.sales.SalesStrategyController;

import java.io.IOException;

/**
 * Created by yuminchen on 16/7/25.
 */
public class GeneralController {

    private static GeneralController controller;

    private AnchorPane centerPanel;

    private BorderPane generalPanel;

    @FXML
    private Label strategy;
    @FXML
    private Label market;

    private Main main;

    /**
     *
     * @param mainApp
     */
    public void setMain(Main mainApp) {
        this.main = mainApp;

    }

    /**
     * set up the general user interface
     *
     * @param main
     * @return
     * @throws IOException
     */
    public static Parent launch(Main main) throws IOException{

        if(controller!=null){
            return controller.generalPanel;
        }

        FXMLLoader loader = new FXMLLoader(
                GeneralController.class.getResource("General.fxml"));

        Pane pane = loader.load();
        controller = loader.getController();
        controller.setMain(main);

        if(pane instanceof BorderPane) {
            controller.generalPanel = (BorderPane) pane;
        }

        controller.centerPanel = (AnchorPane) controller.generalPanel.getCenter();

        return pane;
    }

    @FXML
    private void minimize(){
        main.getPrimaryStage().toBack();
    }

    @FXML
    private void close(){
        main.getPrimaryStage().close();
        System.exit(0);
    }

    @FXML
    private void jump2Strategy(){

        removeCss();
        strategy.getStyleClass().removeAll("label-subtitle");
        strategy.getStyleClass().addAll("label-subtitle-clicked");

        try {

            Pane replacedPanel = SalesStrategyController.launch();
            if(replacedPanel instanceof AnchorPane){
                generalPanel.setCenter(replacedPanel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void jump2Main(){
        removeCss();
        generalPanel.setCenter(centerPanel);
    }

    @FXML
    private void jump2Market(){
        removeCss();
        market.getStyleClass().removeAll("label-subtitle");
        market.getStyleClass().addAll("label-subtitle-clicked");

        try {

            Pane replacedPanel = MemberClassifyController.launch();
            if(replacedPanel instanceof AnchorPane){
                generalPanel.setCenter(replacedPanel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void removeCss(){
        market.getStyleClass().clear();
        strategy.getStyleClass().clear();
        market.getStyleClass().addAll("label-subtitle");
        strategy.getStyleClass().addAll("label-subtitle");


    }

    public void changeCenter(Pane pane){
        generalPanel.setCenter(pane);
    }

    public static GeneralController getController() {
        return controller;
    }
}
