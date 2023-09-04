package com.prac.musicplayer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Layout {
    Scene scene;

    TextField directoryField,selectedTrack,searchField;

    Button load,previous,play,pause,next;

    Label directoryHeader,sliderLabel;

    Slider timeSlider;

    BorderPane borderPane;

    ListView<String> listView;

    MediaEntity mediaEntity;

    List<String> list;

    List<String> trackList;

    public Layout(Stage stage){
        Image image = new Image("icon.png");
        stage.getIcons().add(image);
        stage.setTitle("Music Player");


        directoryField = new TextField();
        directoryField.setEditable(false);

        list = new ArrayList<>();

        mediaEntity = new MediaEntity();
        ComponentMethods componentMethods = new ComponentMethods();

        selectedTrack = new TextField();
        selectedTrack.setMaxWidth(250);
        selectedTrack.setEditable(false);

        searchField = new TextField();
        searchField.setPromptText("search");

        searchField.textProperty().addListener((observableValue, s, t1) -> {
            listView.getItems().clear();
            for (String i : trackList){
                if (i.toLowerCase().contains(searchField.getText())){
                    listView.getItems().add(i);
                }
            }
        });

        load = new Button();
        load.setText("load");

        load.setOnAction(actionEvent -> {
            if (!directoryField.getText().isEmpty()){

                componentMethods.load(Path.of(directoryField.getText()),listView);
                componentMethods.setListViewListener(listView,directoryField,selectedTrack, mediaEntity,timeSlider,sliderLabel);
                trackList = listView.getItems().stream().toList();
            }
        });

        play = new Button();
        play.setText("play");
        play.setOnAction(actionEvent -> {
            if (!selectedTrack.getText().isEmpty()){
                mediaEntity.getMediaPlayer().play();
            }
        });


        pause = new Button();
        pause.setText("pause");
        pause.setOnAction(actionEvent -> {
            if (!selectedTrack.getText().isEmpty()){
                mediaEntity.getMediaPlayer().pause();
            }
        });

        previous = new Button();
        previous.setText("prev");
        previous.setOnAction(actionEvent -> {
            if (!selectedTrack.getText().isEmpty()) {
                componentMethods.playPrevious(mediaEntity, listView, selectedTrack, directoryField, timeSlider, sliderLabel);
            }
        });

        next = new Button();
        next.setText("next");
        next.setOnAction(actionEvent -> {
            if (!selectedTrack.getText().isEmpty()) {
                componentMethods.playNext(mediaEntity, listView, selectedTrack, directoryField, timeSlider, sliderLabel);
            }
        });

        directoryHeader = new Label("Showing Tracks From");
        directoryHeader.setPadding(new Insets(5,5,0,0));
        sliderLabel = new Label("00:00");

        timeSlider = new Slider();
        timeSlider.setMinWidth(200);

        listView = new ListView<>();


        HBox directoryBox = new HBox();
        directoryBox.setPadding(new Insets(10,10,10,10));
        directoryBox.setSpacing(10);
        directoryBox.getChildren().addAll(directoryHeader,directoryField,load,searchField);

        HBox sliderBox = new HBox();
        sliderBox.setPadding(new Insets(10,10,10,10));
        sliderBox.setSpacing(10);
        sliderBox.getChildren().addAll(timeSlider,sliderLabel);
        sliderBox.setAlignment(Pos.CENTER);

        HBox mediaButtons = new HBox();
        mediaButtons.setSpacing(10);
        mediaButtons.setPadding(new Insets(10,10,10,10));
        mediaButtons.getChildren().addAll(previous,play,pause,next);
        mediaButtons.setAlignment(Pos.CENTER);

        VBox topLayout = new VBox();
        topLayout.setPadding(new Insets(10,10,10,10));
        topLayout.setSpacing(10);
        topLayout.getChildren().addAll(componentMethods.createMenuBar(stage,directoryField),directoryBox);

        VBox centerLayout = new VBox();
        centerLayout.getChildren().addAll(listView);

        VBox bottomLayout = new VBox();
        bottomLayout.setPadding(new Insets(10,10,10,10));
        bottomLayout.setSpacing(10);
        bottomLayout.getChildren().addAll(selectedTrack,sliderBox,mediaButtons);
        bottomLayout.setAlignment(Pos.CENTER);

        borderPane = new BorderPane();
        borderPane.setTop(topLayout);
        borderPane.setCenter(centerLayout);
        borderPane.setBottom(bottomLayout);

        scene = new Scene(borderPane);
        scene.getStylesheets().add("styles.css");

        stage.setScene(scene);
        stage.show();
    }
}
