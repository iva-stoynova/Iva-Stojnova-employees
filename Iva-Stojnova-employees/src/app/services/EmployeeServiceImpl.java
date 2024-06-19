package app.services;

import app.model.Employee;
import app.model.PairOfEmployees;
import app.factories.PairOfEmployeesFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static app.ApplicationConstants.*;

public class EmployeeServiceImpl implements EmployeeService {

	private List<Employee> database;

    
    public EmployeeServiceImpl() {
    	 this.database = new ArrayList<>();
    }

   
    @Override
    public void addEmployeeRecords(List<Employee> records) {
    	this.database.addAll(records);
    }

    /** finding all PairOfEmployees, couples which have overlap and save them into List<PairOfEmployees> */
    @Override
    public List<PairOfEmployees> findAllPairOfEmplsWithOverlap() {
    	
        List<Employee> allRecords = Collections.unmodifiableList(this.database);

        List<PairOfEmployees> pairOfEmpls = new ArrayList<>();
        
        for (int i = INDEX_ZERO; i < allRecords.size() - ONE; i++) {
            for (int j = i + ONE; j < allRecords.size(); j++) {
                
            	Employee firstEmpl = allRecords.get(i);
                Employee secondEmpl = allRecords.get(j);

                if (firstEmpl.getProjectId() == secondEmpl.getProjectId() && isDateOverlapped(firstEmpl, secondEmpl)) {
                   
                	long overlapDays = checkTimeOverlaps(firstEmpl, secondEmpl);

                    if (overlapDays > DEFAULT_OVERLAP_ZERO_DAYS) {
                        updatePairOfEmplCollection(pairOfEmpls, firstEmpl, secondEmpl, overlapDays);
                    }
                }
            }
        }
        return pairOfEmpls;
    }

    /** calculating the total overlap and returning it */
    private long checkTimeOverlaps(Employee firstEmpl, Employee secondEmpl) {
           	
    	// (startA <= startB) - firstEmpl.getDateFrom().isBefore(secondEmpl.getDateFrom())
    	LocalDate periodStartDate = (firstEmpl.getDateFrom().compareTo(secondEmpl.getDateFrom())<=0) ? secondEmpl.getDateFrom() : firstEmpl.getDateFrom();

        
    	// (endB endA <= endB) - firstEmpl.getDateTo().isBefore(secondEmpl.getDateTo())
    	LocalDate periodEndDate = (firstEmpl.getDateTo().compareTo(secondEmpl.getDateTo())<=0) ? firstEmpl.getDateTo() : secondEmpl.getDateTo();

        //ОК when we have involved leap years too - from 2019-01-01 to 2019-01-15 will return 14days in result not 15, which will accept for correct
        return Math.abs(ChronoUnit.DAYS.between(periodStartDate, periodEndDate));
    }

    /** returning if two employees have overlap */
    private boolean isDateOverlapped(Employee firstEmpl, Employee secondEmpl) {
        
    	//have overlap if (startA <= endB) and (endA >= startB)         
        return ( (firstEmpl.getDateFrom().compareTo(secondEmpl.getDateTo())<=0) && (firstEmpl.getDateTo().compareTo(secondEmpl.getDateFrom())>=0) );
        	
    }

    /** check and returning if the current pairOfEmployees is already present in pairOfEmpl collection (worked together under others projects) */
    private boolean isPairOfEmplPresent(PairOfEmployees pairOfEmployees, long firstEmplId, long secondEmplId) {
       
    	return ( pairOfEmployees.getFirstEmployeeId() == firstEmplId && pairOfEmployees.getSecondEmployeeId() == secondEmplId )
                || ( pairOfEmployees.getFirstEmployeeId() == secondEmplId && pairOfEmployees.getSecondEmployeeId() == firstEmplId );        
    }

    // двойката служители, които най-дълго време са работили заедно по ОБЩИ ПРОЕКТИ  
    /** If the pairOfEmpls is already present, it's total overlap duration will be updated with the new value, otherwise will be create and add new pairOfEmpls with the current data */
    private void updatePairOfEmplCollection(List<PairOfEmployees> pairOfEmps, Employee firstEmpl, Employee secondEmpl, long overlapDays) {
        
    	AtomicBoolean isPresent = new AtomicBoolean(false);
        
    	//If the PairOfEmployees is present -> update pairOfEmpl's total overlap
        pairOfEmps.forEach(pairOfEmpl -> {
            if (isPairOfEmplPresent(pairOfEmpl, firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId())) {
            	pairOfEmpl.addOverlapDuration(overlapDays);
                isPresent.set(true);
            }
        });
        //If the PairOfEmployees isn't present -> create and add new pairOfEmpl with the current data
        if (!isPresent.get()) {
            PairOfEmployees newPairOfEmployees = PairOfEmployeesFactory.create(firstEmpl.getEmployeeId(), secondEmpl.getEmployeeId(), overlapDays);
            pairOfEmps.add(newPairOfEmployees);
        }
    }
}
