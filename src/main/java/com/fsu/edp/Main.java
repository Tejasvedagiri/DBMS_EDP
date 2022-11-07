package com.fsu.edp;

import com.fsu.edp.engine.Engine;
import com.fsu.edp.model.Index;
import com.fsu.edp.model.Partation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        Long numOfLabels = Long.valueOf(args[1]);
        Long numOfVertices = Long.valueOf(args[2]);
        Long numOfQueries = 100L;

        Random rand = new Random();

        Index index = Engine.runAlgorithmOne(fileName, numOfLabels);

        Set<Long> labels = new HashSet<>();
        for(int i=0;i<numOfQueries;i++){
            int randomLabelNum = rand.nextInt(Math.toIntExact(numOfLabels));
            //while(labels.size() < (numOfLabels / 2)){
            labels.add(Long.valueOf(randomLabelNum));
            //}
            System.out.println("Partations ==> " + labels);
            Long src = Long.valueOf(rand.nextInt(Math.toIntExact(numOfVertices)));
            while(index.getMinPr(src, labels) == Long.MAX_VALUE){
                src = Long.valueOf(rand.nextInt(Math.toIntExact(numOfVertices)));
            }
            Long dst = Long.valueOf(rand.nextInt(Math.toIntExact(numOfVertices)));
            System.out.println("finding route between src ==> " +src + " target ==> " + dst + " in labels ==> " + labels);
            Long cost = Engine.runAlgorithTwo(index, src, dst, labels);
            if(cost == Long.MAX_VALUE){
                System.out.println("Could not find route between src ==> " +src + " target ==> " + dst);
            }else{
                System.out.println("Found route between src ==> " +src + " target ==> " + dst + " in label ==> " + labels);
                System.out.println("Finished cost is " + cost);
            }
            Long totalEntries = 0L;
            for(long j=0; j< numOfLabels;j++){
                totalEntries += index.getPartition(j).getCostSize();
            }
            System.out.println("There are " + totalEntries + " in Index");
//            Set<Long> a = new HashSet<>();
//            a.add(0L);
//            Long newCost = Engine.runAlgorithTwo(index, 1L, 9L,labels );
//            System.out.println(newCost);


        }

    }
}