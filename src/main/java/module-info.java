module com.prac.musicplayer {
    requires javafx.controls;
    requires javafx.media;


    opens com.prac.musicplayer to javafx.fxml;
    exports com.prac.musicplayer;
}