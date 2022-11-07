package com.fsu.edp.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReaderUtil {

    public static final void readFile(String fileName, List<Long> src, List<Long> dst, List<Long> weight, List<Long> label){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(e -> {
                String[] data = e.split(" ");
                src.add(Long.valueOf(data[0]));
                dst.add(Long.valueOf(data[1]));
                weight.add(Long.valueOf(data[2]));
                label.add(Long.valueOf(data[3]));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
