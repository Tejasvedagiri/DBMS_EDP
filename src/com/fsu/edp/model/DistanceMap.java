package com.fsu.edp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DistanceMap {
    Long label;
    Long dst;

    private int hashCode;

    public DistanceMap(Long label, Long dst) {
        this.label = label;
        this.dst = dst;
        this.hashCode = Objects.hash(label, dst);
    }

    @Override
    public String toString() {
        return "DistanceMap{" +
                "label=" + label +
                ", dst=" + dst +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DistanceMap that = (DistanceMap) o;
        return label == that.label && dst == that.dst;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
