package ProblemSetSix.BLL;

import java.time.LocalDateTime;

import ProblemSetSix.DAL.ServiceOrderDataProvider;

public class ServiceController {

    private DeliveryScheduler scheduler = new DeliveryScheduler();
    private ServiceOrderDataProvider serviceDP = new ServiceOrderDataProvider();

    /**
     * Method to request a library return used in main
     * @param studentId
     * @return
     * @throws Exception
     */
    public String requestLibraryReturn(int studentId) throws Exception {

        int serviceOrderId = serviceDP.createServiceOrder(studentId, 2);

        int libraryDormId = 3;

        LocalDateTime eta = scheduler.schedule(serviceOrderId, studentId, libraryDormId,true);

        return "Library return scheduled! ETA: " + eta;
    }
}