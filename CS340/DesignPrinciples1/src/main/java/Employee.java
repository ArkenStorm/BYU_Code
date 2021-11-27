/*1. What design principles does this code violate?
    It's not high quality abstraction, it's decomposed TOO much; the getter function for total years of service should
    be part of the employee.

2. Refactor the code to improve its design.

*/


import java.util.Date;

class Employee {
    …
    private Date employmentStartDate;
    private Date employmentEndDate;

    public int getTotalYearsOfService() { … }
}


class RetirementCalculator {
    private Employee employee;

    public RetirementCalculator(Employee emp) {
        this.employee = emp;
    }

    public float calculateRetirement(Date payPeriodStart, Date payPeriodEnd) { … }

    private int getMonthsInLastPosition(Date startDate, Date endDate) { … }
    ...
}
