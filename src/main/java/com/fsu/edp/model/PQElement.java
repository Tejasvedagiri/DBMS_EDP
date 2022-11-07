package com.fsu.edp.model;

public class PQElement {
    Long label;
    Long dst;
    Long cost;

    public PQElement(Long label, Long dst, Long cost){
        this.label = label;
        this.dst = dst;
        this.cost = cost;
    }

    public Long getLabel() {
        return label;
    }

    public void setLabel(Long label) {
        this.label = label;
    }

    public Long getDst() {
        return dst;
    }

    public void setDst(Long dst) {
        this.dst = dst;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "PQElement{" +
                "label=" + label +
                ", dst=" + dst +
                ", cost=" + cost +
                '}';
    }
    public boolean compare(PQElement pqe2){
        return this.cost > pqe2.getCost();
    }
}
