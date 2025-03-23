package com.mycompany.graphalgorithmtask2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class FlowNetworks {

    private ArrayList<ArrayList<Edge>> graph;
    private int vertices;
    private int source;
    private int sink;
    private int maxFlow;//maximum tasınabilecek kamyon sayisi
    private List<List<Integer>> usedPaths; // kullanılan yollar 

    public FlowNetworks(int source, int sink, int[][] capacityGraph, int[][] distances) {
        this.source = source;
        this.sink = sink;
        this.vertices = capacityGraph.length;
        this.maxFlow = 0;
        this.usedPaths = new ArrayList<>();
        this.graph = new ArrayList<>(vertices);
        
        
        for (int i = 0; i < vertices; i++) { //hersehir baglantili oldugu edge tutan liste
            this.graph.add(new ArrayList<>());
        }

        // doldur edge ile
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (capacityGraph[i][j] != 0) {
                    this.graph.get(i).add(new Edge(j, capacityGraph[i][j], distances[i][j]));
                }
            }
        }
    }

    
    public boolean findShortestAugmentingPath() {
        int[] dist = new int[vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        int[] parent = new int[vertices]; //hangi dugumden geldigimizi saklar 
        Arrays.fill(parent, -1);

        boolean[] inQueue = new boolean[vertices]; //sehri iki kere eklememk icin
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(v -> dist[v]));//en kısave az maliyet mesafete sahipsehir onceisliyo
        queue.add(source);
        inQueue[source] = true;
        
        //check en kısa mesafesehir,gereksiz hesap onluyo
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            inQueue[currentNode] = false;

            for (Edge edge : graph.get(currentNode)) {
                if (edge.capacity > 0 && dist[currentNode] + edge.cost < dist[edge.to]) {
                    dist[edge.to] = dist[currentNode] + edge.cost;
                    parent[edge.to] = currentNode;

                    if (!inQueue[edge.to]) {
                        queue.add(edge.to);
                        inQueue[edge.to] = true;
                    }
                }
            }
        }

        if (dist[sink] == Integer.MAX_VALUE) { //hedef sehir
            return false; //yol bulunmadı
        }

        // min kapasiteyi bul-bottleneck, v su an sehir a vden once
        int pathFlow = Integer.MAX_VALUE;
        for (int v = sink; v != source; v = parent[v]) {
            int a = parent[v];
            for (Edge edge : graph.get(a)) {
                if (edge.to == v) {
                    pathFlow = Math.min(pathFlow, edge.capacity);
                }
            }
        }

        // güncelle residualgraphı
        List<Integer> currentPath = new ArrayList<>();
        for (int v = sink; v != source; v = parent[v]) {
            int b = parent[v];
            
            currentPath.add(v); 
            for (Edge edge : graph.get(b)) {
                if (edge.to == v) {
                    edge.capacity -= pathFlow; //residual kapasite azaldı
                }
            }

            // reverse yol ekle
            boolean reverseFound = false;
            for (Edge edge : graph.get(v)) {
                if (edge.to == b) {
                    edge.capacity += pathFlow;
                    reverseFound = true;
                    break;
                }
            }
            if (!reverseFound) {
                graph.get(v).add(new Edge(b, pathFlow, -1 * getEdgeCost(b, v)));
            }
        }
        currentPath.add(source); 
        Collections.reverse(currentPath); 
        usedPaths.add(currentPath); 

        maxFlow += pathFlow; 
        return true;
    }

    //yardımcı method 
    private int getEdgeCost(int v1, int v2) {
        for (Edge edge : graph.get(v1)) {
            if (edge.to == v2) {
                return edge.cost;
            }
        }
        return Integer.MAX_VALUE;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    

    public List<List<Integer>> getUsedPaths() {
        return usedPaths;
    }
    
    public List<Integer> getMinCostPath(int[][] distances, int[][] capacityGraph) {
    int minCost = Integer.MAX_VALUE;
    List<Integer> minCostPath = null;

    for (List<Integer> path : usedPaths) {
        int pathCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            pathCost += distances[from][to] * capacityGraph[from][to]; 
        }
        if (pathCost < minCost) {
            minCost = pathCost;
            minCostPath = path;
        }
    }

    System.out.println("min cost== " + minCost);
    return minCostPath;
}

    
}
