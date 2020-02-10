package controllers.items;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.BaseTestController;
import controllers.DialogControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.profiles.ProfileWriter;
import models.test.AssessmentManager;
import models.test.grammar.Utterance;
import models.test.pronun.Syllable;
import models.test.results.*;
import org.jetbrains.annotations.NotNull;
import views.ResultDisplayer;
import views.ViewManager;
import views.items.ConfirmDialog;

public class PronunSummaryController extends ItemController implements DialogControl {
    @FXML
    private AnchorPane pane;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXComboBox<Label> resultComboBox;
    @FXML
    private JFXButton btnDiscard;
    @FXML
    private JFXButton btnSave;
    @FXML
    private Label labelAge;
    @FXML
    private Label labelTime;

    private PronunResult result;
    private BaseTestController testController;
    private ConfirmDialog confirmDialog;

    public void initialize() {
        pane.setLayoutY(90);
        pane.getChildren().removeAll(btnDiscard, btnSave);
    }

    @Override
    public void setResult(BaseResult result) {
        this.result = (PronunResult) result;
        this.labelAge.setText(this.result.testAge);
        this.labelTime.setText(this.result.getTestTime());
        setResultComboBox();
        resultComboBox.setValue(resultComboBox.getItems().get(0));
        displayResult("音节");
    }

    @Override
    public void setOnAfterTest(BaseTestController controller) {
        this.testController = controller;
        pane.getChildren().addAll(btnDiscard, btnSave);
    }

    @FXML
    void onClickDiscard(ActionEvent event) {
        stackPane.toFront();
        confirmDialog = new ConfirmDialog(this, stackPane, new JFXDialogLayout());
        confirmDialog.setText(ConfirmDialog.TEXT_BACKINTEST);
        confirmDialog.show();
    }

    @FXML
    void onClickSave(ActionEvent event) {
        AssessmentManager.profile.getPronunResults().add(result);
        ProfileWriter.updateProfileResultToXML(AssessmentManager.profile, "pronun");
        stackPane.toFront();
        confirmDialog = new ConfirmDialog(this, stackPane, new JFXDialogLayout());
        confirmDialog.setText(ConfirmDialog.TEXT_SAVEPROFILE);
        confirmDialog.show();
    }

    private void setResultComboBox() {
        Label syllableLabel = new Label("音节");
        Label inventoryLabel = new Label("发音量表");
        Label errorLabel = new Label("错误模式");
        resultComboBox.getItems().addAll(syllableLabel, inventoryLabel, errorLabel);

        resultComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            displayResult(newValue.getText());
        });
    }

    private void displayResult(String type) {
        switch (type) {
            case "音节":
                setSyllableTable();
                break;
            case "发音量表":
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickNoDialog() {
        ViewManager.getInstance().switchScene(ViewManager.PATH_TESTMENU);
    }

    @Override
    public void onClickYesDialog() {
        if (confirmDialog.isSingleAction) {
            ViewManager.getInstance().switchScene(ViewManager.PATH_TESTMENU);
        }
        confirmDialog.close();
        stackPane.toBack();
    }

    private void setSyllableTable() {
        ObservableList<Syllable> syllables = FXCollections.observableArrayList(result.syllables);
        final TreeItem<Syllable> root = new RecursiveTreeItem<>(syllables, RecursiveTreeObject::getChildren);

        JFXTreeTableColumn<Syllable, String> targetColumn = new JFXTreeTableColumn<>("目标音节");
        targetColumn.setPrefWidth(150);
        targetColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Syllable, String> param) ->{
            if (targetColumn.validateValue(param))
                return param.getValue().getValue().getTargetProperty();
            else
                return targetColumn.getComputedValue(param);
        });

        JFXTreeTableColumn<Syllable, String> responseColumn = new JFXTreeTableColumn<>("发音");
        responseColumn.setPrefWidth(150);
        responseColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Syllable, String> param) ->{
            if (responseColumn.validateValue(param))
                return param.getValue().getValue().getResponseProperty();
            else
                return responseColumn.getComputedValue(param);
        });

        JFXTreeTableColumn<Syllable, String> presentColumn = new JFXTreeTableColumn<>("正确辅音");
        presentColumn.setPrefWidth(150);
        presentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Syllable, String> param) ->{
            if (presentColumn.validateValue(param))
                return param.getValue().getValue().getPresentConsonantProperty();
            else
                return presentColumn.getComputedValue(param);
        });

        JFXTreeTableColumn<Syllable, String> errorColumn = new JFXTreeTableColumn<>("错误模式");
        errorColumn.setPrefWidth(200);
        errorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Syllable, String> param) ->{
            if (errorColumn.validateValue(param))
                return param.getValue().getValue().getErrorProperty();
            else
                return errorColumn.getComputedValue(param);
        });

        JFXTreeTableView<Syllable> table = new JFXTreeTableView<>(root);
        table.getColumns().setAll(targetColumn, responseColumn, presentColumn, errorColumn);
        addTableToPane(table);
    }

    private void setInventoryTable() {

    }

    private void addTableToPane(@NotNull JFXTreeTableView table) {
        table.setLayoutX(450);
        table.setLayoutY(80);
        table.setPrefHeight(500);
        table.setShowRoot(false);
        pane.getChildren().add(table);
    }
}
