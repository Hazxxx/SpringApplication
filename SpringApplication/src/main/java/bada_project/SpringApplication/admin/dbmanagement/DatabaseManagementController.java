package bada_project.SpringApplication.admin.dbmanagement;

import bada_project.SpringApplication.admin.dbmanagement.dao.*;
import bada_project.SpringApplication.admin.dbmanagement.models.*;
import bada_project.SpringApplication.dao.AdresyDAO;
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
    private final ModeleDAO modeleDAO;
    private final SalonyDAO salonyDAO;
    private final AdresyDAO adresyDAO;
    private final StanowiskaDAO stanowiskaDAO;
    private final FirmyPartnerskieDAO firmyPartnerskieDAO;

    public DatabaseManagementController(
            MarkiDAO markiDAO,
            ModeleDAO modeleDAO,
            SalonyDAO salonyDAO,
            AdresyDAO adresyDAO,
            StanowiskaDAO stanowiskaDAO,
            FirmyPartnerskieDAO firmyPartnerskieDAO
    ) {
        this.markiDAO = markiDAO;
        this.modeleDAO = modeleDAO;
        this.salonyDAO = salonyDAO;
        this.adresyDAO = adresyDAO;
        this.stanowiskaDAO = stanowiskaDAO;
        this.firmyPartnerskieDAO = firmyPartnerskieDAO;
    }

    // ===== MAIN DATABASE PAGE =====

    // ===== MARKI =====
    @GetMapping("/marki")
    public String markiList(Model model) {
        model.addAttribute("marki", markiDAO.findAll());
        return "admin/database/marki/list";
    }

    @GetMapping("/marki/new")
    public String markiNew(Model model) {
        model.addAttribute("marka", new Marka());
        model.addAttribute("isEdit", false);
        return "admin/database/marki/form";
    }

    @PostMapping("/marki")
    public String markiCreate(@ModelAttribute Marka marka, RedirectAttributes redirectAttributes) {
        try {
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

    @PostMapping("/marki/{id}")
    public String markiUpdate(@PathVariable Long id, @ModelAttribute Marka marka, RedirectAttributes redirectAttributes) {
        try {
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

    @PostMapping("/marki/{id}/delete")
    public String markiDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Marka> markaOpt = markiDAO.findById(id);
            if (markaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Marka nie została znaleziona");
                return "redirect:/admin/database/marki";
            }
            Marka marka = markaOpt.get();
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

    // ===== MODELE =====
    @GetMapping("/modele")
    public String modeleList(Model model) {
        model.addAttribute("modele", modeleDAO.findAll());
        return "admin/database/modele/list";
    }

    @GetMapping("/modele/new")
    public String modeleNew(Model model) {
        model.addAttribute("model", new ModelSamochodu());
        model.addAttribute("marki", markiDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/modele/form";
    }

    @PostMapping("/modele")
    public String modeleCreate(@ModelAttribute ModelSamochodu model, RedirectAttributes redirectAttributes) {
        try {
            modeleDAO.insert(model);
            redirectAttributes.addFlashAttribute("success", "Model został dodany pomyślnie");
            return "redirect:/admin/database/modele";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania modelu: " + e.getMessage());
            return "redirect:/admin/database/modele/new";
        }
    }

    @GetMapping("/modele/{id}/edit")
    public String modeleEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ModelSamochodu> modelOpt = modeleDAO.findById(id);
        if (modelOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Model nie został znaleziony");
            return "redirect:/admin/database/modele";
        }
        model.addAttribute("model", modelOpt.get());
        model.addAttribute("marki", markiDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/modele/form";
    }

    @PostMapping("/modele/{id}")
    public String modeleUpdate(@PathVariable Long id, @ModelAttribute ModelSamochodu model, RedirectAttributes redirectAttributes) {
        try {
            model.setIdModelu(id);
            modeleDAO.update(model);
            redirectAttributes.addFlashAttribute("success", "Model został zaktualizowany pomyślnie");
            return "redirect:/admin/database/modele";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji modelu: " + e.getMessage());
            return "redirect:/admin/database/modele/" + id + "/edit";
        }
    }

    @PostMapping("/modele/{id}/delete")
    public String modeleDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeleDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Model został usunięty pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć modelu. Istnieją powiązane pojazdy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania modelu: " + e.getMessage());
        }
        return "redirect:/admin/database/modele";
    }

    // ===== SALONY =====
    @GetMapping("/salony")
    public String salonyList(Model model) {
        model.addAttribute("salony", salonyDAO.findAll());
        return "admin/database/salony/list";
    }

    @GetMapping("/salony/new")
    public String salonyNew(Model model) {
        model.addAttribute("salon", new Salon());
        model.addAttribute("firmyPartnerskie", firmyPartnerskieDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/salony/form";
    }

    @PostMapping("/salony")
    public String salonyCreate(@ModelAttribute Salon salon, RedirectAttributes redirectAttributes) {
        try {
            salonyDAO.insert(salon);
            redirectAttributes.addFlashAttribute("success", "Salon został dodany pomyślnie");
            return "redirect:/admin/database/salony";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania salonu: " + e.getMessage());
            return "redirect:/admin/database/salony/new";
        }
    }

    @GetMapping("/salony/{id}/edit")
    public String salonyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Salon> salonOpt = salonyDAO.findById(id);
        if (salonOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Salon nie został znaleziony");
            return "redirect:/admin/database/salony";
        }
        model.addAttribute("salon", salonOpt.get());
        model.addAttribute("firmyPartnerskie", firmyPartnerskieDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/salony/form";
    }

    @PostMapping("/salony/{id}")
    public String salonyUpdate(@PathVariable Long id, @ModelAttribute Salon salon, RedirectAttributes redirectAttributes) {
        try {
            salon.setIdSalonu(id);
            salonyDAO.update(salon);
            redirectAttributes.addFlashAttribute("success", "Salon został zaktualizowany pomyślnie");
            return "redirect:/admin/database/salony";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji salonu: " + e.getMessage());
            return "redirect:/admin/database/salony/" + id + "/edit";
        }
    }

    @PostMapping("/salony/{id}/delete")
    public String salonyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            salonyDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Salon został usunięty pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć salonu. Istnieją powiązane rekordy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania salonu: " + e.getMessage());
        }
        return "redirect:/admin/database/salony";
    }

    // ===== STANOWISKA =====
    @GetMapping("/stanowiska")
    public String stanowiskaList(Model model) {
        model.addAttribute("stanowiska", stanowiskaDAO.findAll());
        return "admin/database/stanowiska/list";
    }

    @GetMapping("/stanowiska/new")
    public String stanowiskaNew(Model model) {
        model.addAttribute("stanowisko", new Stanowisko());
        model.addAttribute("isEdit", false);
        return "admin/database/stanowiska/form";
    }

    @PostMapping("/stanowiska")
    public String stanowiskaCreate(@ModelAttribute Stanowisko stanowisko, RedirectAttributes redirectAttributes) {
        try {
            stanowiskaDAO.insert(stanowisko);
            redirectAttributes.addFlashAttribute("success", "Stanowisko zostało dodane pomyślnie");
            return "redirect:/admin/database/stanowiska";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania stanowiska: " + e.getMessage());
            return "redirect:/admin/database/stanowiska/new";
        }
    }

    @GetMapping("/stanowiska/{id}/edit")
    public String stanowiskaEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Stanowisko> stanowiskoOpt = stanowiskaDAO.findById(id);
        if (stanowiskoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Stanowisko nie zostało znalezione");
            return "redirect:/admin/database/stanowiska";
        }
        model.addAttribute("stanowisko", stanowiskoOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/database/stanowiska/form";
    }

    @PostMapping("/stanowiska/{id}")
    public String stanowiskaUpdate(@PathVariable Long id, @ModelAttribute Stanowisko stanowisko, RedirectAttributes redirectAttributes) {
        try {
            stanowisko.setIdStanowiska(id);
            stanowiskaDAO.update(stanowisko);
            redirectAttributes.addFlashAttribute("success", "Stanowisko zostało zaktualizowane pomyślnie");
            return "redirect:/admin/database/stanowiska";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji stanowiska: " + e.getMessage());
            return "redirect:/admin/database/stanowiska/" + id + "/edit";
        }
    }

    @PostMapping("/stanowiska/{id}/delete")
    public String stanowiskaDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            stanowiskaDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Stanowisko zostało usunięte pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć stanowiska. Istnieją przypisani pracownicy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania stanowiska: " + e.getMessage());
        }
        return "redirect:/admin/database/stanowiska";
    }

    // ===== FIRMY PARTNERSKIE =====
    @GetMapping("/firmy-partnerskie")
    public String firmyList(Model model) {
        model.addAttribute("firmy", firmyPartnerskieDAO.findAll());
        return "admin/database/firmy/list";
    }

    @GetMapping("/firmy-partnerskie/new")
    public String firmyNew(Model model) {
        model.addAttribute("firma", new FirmaPartnerska());
        model.addAttribute("isEdit", false);
        return "admin/database/firmy/form";
    }

    @PostMapping("/firmy-partnerskie")
    public String firmyCreate(@ModelAttribute FirmaPartnerska firma, RedirectAttributes redirectAttributes) {
        try {
            firmyPartnerskieDAO.insert(firma);
            redirectAttributes.addFlashAttribute("success", "Firma została dodana pomyślnie");
            return "redirect:/admin/database/firmy-partnerskie";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania firmy: " + e.getMessage());
            return "redirect:/admin/database/firmy-partnerskie/new";
        }
    }

    @GetMapping("/firmy-partnerskie/{id}/edit")
    public String firmyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<FirmaPartnerska> firmaOpt = firmyPartnerskieDAO.findById(id);
        if (firmaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Firma nie została znaleziona");
            return "redirect:/admin/database/firmy-partnerskie";
        }
        model.addAttribute("firma", firmaOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/database/firmy/form";
    }

    @PostMapping("/firmy-partnerskie/{id}")
    public String firmyUpdate(@PathVariable Long id, @ModelAttribute FirmaPartnerska firma, RedirectAttributes redirectAttributes) {
        try {
            firma.setIdFirmyPartnerskiej(id);
            firmyPartnerskieDAO.update(firma);
            redirectAttributes.addFlashAttribute("success", "Firma została zaktualizowana pomyślnie");
            return "redirect:/admin/database/firmy-partnerskie";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji firmy: " + e.getMessage());
            return "redirect:/admin/database/firmy-partnerskie/" + id + "/edit";
        }
    }

    @PostMapping("/firmy-partnerskie/{id}/delete")
    public String firmyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            firmyPartnerskieDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Firma została usunięta pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć firmy. Istnieją powiązane salony.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania firmy: " + e.getMessage());
        }
        return "redirect:/admin/database/firmy-partnerskie";
    }
}