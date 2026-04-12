package ProblemSetSix.BLL;

import ProblemSetSix.DAL.*;

import java.time.LocalDateTime;
import java.util.List;

public class DeliveryScheduler {

    private StudentDataProvider studentDP = new StudentDataProvider();
    private DeliveryDataProvider deliveryDP = new DeliveryDataProvider();
    private EmployeeDataProvider employeeDP = new EmployeeDataProvider();
    private CampusGraphBLL graphService = new CampusGraphBLL();

    /**
     * Method to schedule an employee (closest to student)
     * @param id
     * @param studentId
     * @param destinationDorm
     * @param isService
     * @return
     * @throws Exception
     */
    public LocalDateTime schedule(int id, int studentId, Integer destinationDorm, boolean isService) throws Exception {

        int studentDorm = studentDP.getStudentDorm(studentId);

        List<Integer> employees = employeeDP.getAllEmployees();

        int bestEmployee = -1;
        int bestTime = Integer.MAX_VALUE;

        for (int empId : employees) {

            if (!employeeDP.isEmployeeOnShift(empId)) continue;
            if (employeeDP.isEmployeeBusy(empId)) continue;

            int empDorm = employeeDP.getEmployeeDorm(empId);

            int travelTime;

            if (!isService) {

                travelTime = graphService.getShortestTravelTime(empDorm, studentDorm);

            } else {

                int leg1 = graphService.getShortestTravelTime(empDorm, studentDorm);
                int leg2 = graphService.getShortestTravelTime(studentDorm, destinationDorm);

                if (leg1 == Integer.MAX_VALUE || leg2 == Integer.MAX_VALUE) continue;

                travelTime = leg1 + leg2;
            }

            if (travelTime < bestTime) {
                bestTime = travelTime;
                bestEmployee = empId;
            }
        }

        if (bestEmployee == -1) {
            throw new Exception("No available employees for delivery today.");
        }

        LocalDateTime deliveryTime = LocalDateTime.now().plusMinutes(bestTime);

        if (!isService) {
            deliveryDP.assignOrderDelivery(id, bestEmployee, deliveryTime);
        } else {
            deliveryDP.assignServiceDelivery(id, bestEmployee, deliveryTime);
        }

        return deliveryTime;
    }
}