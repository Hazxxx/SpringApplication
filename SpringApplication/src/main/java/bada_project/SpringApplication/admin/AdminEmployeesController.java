package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.EmployeeDTO;
import bada_project.SpringApplication.audit.AuditService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeesController {

    private final AdminEmployeesDAO dao;
    private final AuditService auditService;

    public AdminEmployeesController(AdminEmployeesDAO dao, AuditService auditService) {
        this.dao = dao;
        this.auditService = auditService;
    }

    /* =========================
       LIST ALL EMPLOYEES
       ========================= */
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", dao.findAll());
        return "admin/employees";
    }

    /* =========================
       EDIT EMPLOYEE FORM
       ========================= */
    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        EmployeeDTO employee = dao.findById(id);
        if (employee == null) {
            return "redirect:/admin/employees?error=notfound";
        }

        model.addAttribute("employee", employee);
        model.addAttribute("positions", dao.getAllPositions());
        model.addAttribute("salons", dao.getAllSalons());
        return "admin/employee-edit";
    }

    /* =========================
       UPDATE EMPLOYEE
       ========================= */
    @PostMapping("/edit/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @ModelAttribute EmployeeDTO employee,
            Authentication auth,
            RedirectAttributes redirectAttributes
    ) {
        try {
            employee.setIdPracownika(id);
            dao.update(employee);

            auditService.log(
                    auth.getName(),
                    "Updated employee: " + employee.getEmail()
            );

            redirectAttributes.addFlashAttribute("success", "Employee updated successfully");
            return "redirect:/admin/employees";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update employee: " + e.getMessage());
            return "redirect:/admin/employees/edit/" + id;
        }
    }

    /* =========================
       DELETE EMPLOYEE
       ========================= */
    @PostMapping("/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            Authentication auth,
            RedirectAttributes redirectAttributes
    ) {
        try {
            EmployeeDTO employee = dao.findById(id);
            if (employee == null) {
                redirectAttributes.addFlashAttribute("error", "Employee not found");
                return "redirect:/admin/employees";
            }

            dao.delete(id);

            auditService.log(
                    auth.getName(),
                    "Deleted employee: " + employee.getEmail()
            );

            redirectAttributes.addFlashAttribute("success", "Employee deleted successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete employee: " + e.getMessage());
        }

        return "redirect:/admin/employees";
    }
}