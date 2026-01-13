package bada_project.SpringApplication.user.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class DashboardController {

    // Automatyczne przekierowanie po zalogowaniu
    @GetMapping("/main")
    public String defaultAfterLogin(Authentication authentication) {
        // Sprawdzamy role użytkownika
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_EMPLOYEE"));

        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else if (isEmployee) {
            return "redirect:/employee/dashboard";
        } else if (isUser) {
            return "redirect:/user/dashboard";
        } else {
            return "redirect:/index";
        }
    }
/*
    // Panel administratora
    @GetMapping("/admin/dashboard")
    public String showAdminPage(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "admin/dashboard";
    }
*/
    // Panel użytkownika
    @GetMapping("/user/dashboard")
    public String showUserPage(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "user/dashboard";
    }
}