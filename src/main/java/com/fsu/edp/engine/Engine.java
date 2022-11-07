package com.fsu.edp.engine;

import com.fsu.edp.model.*;
import com.fsu.edp.utils.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Engine {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    public static Index runAlgorithmOne(String inputPath, Long numberOfLabels){
        logger.info("Creating a index map");
        Index index = new Index();

        Map<Long, Set<Long>> otherHostsMap = new HashMap<>();
        List<Long> src = new ArrayList<>();
        List<Long> dst = new ArrayList<>();
        List<Long> weight = new ArrayList<>();
        List<Long> label = new ArrayList<>();

        FileReaderUtil.readFile(inputPath, src, dst, weight, label);

        for (Long i = 0L; i < numberOfLabels; ++i) {
            index.createPartition(i);
        }

        for (int i = 0; i < src.size(); i++) {
            if (!otherHostsMap.containsKey(src.get(i))) {
                Set<Long> hostMap = new HashSet<>();
                otherHostsMap.put(src.get(i), hostMap);
            }
            otherHostsMap.get(src.get(i)).add(label.get(i));
        }
        logger.info("First Scan completed");
        for (int i = 0; i < src.size(); i++) {
            Partition par = index.getPartition(label.get(i));
            Long cur_src = src.get(i);
            Long cur_dst = dst.get(i);
            Long cur_label = label.get(i);
            Long cur_weight = weight.get(i);
            if (!par.contains(cur_src)) {
                addVertex(par, cur_src, cur_label, otherHostsMap);
            }
            if (!par.contains(cur_dst)) {
                addVertex(par, cur_dst, cur_label, otherHostsMap);
            }
            par.addEdge(cur_src, cur_dst, cur_weight);
        }
        return index;
    }
    private static void addVertex(Partition par, Long src, Long label, Map<Long, Set<Long>> otherHostsMap) {
        boolean bridge = false;
        List<Long> otherHost = new ArrayList<>();
        if(otherHostsMap.containsKey(src)) {
            for (Long labelKey : otherHostsMap.get(src)) {
                if (!Objects.equals(label, labelKey)) {
                    otherHost.add(labelKey);
                }
            }
        }
        if (!otherHost.isEmpty()) {
            bridge = true;
        }
        par.addVertex(src, bridge, otherHost);
    }

    public static Long runAlgorithTwo(Index index, Long src, Long dst, Set<Long> labels) {
        logger.info("Running algorithm two from paper");
        Queue<PQElement> pqElementQueue = new ConcurrentLinkedQueue<>();
        Map<DistanceMap, Long> globalDistance = new HashMap<>();
        Long minPtr = index.getMinPr(src, labels);
        if (minPtr == Long.MAX_VALUE){
            return Long.MAX_VALUE;
        }
        insertIntoQueue(pqElementQueue, minPtr, src, 0L, globalDistance);

        while(!pqElementQueue.isEmpty()){
            PQElement pqe = pqElementQueue.remove();

            Long cur_label = pqe.getLabel();
            Long cur_dst = pqe.getDst();
            Long cur_cost = pqe.getCost();
            DistanceMap key = new DistanceMap(cur_label, cur_dst);
            if(cur_cost > globalDistance.get(key)){
                continue;
            }
            if(Objects.equals(pqe.getDst(), dst)){
                return  pqe.getCost();
            }
//            if(Objects.equals(pqe.getCost(), globalDistance.get(key))){
//                logger.info("Found pre computed distance from Distance Map");
//                return pqe.getCost();
//            }
            if(!index.containsCost(cur_label, cur_dst, dst)){
                Partition par = index.getPartition(cur_label);
                Map<Long, Long> distances = new HashMap<>();
                distances.put(cur_dst, 0L);

                Queue<DistanceVertexPair> djQ = new ConcurrentLinkedQueue<>();
                djQ.add(new DistanceVertexPair(0L, cur_dst));

                while(!djQ.isEmpty()){
                    DistanceVertexPair v = djQ.remove();
                    logger.debug("Visiting Node ==> {}", v.getVertex());
                    if(par.isBridge(v.getVertex()) && !Objects.equals(v.getVertex(), cur_dst)){
                        par.addCost(cur_dst, v.getVertex(), distances.get(v.getVertex()));
                        par.getVertex(cur_dst).addBridgeEdge(v.getVertex());
                    }else if(Objects.equals(v.getVertex(), dst)){
                        par.addCost(cur_dst, v.getVertex(), distances.get(v.getVertex()));
                    }
                    for(Edge e: par.getEdge(v.getVertex())){
                        Long newWeight = v.getDistance() + e.getWeight();
                        if(!distances.containsKey(e.getDst()) || distances.get(e.getDst()) > newWeight ){
                            distances.put(e.getDst(), newWeight);
                            djQ.add(new DistanceVertexPair(newWeight, e.getDst()));
                        }
                    }
                }
                if(!distances.containsKey(dst)){
                    par.addCost(cur_dst, dst, Long.MAX_VALUE);
                }
            }
            Long costToDistance = index.getCost(cur_label, cur_dst, dst);
            if(costToDistance != Long.MAX_VALUE){
                insertIntoQueue(pqElementQueue, cur_label, dst, cur_cost + costToDistance, globalDistance);
            }
            //Check other hosts
            for(Long bridge: index.getBridgeEdges(cur_label, cur_dst)){
                Long costToBridge = index.getCost(cur_label, cur_dst, bridge);
                for(Long otherHostLabels: index.getOtherHosts(cur_label, bridge)){
                    if(labels.contains(otherHostLabels)){
                        insertIntoQueue(pqElementQueue, otherHostLabels, bridge, cur_cost+costToBridge, globalDistance);
                    }
                }
            }
            if(index.isBridge(cur_label, cur_dst)){
                for(Long otherLabel: index.getOtherHosts(cur_label, cur_dst)){
                    if(labels.contains(otherLabel)){
                        insertIntoQueue(pqElementQueue, otherLabel, cur_dst, cur_cost, globalDistance);
                    }
                }

            }
        }

        return Long.MAX_VALUE;
    }

    private static void insertIntoQueue(Queue<PQElement> pqElementQueue, Long label, Long dst, Long distance, Map<DistanceMap, Long> globalDistance) {
        DistanceMap key = new DistanceMap(label, dst);
        if(!globalDistance.containsKey(key) || globalDistance.get(key) > distance){
            PQElement pqe = new PQElement(label, dst, distance);
            pqElementQueue.add(pqe);
            globalDistance.put(key, distance);
        }
    }
}
