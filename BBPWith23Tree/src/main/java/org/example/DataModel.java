package org.example;


public class DataModel {
    public final int[] weights;
    public final int numItems ;
    public final int numBins;
    public final int binCapacity;

    public DataModel(int[] weights, int numItems, int numBins, int binCapacity) {
        this.weights = weights;
        this.numItems = numItems;
        this.numBins = numBins;
        this.binCapacity = binCapacity;
    }
}


