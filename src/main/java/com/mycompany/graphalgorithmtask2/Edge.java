package com.mycompany.graphalgorithmtask2;

public class Edge {

    int to;
    int capacity; //max kamyon 
    int cost;//yol uzun

    public Edge(int to, int capacity, int cost) {
        this.to = to;
        this.capacity = capacity;
        this.cost = cost;
    }

}
