module com.prac.musicplayer {
    requires javafx.controls;
    requires javafx.media;


    opens com.prac.musicplayer to javafx.fxml;
    exports com.prac.musicplayer;
    exports com.prac.musicplayer.controller;
    opens com.prac.musicplayer.controller to javafx.fxml;
    exports com.prac.musicplayer.view;
    opens com.prac.musicplayer.view to javafx.fxml;
    exports com.prac.musicplayer.utilities;
    opens com.prac.musicplayer.utilities to javafx.fxml;
}