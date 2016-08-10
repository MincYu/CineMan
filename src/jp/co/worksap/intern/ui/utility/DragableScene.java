package jp.co.worksap.intern.ui.utility;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by yuminchen on 16/7/25.
 */
public class DragableScene {

    static double ix;
    static double iy;

    public static void enableDrag(Scene scene, Stage stage) {

        scene.setOnMousePressed(event -> {
            ix = event.getScreenX() - stage.getX();
            iy = event.getScreenY() - stage.getY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - ix);
            stage.setY(event.getScreenY() - iy);
        });
    }

}

