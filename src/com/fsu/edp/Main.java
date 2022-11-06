package com.fsu.edp;

import com.fsu.edp.model.Index;
import com.fsu.edp.model.Partation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String fileName = "data/data.txt";
        Long numOfLabels = 5L;
        Index index = new Index();
        for (Long i = 0L; i < numOfLabels; ++i) {
            index.createPartition(i);
        }
        Map<Long, Set<Long>> otherHostsMap = new HashMap<>();
        List<Long> src = new ArrayList<>();
        List<Long> dst = new ArrayList<>();
        List<Long> weight = new ArrayList<>();
        List<Long> label = new ArrayList<>();

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
        for (int i = 0; i < src.size(); i++) {
            if (!otherHostsMap.containsKey(src.get(i))) {
                Set<Long> hostMap = new HashSet<>();
                otherHostsMap.put(src.get(i), hostMap);
            }
            otherHostsMap.get(src.get(i)).add(label.get(i));
        }
        System.out.println(otherHostsMap);
        System.out.println("First Scan completed");
        for (int i = 0; i < src.size(); i++) {
            Partation par = index.getPartition(label.get(i));
            Long cur_src = src.get(i);
            Long cur_dst = dst.get(i);
            Long cur_label = label.get(i);
            if (!par.contains(cur_src)) {
                addVertex(par, cur_src, cur_label, otherHostsMap);
            }
            if (!par.contains(cur_dst)) {
                addVertex(par, cur_dst, cur_label, otherHostsMap);
            }
            System.out.println(par);
        }
    }

    private static void addVertex(Partation par, Long src, Long label, Map<Long, Set<Long>> otherHostsMap) {
        boolean bridge = false;
        List<Long> otherHost = new ArrayList<>();
        for (Long labelKey : otherHostsMap.get(src)) {
            System.out.println("Label key for label " + labelKey + " "+ label + " " + src);
            if (label != labelKey) {
                otherHost.add(labelKey);
            }
        }
        if (!otherHost.isEmpty()) {
            bridge = true;
        }
        par.addVertex(src, bridge, otherHost);
        System.out.println("Added vertex " + src + " to partition " + label + " with other hosts");
        for(Long other: par.getVertex(src).getOtherHost()){
            System.out.println(other);
        }
    }
}