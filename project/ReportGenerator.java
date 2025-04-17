package project;

interface ReportGenerator {
   /**
     * Generates a report based on the provided criteria.
     * 
     * @param criteria The filtering criteria for the report
     * @return The generated report
     */
    Report generateReport();
    
    /**
     * Filters the report based on specified criteria.
     * 
     * @param criteria The filtering criteria
     */
    void filterReport(String criteria); 
}
