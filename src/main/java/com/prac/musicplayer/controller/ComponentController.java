package com.prac.musicplayer.controller;

import com.prac.musicplayer.utilities.Utilities;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.List;

public class ComponentController {
    public MenuBar createMenuBar(Stage stage, TextField textField) {
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
        menuBar.getMenus().addAll(selectMenu, convertMenu, functions);

        return menuBar;
    }

    public void load(Path path, ListView<String> listView) {

        listView.setItems(Utilities.listFilesForFolder(path));
    }

    public void setListViewListener(ListView<String> listView, TextField directory, TextField selectedTrack, MediaEntity mediaEntity, Slider slider, Label sliderLabel) {
        listView.setOnMouseClicked(mouseEvent -> {
            if (selectedTrack.getText().isEmpty()) {
                selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
                mediaEntity.setMediaPlayer(new MediaPlayer(new Media(Utilities.combine(Path.of(directory.getText()), Path.of(selectedTrack.getText())).toURI().toString())));
            } else {
                mediaEntity.getMediaPlayer().dispose();
                selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
                mediaEntity.setMediaPlayer(new MediaPlayer(new Media(Utilities.combine(Path.of(directory.getText()), Path.of(selectedTrack.getText())).toURI().toString())));
            }
            mediaEntity.getMediaPlayer().setOnReady(() -> setSlider(mediaEntity, slider, sliderLabel, listView, selectedTrack, directory));
        });
    }

    public void setSlider(MediaEntity mediaEntity, Slider slider, Label sliderLabel, ListView<String> listView, TextField selectedTrack, TextField directoryField) {
        mediaEntity.getMediaPlayer().setOnEndOfMedia(() -> playNext(mediaEntity, listView, selectedTrack, directoryField, slider, sliderLabel));
        slider.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mediaEntity.getMediaPlayer()
                        .getTotalDuration()
                        .toSeconds(),
                mediaEntity.getMediaPlayer().totalDurationProperty()
        ));
        mediaEntity.getMediaPlayer().currentTimeProperty().addListener((observableValue, duration, t1) -> {
            slider.setValue(t1.toSeconds());
            sliderLabel.setText(Utilities.millsToMins((long) t1.toMillis()));
        });
        slider.setOnMouseDragged(mouseEvent -> mediaEntity.getMediaPlayer().seek(Duration.seconds(slider.getValue())));
    }

    public void playNext(MediaEntity mediaEntity, ListView<String> listView, TextField selectedTrack, TextField directoryField, Slider slider, Label sliderLabel) {
        mediaEntity.getMediaPlayer().dispose();

        listView.getSelectionModel().selectNext();
        selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
        mediaEntity.setMediaPlayer(new MediaPlayer(new Media(Utilities.combine(Path.of(directoryField.getText()), Path.of(selectedTrack.getText())).toURI().toString())));
        mediaEntity.getMediaPlayer().setOnReady(() -> setSlider(mediaEntity, slider, sliderLabel, listView, selectedTrack, directoryField));
        mediaEntity.getMediaPlayer().play();
    }

    public void playPrevious(MediaEntity mediaEntity, ListView<String> listView, TextField selectedTrack, TextField directoryField, Slider slider, Label sliderLabel) {
        mediaEntity.getMediaPlayer().dispose();
        listView.getSelectionModel().selectPrevious();
        selectedTrack.setText(listView.getSelectionModel().getSelectedItem());
        mediaEntity.setMediaPlayer(new MediaPlayer(new Media(Utilities.combine(Path.of(directoryField.getText()), Path.of(selectedTrack.getText())).toURI().toString())));
        mediaEntity.getMediaPlayer().setOnReady(() -> setSlider(mediaEntity, slider, sliderLabel, listView, selectedTrack, directoryField));
        mediaEntity.getMediaPlayer().play();
    }

    public void search(ListView<String> listView, List<String> trackList, TextField searchField) {
        listView.getItems().clear();
        for (String i : trackList) {
            if (i.toLowerCase().contains(searchField.getText().toLowerCase())) {
                listView.getItems().add(i);
            }
        }
    }

    public List<String> copyList(ListView<String> listView) {
        return listView.getItems().stream().toList();
    }
}

