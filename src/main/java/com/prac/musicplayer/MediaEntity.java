package com.prac.musicplayer;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaEntity {
    private MediaPlayer mediaPlayer;
    private Media media;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
