package ProblemSetSix.BLL;

import ProblemSetSix.DAL.*;

import java.time.LocalDateTime;
import java.util.List;

public class DeliveryScheduler {

    private StudentDataProvider studentDP = new StudentDataProvider();
    private DeliveryDataProvider deliveryDP = new DeliveryDataProvider();
    private EmployeeDataProvider employeeDP = new EmployeeDataProvider();
    private CampusGraphBLL graphService = new CampusGraphBLL();

    public LocalDateTime schedule(int orderId, int studentId) throws Exception {

        int studentDorm = studentDP.getStudentDorm(studentId);

        List<Integer> employees = employeeDP.getAllEmployees();

        int bestEmployee = -1;
        int bestTime = Integer.MAX_VALUE;

        for (int empId : employees) {

            if (!employeeDP.isEmployeeOnShift(empId)) {
                continue;
            }

            if (deliveryDP.isEmployeeBusy(empId)) {
                continue;
            }

            int empDorm = employeeDP.getEmployeeDorm(empId);

            int travelTime = graphService.getShortestTravelTime(empDorm, studentDorm);

            if (travelTime < bestTime) {
                bestTime = travelTime;
                bestEmployee = empId;
            }
        }

        if (bestEmployee == -1) {
            throw new Exception("No available employees for delivery today.");
        }

        LocalDateTime deliveryTime = LocalDateTime.now().plusMinutes(bestTime);

        deliveryDP.assignDelivery(orderId, bestEmployee, deliveryTime);

        return deliveryTime;
    }
}