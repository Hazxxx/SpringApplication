package bada_project.SpringApplication.admin.dbmanagement;

import bada_project.SpringApplication.admin.dbmanagement.dao.MarkiDAO;
import bada_project.SpringApplication.admin.dbmanagement.models.Marka;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/database")
public class DatabaseManagementController {

    private final MarkiDAO markiDAO;

    public DatabaseManagementController(MarkiDAO markiDAO) {
        this.markiDAO = markiDAO;
    }

    // ===== MAIN DATABASE PAGE =====

    @GetMapping
    public String databaseIndex(Model model) {
        model.addAttribute("totalMarki", markiDAO.countTotal());
        return "admin/database/index";
    }

    // ===== MARKI - LIST =====

    @GetMapping("/marki")
    public String markiList(Model model) {
        model.addAttribute("marki", markiDAO.findAll());
        return "admin/database/marki/list";
    }

    // ===== MARKI - NEW =====

    @GetMapping("/marki/new")
    public String markiNew(Model model) {
        model.addAttribute("marka", new Marka());
        model.addAttribute("isEdit", false);
        return "admin/database/marki/form";
    }

    // ===== MARKI - CREATE =====

    @PostMapping("/marki")
    public String markiCreate(@ModelAttribute Marka marka, RedirectAttributes redirectAttributes) {
        try {
            // Walidacja
            if (marka.getNazwa() == null || marka.getNazwa().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nazwa marki nie może być pusta");
                return "redirect:/admin/database/marki/new";
            }

            if (markiDAO.existsByNazwa(marka.getNazwa())) {
                redirectAttributes.addFlashAttribute("error", "Marka o tej nazwie już istnieje");
                return "redirect:/admin/database/marki/new";
            }

            markiDAO.insert(marka);
            redirectAttributes.addFlashAttribute("success", "Marka została dodana pomyślnie");
            return "redirect:/admin/database/marki";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania marki: " + e.getMessage());
            return "redirect:/admin/database/marki/new";
        }
    }

    // ===== MARKI - EDIT =====

    @GetMapping("/marki/{id}/edit")
    public String markiEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Marka> markaOpt = markiDAO.findById(id);

        if (markaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Marka nie została znaleziona");
            return "redirect:/admin/database/marki";
        }

        model.addAttribute("marka", markaOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/database/marki/form";
    }

    // ===== MARKI - UPDATE =====

    @PostMapping("/marki/{id}")
    public String markiUpdate(@PathVariable Long id, @ModelAttribute Marka marka, RedirectAttributes redirectAttributes) {
        try {
            // Walidacja
            if (marka.getNazwa() == null || marka.getNazwa().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nazwa marki nie może być pusta");
                return "redirect:/admin/database/marki/" + id + "/edit";
            }

            if (markiDAO.existsByNazwaExcludingId(marka.getNazwa(), id)) {
                redirectAttributes.addFlashAttribute("error", "Marka o tej nazwie już istnieje");
                return "redirect:/admin/database/marki/" + id + "/edit";
            }

            marka.setIdMarki(id);
            markiDAO.update(marka);
            redirectAttributes.addFlashAttribute("success", "Marka została zaktualizowana pomyślnie");
            return "redirect:/admin/database/marki";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji marki: " + e.getMessage());
            return "redirect:/admin/database/marki/" + id + "/edit";
        }
    }

    // ===== MARKI - DELETE =====

    @PostMapping("/marki/{id}/delete")
    public String markiDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Marka> markaOpt = markiDAO.findById(id);

            if (markaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Marka nie została znaleziona");
                return "redirect:/admin/database/marki";
            }

            Marka marka = markaOpt.get();

            // Sprawdź czy są powiązane modele
            if (marka.getLiczbaModeli() != null && marka.getLiczbaModeli() > 0) {
                redirectAttributes.addFlashAttribute("error",
                        "Nie można usunąć marki. Istnieje " + marka.getLiczbaModeli() + " powiązanych modeli.");
                return "redirect:/admin/database/marki";
            }

            markiDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Marka została usunięta pomyślnie");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć marki. Istnieją powiązane rekordy w bazie danych.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania marki: " + e.getMessage());
        }

        return "redirect:/admin/database/marki";
    }
}