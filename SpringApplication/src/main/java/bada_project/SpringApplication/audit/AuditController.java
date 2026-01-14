package bada_project.SpringApplication.audit;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/audit")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public String auditLogs(Model model) {
        try {
            // Clean up old logs before displaying
            auditService.deleteOlderThan48Hours();

            model.addAttribute("logs", auditService.findAll());
            return "admin/audit";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load audit logs: " + e.getMessage());
            model.addAttribute("logs", java.util.Collections.emptyList());
            return "admin/audit";
        }
    }
}