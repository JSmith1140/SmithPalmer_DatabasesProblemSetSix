package ProblemSetSix.BLL;

import ProblemSetSix.DAL.CampusGraphProvider;
import java.util.*;

public class CampusGraphBLL {

    public static class Edge {
        public int to;
        public int time;

        public Edge(int to, int time) {
            this.to = to;
            this.time = time;
        }
    }

    private Map<Integer, List<Edge>> buildGraph(Map<Integer, List<CampusGraphProvider.Edge>> records) {
        Map<Integer, List<Edge>> graph = new HashMap<>();

        for (Map.Entry<Integer, List<CampusGraphProvider.Edge>> entry : records.entrySet()) {

            int from = entry.getKey();

            graph.putIfAbsent(from, new ArrayList<>());

            for (CampusGraphProvider.Edge e : entry.getValue()) {
                graph.get(from).add(new Edge(e.to, e.time));
            }
        }

        return graph;
    }

    public int getShortestTravelTime(int start, int end) throws Exception {

        CampusGraphProvider provider = new CampusGraphProvider();

        Map<Integer, List<CampusGraphProvider.Edge>> records = provider.getGraph();

        Map<Integer, List<Edge>> graph = buildGraph(records);

        Map<Integer, Integer> dist = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        pq.add(new int[]{start, 0});
        dist.put(start, 0);

        while (!pq.isEmpty()) {

            int[] curr = pq.poll();
            int node = curr[0];
            int currentDist = curr[1];

            if (node == end) return currentDist;

            if (!graph.containsKey(node)) continue;

            for (Edge edge : graph.get(node)) {

                int newDist = currentDist + edge.time;

                if (!dist.containsKey(edge.to) || newDist < dist.get(edge.to)) {
                    dist.put(edge.to, newDist);
                    pq.add(new int[]{edge.to, newDist});
                }
            }
        }

        return Integer.MAX_VALUE;
    }
}