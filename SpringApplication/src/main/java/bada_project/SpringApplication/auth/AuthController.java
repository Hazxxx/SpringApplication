package bada_project.SpringApplication.auth;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final RegisterService registerService;

    public AuthController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(
            @Valid @ModelAttribute("form") RegisterForm form,
            BindingResult br,
            Model model
    ) {
        if (br.hasErrors()) {
            model.addAttribute("error",
                    br.getAllErrors().get(0).getDefaultMessage());
            return "register";
        }

        registerService.register(form);
        return "redirect:/register?success";
    }
}
