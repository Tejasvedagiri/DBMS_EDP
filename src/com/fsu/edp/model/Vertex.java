package com.fsu.edp.model;

import java.util.List;

public class Vertex {
    private long id;
    private boolean bridge;
    private List<Long> otherHost;
    private List<Long> bridgeEdge;
    private List<Edge> edges;

    Vertex(long id, boolean bridge, List<Long> otherHost){
        this.id = id;
        this.bridge = bridge;
        this.otherHost = otherHost;
    }

    public long getId() {
        return id;
    }

    public boolean isBridge() {
        return bridge;
    }

    public List<Long> getOtherHost() {
        return otherHost;
    }

    public List<Long> getBridgeEdge() {
        return bridgeEdge;
    }

    public void addBridgeEdge(long distance){
        this.bridgeEdge.add(distance);

    }

    public void addEdge(long distance, long weight) {
        Edge e = new Edge(distance, weight);
        this.edges.add(e);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", bridge=" + bridge +
                ", otherHost=" + otherHost +
                ", bridgeEdge=" + bridgeEdge +
                ", edges=" + edges +
                '}';
    }
}
