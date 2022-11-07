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

    public Long getVertex() {
        return vertex;
    }

    @Override
    public String toString() {
        return "DistanceVertexPair{" +
                "distance=" + distance +
                ", vertex=" + vertex +
                '}';
    }
}
