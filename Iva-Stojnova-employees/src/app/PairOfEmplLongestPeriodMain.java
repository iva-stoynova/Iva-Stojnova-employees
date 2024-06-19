package app;

import app.core.ProcessingTask;
//import app.io.ConsoleOutputWriter;
//import app.io.FileIO;
//import app.io.FileIOImpl;
//import app.io.Writer;
///import app.repository.EmployeeRepository;
//import app.repository.EmployeeRepositoryImpl;
//import app.services.EmployeeService;
import app.services.EmployeeServiceImpl;


public class PairOfEmplLongestPeriodMain {
    public static void main(String[] args) {
    	
    	ProcessingTask processTask = new ProcessingTask(new EmployeeServiceImpl());
        processTask.run();
    	
    }
}
