package bada_project.SpringApplication.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminDashboardDAO dashboardDAO;

    public AdminController(AdminDashboardDAO dashboardDAO) {
        this.dashboardDAO = dashboardDAO;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardDAO.fetchStats());
        return "admin/dashboard";
    }

    @GetMapping("/structure")
    public String structure() {
        return "admin/structure";
    }
}