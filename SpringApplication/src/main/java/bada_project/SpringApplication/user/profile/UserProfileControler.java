package bada_project.SpringApplication.user.profile;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserProfileControler {

    private final UserProfileService service;

    public UserProfileControler(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model) {
        String username = auth.getName(); // przyjmujemy: username == email
        UserProfile profile = service.getByEmail(username);

        if (profile == null) {
            model.addAttribute("errorMessage",
                    "Nie znaleziono klienta w tabeli KLIENCI dla loginu/email: " + username);
            // możesz zrobić osobny widok error, albo wysłać na dashboard:
            return "profile";
        }

        profile.setUsername(username);
        model.addAttribute("profile", profile);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @Valid @ModelAttribute("profile") UserProfile profile,
                                BindingResult br,
                                Model model) {

        String username = auth.getName();
        UserProfile dbProfile = service.getByEmail(username);

        if (dbProfile == null) {
            model.addAttribute("errorMessage",
                    "Nie znaleziono klienta w bazie dla loginu/email: " + username);
            return "profile";
        }

        // przepisujemy ID z bazy (żeby user nie mógł podmienić)
        profile.setIdKlienta(dbProfile.getIdKlienta());
        profile.setIdAdresu(dbProfile.getIdAdresu());
        profile.setIdSalonu(dbProfile.getIdSalonu());
        profile.setUsername(username);

        // jeżeli numer_lokalu ma być NOT NULL, pilnujemy defaultu
        if (profile.getNumerLokalu() == null) profile.setNumerLokalu(0);
        if (profile.getNumerBudynku() == null) profile.setNumerBudynku(0);

        if (br.hasErrors()) {
            return "profile";
        }

        service.updateProfile(profile);
        model.addAttribute("successMessage", "Zapisano zmiany.");
        return "profile";
    }
}
