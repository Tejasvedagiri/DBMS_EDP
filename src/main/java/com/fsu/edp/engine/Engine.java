package com.fsu.edp.engine;

import com.fsu.edp.model.*;
import com.fsu.edp.utils.FileReaderUtil;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Engine {

    public static Index runAlgorithmOne(String inputPath, Long numberOfLabels){
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
        System.out.println("First Scan completed");
        for (int i = 0; i < src.size(); i++) {
            Partation par = index.getPartition(label.get(i));
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
    private static void addVertex(Partation par, Long src, Long label, Map<Long, Set<Long>> otherHostsMap) {
        boolean bridge = false;
        List<Long> otherHost = new ArrayList<>();
        if(otherHostsMap.containsKey(src)) {
            for (Long labelKey : otherHostsMap.get(src)) {
                if (label != labelKey) {
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
            if(pqe.getDst() == dst){
                return  pqe.getCost();
            }
//            if(pqe.getCost() == globalDistance.get(label).getSrcToDstDistance(src, cur_dst)){
//                return pqe.getCost();
//            }
            if(!index.containsCost(cur_label, cur_dst, dst)){
                Partation par = index.getPartition(cur_label);
                Map<Long, Long> distances = new HashMap<>();
                distances.put(cur_dst, 0L);

                Queue<DistanceVertexPair> djQ = new ConcurrentLinkedQueue<>();
                djQ.add(new DistanceVertexPair(0L, cur_dst));

                while(!djQ.isEmpty()){
                    DistanceVertexPair v = djQ.remove();
                    System.out.println("Internal: now visiting ==> "+ v.getVertex());
//                    System.out.println(distances.containsKey(v.getVertex()));
//                    System.out.println(v.getDistance() != distances.get(v.getVertex()));
                    if(true){
                        if(par.isBridge(v.getVertex()) && v.getVertex() != cur_dst){
                            par.addCost(cur_dst, v.getVertex(), distances.get(v.getVertex()));
                            par.getVertex(cur_dst).addBridgeEdge(v.getVertex());
                        }else if(v.getVertex() == dst){
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
                }
                if(!distances.containsKey(dst)){
                    par.addCost(cur_dst, dst, Long.MAX_VALUE);
                }
            }
            Long costToDistance = index.getCost(cur_label, cur_dst, dst);
            if(costToDistance != Long.MAX_VALUE){
                insertIntoQueue(pqElementQueue, cur_label, dst, cur_cost + costToDistance, globalDistance);
            }
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
