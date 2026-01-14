package bada_project.SpringApplication.admin.dto;

public class AdminDashboardStats {

    private final int clientsCount;
    private final int employeesCount;
    private final int adminsCount;
    private final int salonsCount;
    private final int companiesCount;

    public AdminDashboardStats(
            int clientsCount,
            int employeesCount,
            int adminsCount,
            int salonsCount,
            int companiesCount
    ) {
        this.clientsCount = clientsCount;
        this.employeesCount = employeesCount;
        this.adminsCount = adminsCount;
        this.salonsCount = salonsCount;
        this.companiesCount = companiesCount;
    }

    public int getClientsCount() { return clientsCount; }
    public int getEmployeesCount() { return employeesCount; }
    public int getAdminsCount() { return adminsCount; }
    public int getSalonsCount() { return salonsCount; }
    public int getCompaniesCount() { return companiesCount; }
}
