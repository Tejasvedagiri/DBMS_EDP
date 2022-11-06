package com.fsu.edp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Index {

    private Map<Long, Partation> partitions = new HashMap<>();

    public Partation getPartition(Long label){
        return partitions.get(label);
    }
    public List<Long> getOtherHosts(Long label, Long vertexId){
        return partitions.get(label).getVertex(vertexId).getOtherHost();
    }
    public Long getMinPr(Long src, Set<Long> labels){
        //TO-DO
        Long min = Long.MAX_VALUE;
        Long label = Long.MAX_VALUE;
        for (Long key: partitions.keySet()) {
            Partation val = partitions.get(key);
            if(val.contains(src) && labels.contains(key)){
                for(Edge edgeKey: val.getEdge(src)){
                    if(edgeKey.getWeight() < min){
                        min = edgeKey.getWeight();
                        label = key;
                    }

                }

            }

        }
        return label;
    }
    public boolean isBridge(Long label, Long vertexId) {
        return partitions.get(label).getVertex(vertexId).isBridge();
    }
    public void createPartition(Long label){
        partitions.put(label, new Partation());
    }
    public Long getCost(Long label, Long src, Long dst){
        return partitions.get(label).getCost(src, dst);
    }
    public boolean containsCost(Long label, Long src, Long dst){
        return partitions.get(label).containsCost(src, dst);
    }
    public List<Long> getBridgeEdges(long label, long vertexId){
        return partitions.get(label).getVertex(vertexId).getBridgeEdge();
    }

    @Override
    public String toString() {
        return "Index{" +
                "partitions=" + partitions +
                '}';
    }
}
