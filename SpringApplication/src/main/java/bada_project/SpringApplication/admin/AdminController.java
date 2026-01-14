package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.AdminDashboardStats;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminDashboardDAO dashboardDAO;

    public AdminController(AdminDashboardDAO dashboardDAO) {
        this.dashboardDAO = dashboardDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        AdminDashboardStats stats = dashboardDAO.fetchStats();

        model.addAttribute("clientsCount", stats.getClientsCount());
        model.addAttribute("employeesCount", stats.getEmployeesCount());
        model.addAttribute("adminsCount", stats.getAdminsCount());
        model.addAttribute("salonsCount", stats.getSalonsCount());
        model.addAttribute("companiesCount", stats.getCompaniesCount());

        return "admin/dashboard";
    }

    @PostMapping("/clear-database")
    public String clearDatabase(RedirectAttributes redirectAttributes) {
        dashboardDAO.clearDatabase();
        redirectAttributes.addFlashAttribute(
                "success",
                "Baza danych została wyczyszczona."
        );
        return "redirect:/admin/dashboard";
    }


    @GetMapping("/structure")
    public String structure() {
        return "admin/structure";
    }
}
