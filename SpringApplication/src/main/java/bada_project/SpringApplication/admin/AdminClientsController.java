package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.ClientDTO;
import bada_project.SpringApplication.audit.AuditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clients")
public class AdminClientsController {

    private final AdminClientsDAO dao;
    private final AuditService auditService;
    private final PasswordEncoder passwordEncoder;

    public AdminClientsController(AdminClientsDAO dao, AuditService auditService, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.auditService = auditService;
        this.passwordEncoder = passwordEncoder;
    }

    /* =========================
       LIST ALL CLIENTS
       ========================= */
    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", dao.findAll());
        return "admin/clients";
    }

    /* =========================
       EDIT CLIENT FORM
       ========================= */
    @GetMapping("/edit/{id}")
    public String editClientForm(@PathVariable Long id, Model model) {
        ClientDTO client = dao.findById(id);
        if (client == null) {
            return "redirect:/admin/clients?error=notfound";
        }

        model.addAttribute("client", client);
        model.addAttribute("salons", dao.getAllSalons());
        return "admin/client-edit";
    }

    /* =========================
       UPDATE CLIENT
       ========================= */
    @PostMapping("/edit/{id}")
    public String updateClient(
            @PathVariable Long id,
            @ModelAttribute ClientDTO client,
            Authentication auth,
            RedirectAttributes redirectAttributes
    ) {
        try {
            client.setIdKlienta(id);
            dao.update(client);

            // Update password if provided
            if (client.getNewPassword() != null && !client.getNewPassword().trim().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(client.getNewPassword());
                dao.updatePassword(id, hashedPassword);

                auditService.log(
                        auth.getName(),
                        "UPDATE_CLIENT_PASSWORD",
                        "Changed password for client with email: " + client.getEmail()
                );
            }

            auditService.log(
                    auth.getName(),
                    "UPDATE_CLIENT",
                    "Updated client with email: " + client.getEmail()
            );

            redirectAttributes.addFlashAttribute("success", "Client updated successfully");
            return "redirect:/admin/clients";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update client: " + e.getMessage());
            return "redirect:/admin/clients/edit/" + id;
        }
    }

    /* =========================
       DELETE CLIENT
       ========================= */
    @PostMapping("/delete/{id}")
    public String deleteClient(
            @PathVariable Long id,
            Authentication auth,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ClientDTO client = dao.findById(id);
            if (client == null) {
                redirectAttributes.addFlashAttribute("error", "Client not found");
                return "redirect:/admin/clients";
            }

            dao.delete(id);

            auditService.log(
                    auth.getName(),
                    "DELETE_CLIENT",
                    "Deleted client with ID=" + id + ", email=" + client.getEmail()
            );

            redirectAttributes.addFlashAttribute("success", "Client deleted successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete client: " + e.getMessage());
        }

        return "redirect:/admin/clients";
    }
}