package controllers;

import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.profiles.Profile;
import models.test.results.GrammarResult;
import views.ViewManager;
import views.items.GrammarResultItem;
import views.items.ProfileListItem;
import views.items.ResultItem;

import java.util.Collections;

public class ViewProfileController {

	@FXML
	private JFXListView<ResultItem> grammarResultList;
	@FXML
	private JFXListView<ResultItem> pronunResultList;
	@FXML
	private Label labelName;
	@FXML
	private Label labelGender;
	@FXML
	private Label labelRecent;
	
	public void initialize() {
		grammarResultList.setCellFactory(lv -> new JFXListCell<ResultItem>() {
			@Override
			public void updateItem(ResultItem item, boolean empty)
			{
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(item);
				}
			}
		});
	}
	
	public void displayProfile(Profile profile) {
		initProfileInfo(profile);
		for (GrammarResult result : profile.getGrammarResults()) {
			grammarResultList.getItems().add(result.toGrammarResultItem());
		}
	}
	
	@FXML
	void onClickBack(ActionEvent event) {
		ViewManager.getInstance().switchScene(ViewManager.PATH_PROFILE);
	}

	private void initProfileInfo(Profile profile) {
		labelName.setText(profile.getInfo().get("name"));
		labelGender.setText(profile.getGender());
		Collections.sort(profile.getGrammarResults());
		if (profile.getGrammarResults().size() == 0) {
			labelRecent.setText("暂无");
		} else {
			labelRecent.setText(profile.getGrammarResults().get(0).getTestTime());
		}
	}
}
