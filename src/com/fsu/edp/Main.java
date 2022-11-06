package com.fsu.edp;

import com.fsu.edp.model.Index;
import com.fsu.edp.model.Partation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String fileName = "data/data.txt";
        Long numOfLabels = 100L;
        Index index = new Index();
        for (Long i = 0L; i < numOfLabels; ++i){
            index.createPartition(i);
        }
        Map<Long, Set<Long>> otherHostsMap = new HashMap<>();
        System.out.println(index);
        try(Stream<String> stream = Files.lines(Path.of(fileName))){
            stream.forEach(e -> {
                String[] data = e.split(" ");
                Set<Long> labels = null;
                Long src = Long.valueOf(data[0]);
                Long label = Long.valueOf(data[1]);
                if(!otherHostsMap.containsKey(src)){
                    labels = new HashSet<>();
                    labels.add(label);
                    otherHostsMap.put(Long.valueOf(data[0]), labels);
                }else{
                    otherHostsMap.get(src).add(label);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(otherHostsMap);
        Partation par = index.getPartition(1L);

    }
}