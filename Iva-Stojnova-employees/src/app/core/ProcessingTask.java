package app.core;


import app.model.Employee;
import app.model.PairOfEmployees;
import app.services.EmployeeService;
import app.factories.EmployeeFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static app.ApplicationConstants.*;

public class ProcessingTask implements Runnable {

    private EmployeeService emplService;

   
    public ProcessingTask(EmployeeService emplService) {
       
        this.emplService = emplService;
    }
   
    @Override
    public void run() {
        //Read all records data from .txt file
        List<Employee> records = null;
		try {
			records = readFile(FILE_PATH)
			        .stream()
			        .map(EmployeeFactory::create)
			        .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Save all employee records into "database"
        this.emplService.addEmployeeRecords(records);

        //Find all PairOfEmployees, couple of employees which r worked under same project and have overlap
        List<PairOfEmployees> pairOfEmpls = this.emplService.findAllPairOfEmplsWithOverlap();

        printOutput(pairOfEmpls);
    }
        
    private static List<String> readFile(String fileName) throws IOException {
       
    	List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());
        }
        return result;

    }
    
    private void printOutput(List<PairOfEmployees> pairOfEmpls) {
        
    	if (pairOfEmpls.size() != EMPTY_COLLECTION_SIZE) {
        	
    		pairOfEmpls.sort((pairOfEmpl1, pairOfEmpl2) ->
                    (int) (pairOfEmpl2.getTotalDuration() - pairOfEmpl1.getTotalDuration()));
            
    		PairOfEmployees bestTeam = pairOfEmpls.get(INDEX_ZERO);

            System.out.print(String.format(BEST_TEAM_PATTERN,
                            bestTeam.getFirstEmployeeId(),
                            bestTeam.getSecondEmployeeId(),
                            bestTeam.getTotalDuration()));
        } else {
        	 System.out.print(NO_TEAMS_MSG);
        }
    }
     
}
