package org.bakerydb.frontend;

import java.io.IOException;
import java.util.Collection;

import org.bakerydb.frontend.controllers.StatusMessageController;

import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputControl;
import javafx.util.StringConverter;

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

    // public static void showEditor(
    //     Collection<Property<?>> properties,
    //     Collection<StringConverter<?>> converters,
    //     Collection<TextInputControl> fields
    // ) {
    //     FXMLLoader fxmlLoader = loader("views/Editor.fxml");
    //     DialogPane dialogPane;
    //     try {
    //         dialogPane = fxmlLoader.load();
    //     } catch (IOException e) {
    //         showStatusMessage("Error Opening Editor", e.getMessage(), true);
    //         return;
    //     }
    //     EditorController editorController = fxmlLoader.getController();
    //     editorController.setProperties(properties);
    //     editorController.setConverters(converters);
    //     editorController.setFields(fields);
    //     Dialog<ButtonType> dialog = new Dialog<>();
    //     dialog.setDialogPane(dialogPane);
    //     dialog.setTitle("Editor");
    //     dialog.showAndWait();
    //
    // }
}
