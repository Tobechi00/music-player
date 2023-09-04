package com.prac.musicplayer;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Path;

public class ComponentMethods {
    public MenuBar createMenuBar(Stage stage, TextField textField){
        MenuBar menuBar = new MenuBar();
        Menu selectMenu = new Menu();
        MenuItem fileItem = new MenuItem();


        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("select your music folder");

        Menu convertMenu = new Menu();
        convertMenu.setText("Convert");

        Menu functions = new Menu();
        functions.setText("Functions");

        selectMenu.setText("File");
        fileItem.setText("Choose Folder");
        fileItem.setOnAction(actionEvent -> textField.setText(chooser.showDialog(stage).toString()));

        selectMenu.getItems().addAll(fileItem);
        menuBar.getMenus().addAll(selectMenu,convertMenu,functions);

        return menuBar;
    }
    public void Load(Path path,ListView<String> listView){

        listView.setItems(Utilities.listFilesForFolder(path));
    }
    public void setListViewListener(ListView<String> listView, TextField directory, TextField selectedTrack, Entity entity,Slider slider,Label sliderLabel ){
        listView.setOnMouseClicked(mouseEvent -> {
            if (selectedTrack.getText().isEmpty()){
                  selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
                  entity.setMedia(new Media(Utilities.combine(Path.of(directory.getText()),Path.of(selectedTrack.getText())).toURI().toString()));
                  entity.setMediaPlayer(new MediaPlayer(entity.getMedia()));
            }else {
                  entity.getMediaPlayer().dispose();
                  selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
                  entity.setMedia(new Media(Utilities.combine(Path.of(directory.getText()),Path.of(selectedTrack.getText())).toURI().toString()));
                  entity.setMediaPlayer(new MediaPlayer(entity.getMedia()));
            }
            entity.getMediaPlayer().setOnReady(() -> setSlider(entity,slider,sliderLabel,listView,selectedTrack,directory));
        });
    }

    public void setSlider(Entity entity,Slider slider,Label sliderLabel,ListView<String> listView,TextField selectedTrack,TextField directoryField) {
            entity.getMediaPlayer().setOnEndOfMedia(() -> playNext(entity,listView,selectedTrack,directoryField,slider,sliderLabel));
            slider.maxProperty().bind(Bindings.createDoubleBinding(
                    () -> entity.getMediaPlayer()
                            .getTotalDuration()
                            .toSeconds(),
                    entity.getMediaPlayer().totalDurationProperty()
            ));
            entity.getMediaPlayer().currentTimeProperty().addListener((observableValue, duration, t1) -> {
                slider.setValue(t1.toSeconds());
                sliderLabel.setText(Utilities.millsToMins((long) t1.toMillis()));
            });
            slider.setOnMouseDragged(mouseEvent -> entity.getMediaPlayer().seek(Duration.seconds(slider.getValue())));
        }

    public void playNext(Entity entity,ListView<String> listView,TextField selectedTrack,TextField directoryField,Slider slider,Label sliderLabel){
        entity.getMediaPlayer().dispose();

        listView.getSelectionModel().selectNext();
        selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
        entity.setMedia(new Media(Utilities.combine(Path.of(directoryField.getText()),Path.of(selectedTrack.getText())).toURI().toString()));
        entity.setMediaPlayer(new MediaPlayer(entity.getMedia()));
        entity.getMediaPlayer().setOnReady(() -> setSlider(entity,slider,sliderLabel,listView,selectedTrack,directoryField));
        entity.getMediaPlayer().play();
    }
    public void playPrevious(Entity entity,ListView<String> listView,TextField selectedTrack,TextField directoryField,Slider slider,Label sliderLabel){
        entity.getMediaPlayer().dispose();
        listView.getSelectionModel().selectPrevious();
        selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
        entity.setMedia(new Media(Utilities.combine(Path.of(directoryField.getText()),Path.of(selectedTrack.getText())).toURI().toString()));
        entity.setMediaPlayer(new MediaPlayer(entity.getMedia()));
        entity.getMediaPlayer().setOnReady(() -> setSlider(entity,slider,sliderLabel,listView,selectedTrack,directoryField));
        entity.getMediaPlayer().play();
    }

    }

