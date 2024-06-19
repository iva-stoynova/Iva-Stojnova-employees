package app.factories;

import app.model.PairOfEmployees;

public final class PairOfEmployeesFactory {

    public PairOfEmployeesFactory() {
    }

    public static PairOfEmployees create(long firstEmployeeId, long secondEmployeeId, long overlapDuration) {
        return new PairOfEmployees(
                firstEmployeeId,
                secondEmployeeId,
                overlapDuration);
    }
}
