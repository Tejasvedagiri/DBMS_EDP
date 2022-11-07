package com.fsu.edp.model;

public class DistanceVertexPair {
    Long distance;
    Long vertex;

    public DistanceVertexPair(Long distance, Long vertex) {
        this.distance = distance;
        this.vertex = vertex;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getVertex() {
        return vertex;
    }

    public void setVertex(Long vertex) {
        this.vertex = vertex;
    }

    @Override
    public String toString() {
        return "DistanceVertexPair{" +
                "distance=" + distance +
                ", vertex=" + vertex +
                '}';
    }
}
