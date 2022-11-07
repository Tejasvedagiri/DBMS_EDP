package com.fsu.edp.model;

public class Edge {
    Long dst;
    Long weight;

    public Edge(Long dst, Long weight){
        this.dst = dst;
        this.weight = weight;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public long getDst() {
        return dst;
    }

    public void setDst(Long dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "dst=" + dst +
                ", weight=" + weight +
                '}';
    }
}
