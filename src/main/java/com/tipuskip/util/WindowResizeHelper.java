package com.tipuskip.util;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Utility untuk memungkinkan pengubahan ukuran (resizing) jendela JavaFX
 * yang menggunakan StageStyle.UNDECORATED (Custom Integrated Title Bar).
 */
public class WindowResizeHelper {

    private static final int BORDER_PADDING = 8;
    private static Cursor cursor = Cursor.DEFAULT;
    private static double startX = 0;
    private static double startY = 0;
    private static double startStageX = 0;
    private static double startStageY = 0;
    private static double startStageWidth = 0;
    private static double startStageHeight = 0;

    public static void attach(Stage stage, Scene scene) {
        ResizeListener listener = new ResizeListener(stage);
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
    }

    private static class ResizeListener implements EventHandler<MouseEvent> {
        private final Stage stage;

        public ResizeListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent event) {
            if (stage.isMaximized()) {
                stage.getScene().setCursor(Cursor.DEFAULT);
                return;
            }

            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            double sceneWidth = stage.getScene().getWidth();
            double sceneHeight = stage.getScene().getHeight();

            if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                boolean left = mouseX < BORDER_PADDING;
                boolean right = mouseX > sceneWidth - BORDER_PADDING;
                boolean top = mouseY < BORDER_PADDING;
                boolean bottom = mouseY > sceneHeight - BORDER_PADDING;

                if (top && left) {
                    cursor = Cursor.NW_RESIZE;
                } else if (top && right) {
                    cursor = Cursor.NE_RESIZE;
                } else if (bottom && left) {
                    cursor = Cursor.SW_RESIZE;
                } else if (bottom && right) {
                    cursor = Cursor.SE_RESIZE;
                } else if (left) {
                    cursor = Cursor.W_RESIZE;
                } else if (right) {
                    cursor = Cursor.E_RESIZE;
                } else if (top) {
                    cursor = Cursor.N_RESIZE;
                } else if (bottom) {
                    cursor = Cursor.S_RESIZE;
                } else {
                    cursor = Cursor.DEFAULT;
                }
                stage.getScene().setCursor(cursor);

            } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                startX = event.getScreenX();
                startY = event.getScreenY();
                startStageX = stage.getX();
                startStageY = stage.getY();
                startStageWidth = stage.getWidth();
                startStageHeight = stage.getHeight();

            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if (cursor == Cursor.DEFAULT) {
                    return;
                }

                double deltaX = event.getScreenX() - startX;
                double deltaY = event.getScreenY() - startY;
                double minWidth = stage.getMinWidth() > 0 ? stage.getMinWidth() : 600;
                double minHeight = stage.getMinHeight() > 0 ? stage.getMinHeight() : 400;

                if (cursor == Cursor.E_RESIZE || cursor == Cursor.NE_RESIZE || cursor == Cursor.SE_RESIZE) {
                    double newWidth = startStageWidth + deltaX;
                    if (newWidth >= minWidth) {
                        stage.setWidth(newWidth);
                    }
                }

                if (cursor == Cursor.S_RESIZE || cursor == Cursor.SW_RESIZE || cursor == Cursor.SE_RESIZE) {
                    double newHeight = startStageHeight + deltaY;
                    if (newHeight >= minHeight) {
                        stage.setHeight(newHeight);
                    }
                }

                if (cursor == Cursor.W_RESIZE || cursor == Cursor.NW_RESIZE || cursor == Cursor.SW_RESIZE) {
                    double newWidth = startStageWidth - deltaX;
                    if (newWidth >= minWidth) {
                        stage.setX(startStageX + deltaX);
                        stage.setWidth(newWidth);
                    }
                }

                if (cursor == Cursor.N_RESIZE || cursor == Cursor.NW_RESIZE || cursor == Cursor.NE_RESIZE) {
                    double newHeight = startStageHeight - deltaY;
                    if (newHeight >= minHeight) {
                        stage.setY(startStageY + deltaY);
                        stage.setHeight(newHeight);
                    }
                }
            }
        }
    }
}
