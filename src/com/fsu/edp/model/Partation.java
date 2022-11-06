package com.fsu.edp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Partation {
    private Map<Long, Vertex> vertices = new HashMap<>();
    private Map<Long, Map<Long, Long>> cost = new HashMap<>();

    public boolean contains(Long vertex_id){
        System.out.println(vertices);
        return vertices.containsKey(vertex_id);
    }
    public void addVertex(Long id, boolean isBridge, List<Long> otherHosts){
        Vertex vertex = new Vertex(id, isBridge, otherHosts);
        vertices.put(id, vertex);
    }
    public void addEdge(Long src, Long dst, Long weight){
        vertices.get(src).addEdge(dst, weight);
    }

    public boolean isBridge(Long vertexId){
        return vertices.get(vertexId).isBridge();
    }

    public Vertex getVertex(Long vertexId){
        return vertices.get(vertexId);
    }
    public List<Edge> getEdge(Long vertexId){
        return vertices.get(vertexId).getEdges();
    }
    public boolean containsCost(Long src, Long dst){
        return cost.get(src).containsKey(dst);
    }

    public void addCost(Long src, Long dst, Long distance){
        Map<Long,Long> disCost = null;
        if(!cost.containsKey(src)){
            disCost= new HashMap<>();
        }else{
            disCost = cost.get(src);
        }
        disCost.put(dst, distance);
        cost.put(src, disCost);
    }
    public Long getCost(Long src, Long dst){
        return cost.get(src).get(dst);
    }
    public long getCostSize(){
        return cost.size();
    }

    @Override
    public String toString() {
        return "Partation{" +
                "vertices=" + vertices +
                ", cost=" + cost +
                '}';
    }
}
