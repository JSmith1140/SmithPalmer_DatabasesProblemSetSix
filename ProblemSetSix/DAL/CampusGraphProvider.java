package ProblemSetSix.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class CampusGraphProvider {

    public static class Edge {
        public int to;
        public int time;

        // constructor
        public Edge(int to, int time) {
            this.to = to;
            this.time = time;
        }
    }

    /**
     * Method to get graph
     * @return
     * @throws SQLException
     */
    public Map<Integer, List<Edge>> getGraph() throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT from_dorm_id, to_dorm_id, travel_time FROM campus_edges";

        Map<Integer, List<Edge>> graph = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int from = rs.getInt("from_dorm_id");
                int to = rs.getInt("to_dorm_id");
                int time = rs.getInt("travel_time");

                graph.putIfAbsent(from, new ArrayList<>());
                graph.get(from).add(new Edge(to, time));
            }
        }

        return graph;
    }

    /**
     * Method to get shortest travel time
     * @param start
     * @param end
     * @return
     * @throws SQLException
     */
    public int getShortestTravelTime(int start, int end) throws SQLException {
        Map<Integer, List<Edge>> graph = getGraph();

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

            for (Edge e : graph.get(node)) {
                int newDist = currentDist + e.time;

                if (!dist.containsKey(e.to) || newDist < dist.get(e.to)) {
                    dist.put(e.to, newDist);
                    pq.add(new int[]{e.to, newDist});
                }
            }
        }

        return Integer.MAX_VALUE;
    }
}
