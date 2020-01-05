package controllers;

import com.jfoenix.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import models.profiles.Profile;
import models.services.Recorder;
import models.test.AssessmentManager;
import models.test.Question;
import views.ViewManager;
import views.items.ConfirmDialog;

public class GrammarTestController extends BaseTestController {
	static final String[] scoreTexts = {"无声或“不知道”", "语义错误，结构错误", "部分或全部重复", "语义错误，结构正确", "语义正确，结构错误", "语义正确，结构正确"};

	@FXML
	private JFXButton btnBack;
	@FXML
	private JFXButton btnRecord;
	@FXML
	private JFXButton btnStopRecord;
	@FXML
	private JFXButton btnAnalyze;
	@FXML
	private JFXButton btnNext;
	@FXML
	private JFXTextArea textTranscribe;
	@FXML
	private JFXTextField textScore;
	@FXML
	private JFXSlider sliderScore;
	@FXML
	private JFXListView<String> resultList;
	@FXML
	private Label labelScore;
	
	public void initialize() {
		recorder = new Recorder();
		manager = AssessmentManager.getInstance();
		manager.startGrammarAssessment(this);
		manager.nextQuestion();
		initScoreDisplay();
	}

	@FXML
	void onClickRecord(ActionEvent event) {
		recorder.startRecord();
		btnStopRecord.toFront();
	}

	@FXML
	void onClickStopRecord(ActionEvent event) {
		recorder.stopRecord();
		btnStopRecord.toBack();
	}

	@FXML
	void onClickAnalyze(ActionEvent event) {
		manager.getAssessment().analyzeResponse(textTranscribe.getText());
	}
	
	@FXML
	void onClickBack(ActionEvent event) {
		ViewManager.getInstance().switchScene(ViewManager.PATH_TESTMENU);
	}

	@FXML
	void onClickNext(ActionEvent event) {
		manager.nextQuestion();
		sliderScore.setValue(0);
		labelScore.setText("");
	}

	private void initScoreDisplay() {
		textScore.setText("0");
		sliderScore.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderScore.setValue((int) Math.round(newValue.doubleValue()));
			labelScore.setText(scoreTexts[(int) sliderScore.getValue()]);
		});
		Bindings.bindBidirectional(textScore.textProperty(), sliderScore.valueProperty(), new StringConverter<Number>() {
			@Override
			public String toString(Number object) {
				return object.toString();
			}

			@Override
			public Integer fromString(String string) {
				return Integer.parseInt(string);
			}
		});
		sliderScore.setValue(0);
		labelScore.setText("");
	}

}
