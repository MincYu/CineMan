package jp.co.worksap.intern.ui.main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jp.co.worksap.intern.ui.GeneralController;
import jp.co.worksap.intern.ui.utility.DragableScene;

/**
 * Created by yuminchen on 16/7/24.
 */
public class Main extends Application{

    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        Parent root = GeneralController.launch(this);
        Scene scene = new Scene(root);

        this.primaryStage.setTitle("CineMan");
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
        this.primaryStage.setScene(scene);
        DragableScene.enableDrag(scene,primaryStage);
        this.primaryStage.show();
    }
}
