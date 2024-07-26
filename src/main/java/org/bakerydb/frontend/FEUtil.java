package org.bakerydb.frontend;

import java.io.IOException;
import java.util.Optional;

import org.bakerydb.backend.models.Hours;
import org.bakerydb.frontend.controllers.ClockInEditorController;
import org.bakerydb.frontend.controllers.EditorController;
import org.bakerydb.frontend.controllers.StatusMessageController;
import org.bakerydb.util.Model;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class FEUtil {
    public static FXMLLoader loader(String name) {
        String resource = name;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(
            Thread.currentThread()
            .getContextClassLoader()
            .getResource(resource)
        );

        return fxmlLoader;
    }

    public static void showStatusMessage(
        String title,
        String message,
        boolean isError
    ) {
        FXMLLoader fxmlLoader = loader("views/StatusMessage.fxml");
        DialogPane dialogPane;

        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        StatusMessageController statusMessageController = fxmlLoader.getController();
        statusMessageController.setMessage(message);
        statusMessageController.setError(isError);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        dialog.showAndWait();
    }

    public static <T extends Model<T>> void showAddEditor(T model, String title, ObservableList<T> observableList) {
        Boolean isAdd = observableList != null;

        T clone = model.clone();
        FXMLLoader fxmlLoader = loader("views/Editor.fxml");
        DialogPane dialogPane;

        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            showStatusMessage("Error Opening Editor", e.getMessage(), true);
            return;
        }

        EditorController editorController = fxmlLoader.getController();
        editorController.setItem(clone);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        Optional<ButtonType> clickedButton =  dialog.showAndWait();

        if (!clickedButton.isPresent()) {
            showStatusMessage(
                "JavaFX Error",
                "Node was not fully loaded",
                true
            );
        } else if (clickedButton.get() == ButtonType.OK) {
            if (isAdd) {
                clone.addToDB()
                    .onSuccess(k -> {
                        model.update(clone);
                        observableList.add(clone);
                    })
                    .onError(m -> showStatusMessage("Failed to Add Item", m, true));
            } else {
                clone.updateDB()
                    .onSuccess(() -> model.update(clone))
                    .onError(m -> showStatusMessage("Failed to Update Item", m, true));
            }
        }
    }

    public static <T extends Model<T>> void showUpdateEditor(T model, String title) {
        showAddEditor(model, title, null);
    }

    public static <T extends Model<T>> void showClockInEditor(T model, String title, ObservableList<T> observableList) {
        Boolean isAdd = observableList != null;

        T clone = model.clone();
        FXMLLoader fxmlLoader = loader("views/ClockInEditor.fxml");
        DialogPane dialogPane;

        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            showStatusMessage("Error Opening Editor", e.getMessage(), true);
            return;
        }

        ClockInEditorController editorController = fxmlLoader.getController();
        editorController.setItem((Hours) clone);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (!clickedButton.isPresent()) {
            showStatusMessage(
                "JavaFX Error",
                "Node was not fully loaded",
                true
            );
        } else if (clickedButton.get() == ButtonType.OK) {
            if (isAdd) {
                clone.addToDB()
                    .onSuccess(k -> {
                        model.update(clone);
                        observableList.add(clone);
                    })
                    .onError(m -> showStatusMessage("Failed to Add Item", m, true));
            }
        }
    }

    public static Optional<ButtonType> showCustomDialog(DialogPane dialogPane, String title) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);
        return dialog.showAndWait();
    }
}
