package com.mycompany.graphalgorithmtask2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\WINCHESTER\\Downloads\\in1.txt"));

            //ilk satır
            String firstLine = reader.readLine();
            String[] firstLineTokens = firstLine.split(" ");
            int cities = Integer.parseInt(firstLineTokens[0]);
            int roads = Integer.parseInt(firstLineTokens[1]);

            int[][] capacityGraph = new int[cities][cities];// her yolun tasıma kapasitisi
            int[][] distances = new int[cities][cities]; //yol unzunlugu maliyeti

            // Yol verilerini dosyadan okuyarak matrisleri dolduralım
            for (int i = 0; i < roads; i++) {
                String line = reader.readLine();
                String[] tokens = line.split(" ");
                int from = Integer.parseInt(tokens[0]) - 1;
                int to = Integer.parseInt(tokens[1]) - 1; 
                int capacity = Integer.parseInt(tokens[2]);
                int distance = Integer.parseInt(tokens[3]);

                capacityGraph[from][to] = capacity;
                distances[from][to] = distance;
            }

            reader.close(); 

            FlowNetworks flowNetwork = new FlowNetworks(0, cities - 1, capacityGraph, distances);

            
            while (flowNetwork.findShortestAugmentingPath()) {
            }

            System.out.println("Maximum number of trucks that can go from city 1 to city " + cities + " in one day: " + flowNetwork.getMaxFlow());

            // Minimum mesafeli yolu hesapla
            int minDistance = Integer.MAX_VALUE;
            List<Integer> minDistancePath = null;

            for (List<Integer> path : flowNetwork.getUsedPaths()) {
                int pathDistance = 0;
                for (int i = 0; i < path.size() - 1; i++) {
                    int from = path.get(i);
                    int to = path.get(i + 1);
                    pathDistance += distances[from][to]; // Mesafeleri topluyoruz
                }
                if (pathDistance < minDistance) {
                    minDistance = pathDistance;
                    minDistancePath = path;
                }
            }

            
            System.out.println("Minimum distance path: " + minDistancePath);
            System.out.println("Minimum distance: " + minDistance);
            flowNetwork.getMinCostPath(distances, capacityGraph);


        } catch (IOException e) {
            System.out.println("file not exist");
            e.printStackTrace();
        }
    }
}
