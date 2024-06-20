package app;

import app.core.ProcessingTask;
import app.services.EmployeeServiceImpl;


public class PairOfEmplLongestPeriodMain {
    public static void main(String[] args) {
    	
    	ProcessingTask processTask = new ProcessingTask(new EmployeeServiceImpl());
        processTask.run();
    	
    }
}
