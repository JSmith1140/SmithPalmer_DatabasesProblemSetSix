package ProblemSetSix.BLL;

import ProblemSetSix.DAL.EmployeeDataProvider;
import ProblemSetSix.DAL.ReportDataProvider;
import java.time.LocalDate;
import java.util.*;

public class ReportController {

    private ReportDataProvider reportDP = new ReportDataProvider();
    private CampusGraphBLL graph = new CampusGraphBLL();
    private EmployeeDataProvider employeeDP = new EmployeeDataProvider();

    /**
     * Method to generate the weekly report used in main
     * @throws Exception
     */
    public void generateWeeklyReport() throws Exception {

        List<ReportDataProvider.DeliveryRecord> records = reportDP.getAllDeliveries();

        Map<Integer, Map<LocalDate, List<ReportDataProvider.DeliveryRecord>>> grouped = new HashMap<>();

        for (var r : records) {
            LocalDate date = r.time.toLocalDateTime().toLocalDate();

            grouped.putIfAbsent(r.employeeId, new HashMap<>());
            grouped.get(r.employeeId).putIfAbsent(date, new ArrayList<>());
            grouped.get(r.employeeId).get(date).add(r);
        }

        System.out.println("\n===== WEEKLY REPORT =====");

        for (int empId : grouped.keySet()) {

            System.out.println("\nEmployee " + empId + ":");

            for (LocalDate date : grouped.get(empId).keySet()) {

                System.out.println("  Date: " + date);

                int totalTime = 0;

                for (var r : grouped.get(empId).get(date)) {

                    System.out.println("    Path: Employee Location -> " + r.dormName);

                    if (r.isService) {
                        System.out.println("    Service: " + r.itemName);
                    } else {
                        System.out.println("    Item: " + r.itemName + " x" + r.quantity);
                    }

                    int empDorm = employeeDP.getEmployeeDorm(empId);

                    int time;

                    if (!r.isService) {
                        time = graph.getShortestTravelTime(empDorm, r.dormId);
                    } else {
                        int libraryDormId = 4; 

                        int leg1 = graph.getShortestTravelTime(empDorm, r.dormId);
                        int leg2 = graph.getShortestTravelTime(r.dormId, libraryDormId);

                        if (leg1 == Integer.MAX_VALUE || leg2 == Integer.MAX_VALUE) {
                            continue;
                        }

                        time = leg1 + leg2;
                    }

                    totalTime += time;
                }
                System.out.println("    Total Time: " + totalTime + " minutes\n");
            }
        }
    }
}