package bada_project.SpringApplication.admin;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminSettingsController {

    private final SystemFlagsDAO systemFlagsDAO;
    private final DatabaseBackupService databaseBackupService;

    public AdminSettingsController(SystemFlagsDAO systemFlagsDAO,
                                   DatabaseBackupService databaseBackupService) {
        this.systemFlagsDAO = systemFlagsDAO;
        this.databaseBackupService = databaseBackupService;
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

    @GetMapping("/admin/settings/database/backup")
    public ResponseEntity<Resource> downloadBackup() {
        try {
            byte[] backupData = databaseBackupService.createBackup();

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "database_backup_" + timestamp + ".sql";

            ByteArrayResource resource = new ByteArrayResource(backupData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(backupData.length)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create database backup: " + e.getMessage(), e);
        }
    }

    @PostMapping("/admin/settings/database/clear")
    public String clearDatabase(RedirectAttributes redirectAttributes) {
        try {
            databaseBackupService.clearDatabase();
            redirectAttributes.addFlashAttribute("success",
                    "Database has been cleared successfully. All data has been removed.");
            return "redirect:/admin/settings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to clear database: " + e.getMessage());
            return "redirect:/admin/settings";
        }
    }
}