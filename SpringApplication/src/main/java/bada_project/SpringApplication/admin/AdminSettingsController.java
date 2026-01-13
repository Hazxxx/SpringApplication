package bada_project.SpringApplication.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminSettingsController {

    private final SystemFlagsDAO systemFlagsDAO;

    public AdminSettingsController(SystemFlagsDAO systemFlagsDAO) {
        this.systemFlagsDAO = systemFlagsDAO;
    }

    @GetMapping("/admin/settings")
    public String settings(Model model) {
        model.addAttribute(
                "maintenanceEnabled",
                systemFlagsDAO.isMaintenanceEnabled()
        );
        return "admin/settings";
    }

    @PostMapping("/admin/settings/maintenance/enable")
    public String enableMaintenance() {
        systemFlagsDAO.setMaintenance(true);
        return "redirect:/admin/settings";
    }

    @PostMapping("/admin/settings/maintenance/disable")
    public String disableMaintenance() {
        systemFlagsDAO.setMaintenance(false);
        return "redirect:/admin/settings";
    }
}
