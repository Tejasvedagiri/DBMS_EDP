package com.fsu.edp.model;

public class Edge {
    Long distance;
    Long weight;

    public Edge(Long distance, Long weight){
        this.distance = distance;
        this.weight = weight;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "distance=" + distance +
                ", weight=" + weight +
                '}';
    }
}
