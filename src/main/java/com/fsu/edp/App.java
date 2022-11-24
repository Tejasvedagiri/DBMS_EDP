package com.fsu.edp;

import com.fsu.edp.engine.Engine;
import com.fsu.edp.model.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        logger.info("Starting EDP");

        String fileName = args[0];
        long numOfLabels = Long.parseLong(args[1]);
        long numOfVertices = Long.parseLong(args[2]);
        long numOfQueries = 100L;

        logger.info("Received args FileName ==> {}", fileName);
        logger.info("Number of Labels/Partitions ==> {}", numOfVertices);
        Random rand = new Random();

        logger.info("Running AlgorithmOne from paper");
        Index index = Engine.runAlgorithmOne(fileName, numOfLabels);

        Set<Long> labels = new HashSet<>();
        for(int i=0;i<numOfQueries;i++){
            int randomLabelNum = rand.nextInt(Math.toIntExact(numOfLabels));
            //while(labels.size() < (numOfLabels / 2)){
            labels.add((long) randomLabelNum);
            //}
            logger.info("Loaded partition ==> {}", labels);
            Long src = (long) rand.nextInt(Math.toIntExact(numOfVertices));
            while(index.getMinPr(src, labels) == Long.MAX_VALUE){
                src = (long) rand.nextInt(Math.toIntExact(numOfVertices));
            }
            Long dst = (long) rand.nextInt(Math.toIntExact(numOfVertices));
            logger.info("Finding route between src ==> {} target ==> {} in labels ==> {}", src, dst, labels);
            Long cost = Engine.runAlgorithmTwo(index, src, dst, labels);
            if(cost == Long.MAX_VALUE){
                logger.info("Could not find route between src ==> {} target ==> {}", src, dst);
            }else{
                logger.info("Found route between src ==> {} target ==> {} with cost ==> {}", src, dst, cost);
            }
            long totalEntries = 0L;
            for(long j=0; j< numOfLabels;j++){
                totalEntries += index.getPartition(j).getCostSize();
            }
            logger.info("Total number of entries in Index ==> {}", totalEntries);

        }
        System.out.println(Engine.runAlgorithmTwo(index, 1L, 9L, labels));
    }
}