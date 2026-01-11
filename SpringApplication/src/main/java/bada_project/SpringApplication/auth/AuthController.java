package bada_project.SpringApplication.auth;

import bada_project.SpringApplication.dao.KlienciDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final KlienciDAO klienciDAO;
    private final PasswordEncoder passwordEncoder;

    public AuthController(KlienciDAO klienciDAO, PasswordEncoder passwordEncoder) {
        this.klienciDAO = klienciDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("form") RegisterForm form, Model model) {

        System.out.println(">>> REGISTER START");
        System.out.println(">>> Email: " + form.getEmail());
        System.out.println(">>> First Name: " + form.getFirstName());

        // Check if email already exists
        if (klienciDAO.existsByEmail(form.getEmail())) {
            System.out.println(">>> EMAIL EXISTS");
            model.addAttribute("error", "Email already registered");
            model.addAttribute("form", form); // Keep form data
            return "register";
        }

        try {
            // Hash the password
            String hash = passwordEncoder.encode(form.getPassword());
            System.out.println(">>> PASSWORD HASHED");

            // Insert client (this also creates address)
            klienciDAO.insertClient(form, hash);
            System.out.println(">>> INSERT CLIENT + ADDRESS DONE");

            // Redirect to same page with success parameter
            return "redirect:/register?success";

        } catch (Exception e) {
            System.err.println(">>> ERROR DURING REGISTRATION: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("form", form);
            return "register";
        }
    }
}