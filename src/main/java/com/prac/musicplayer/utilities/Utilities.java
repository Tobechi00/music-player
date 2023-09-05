package com.prac.musicplayer.utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Path;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utilities {
    public static ObservableList<String> listFilesForFolder(Path path) {
        File folder = path.toFile();
        return FXCollections.observableList(Stream.of(folder.listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList()));
    }

    public static File combine(Path path1, Path path2) {
        File file = new File(path1.toUri());

        return new File(file, path2.toString());
    }

    public static String millsToMins(long time) {
        Format minFormat = new SimpleDateFormat("mm:ss");
        return minFormat.format(time);
    }
}
