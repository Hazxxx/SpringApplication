package bada_project.SpringApplication.admin.dbmanagement;

import bada_project.SpringApplication.admin.dbmanagement.dao.*;
import bada_project.SpringApplication.admin.dbmanagement.models.*;
import bada_project.SpringApplication.dao.KlienciDAO;
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
    private final adminAdresyDAO adminAdresyDAO;
    private final StanowiskaDAO stanowiskaDAO;
    private final FirmyPartnerskieDAO firmyPartnerskieDAO;
    private final OfertyDAO ofertyDAO;
    private final PojazdyDAO pojazdyDAO;
    private final SprzedawcyDAO sprzedawcyDAO;
    private final SprzedazeDAO sprzedazeDAO;
    private final WynagrodzeniaDAO wynagrodzeniaDAO;
    private final KlienciDAO klienciDAO;

    public DatabaseManagementController(
            MarkiDAO markiDAO,
            ModeleDAO modeleDAO,
            SalonyDAO salonyDAO,
            adminAdresyDAO adminAdresyDAO,
            StanowiskaDAO stanowiskaDAO,
            FirmyPartnerskieDAO firmyPartnerskieDAO,
            OfertyDAO ofertyDAO,
            PojazdyDAO pojazdyDAO,
            SprzedawcyDAO sprzedawcyDAO,
            SprzedazeDAO sprzedazeDAO,
            WynagrodzeniaDAO wynagrodzeniaDAO,
            KlienciDAO klienciDAO
    ) {
        this.markiDAO = markiDAO;
        this.modeleDAO = modeleDAO;
        this.salonyDAO = salonyDAO;
        this.adminAdresyDAO = adminAdresyDAO;
        this.stanowiskaDAO = stanowiskaDAO;
        this.firmyPartnerskieDAO = firmyPartnerskieDAO;
        this.ofertyDAO = ofertyDAO;
        this.pojazdyDAO = pojazdyDAO;
        this.sprzedawcyDAO = sprzedawcyDAO;
        this.sprzedazeDAO = sprzedazeDAO;
        this.wynagrodzeniaDAO = wynagrodzeniaDAO;
        this.klienciDAO = klienciDAO;
    }

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

    // ===== ADRESY =====
    @GetMapping("/adresy")
    public String adresyList(Model model) {
        model.addAttribute("adresy", adminAdresyDAO.findAll());
        return "admin/database/adresy/list";
    }

    @GetMapping("/adresy/new")
    public String adresyNew(Model model) {
        model.addAttribute("adres", new Adresy());
        model.addAttribute("isEdit", false);
        return "admin/database/adresy/form";
    }

    @PostMapping("/adresy")
    public String adresyCreate(@ModelAttribute Adresy adres, RedirectAttributes redirectAttributes) {
        try {
            adminAdresyDAO.insert(adres);
            redirectAttributes.addFlashAttribute("success", "Adres został dodany pomyślnie");
            return "redirect:/admin/database/adresy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania adresu: " + e.getMessage());
            return "redirect:/admin/database/adresy/new";
        }
    }

    @GetMapping("/adresy/{id}/edit")
    public String adresyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Adresy> adresOpt = adminAdresyDAO.findById(id);
        if (adresOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Adres nie został znaleziony");
            return "redirect:/admin/database/adresy";
        }
        model.addAttribute("adres", adresOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/database/adresy/form";
    }

    @PostMapping("/adresy/{id}")
    public String adresyUpdate(@PathVariable Long id, @ModelAttribute Adresy adres, RedirectAttributes redirectAttributes) {
        try {
            adres.setIdAdresu(id);
            adminAdresyDAO.update(adres);
            redirectAttributes.addFlashAttribute("success", "Adres został zaktualizowany pomyślnie");
            return "redirect:/admin/database/adresy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji adresu: " + e.getMessage());
            return "redirect:/admin/database/adresy/" + id + "/edit";
        }
    }

    @PostMapping("/adresy/{id}/delete")
    public String adresyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminAdresyDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Adres został usunięty pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć adresu. Istnieją powiązane rekordy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania adresu: " + e.getMessage());
        }
        return "redirect:/admin/database/adresy";
    }

    // ===== POJAZDY =====
    @GetMapping("/pojazdy")
    public String pojazdyList(Model model) {
        model.addAttribute("pojazdy", pojazdyDAO.findAll());
        return "admin/database/pojazdy/list";
    }

    @GetMapping("/pojazdy/new")
    public String pojazdyNew(Model model) {
        model.addAttribute("pojazd", new Pojazdy());
        model.addAttribute("modele", modeleDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/pojazdy/form";
    }

    @PostMapping("/pojazdy")
    public String pojazdyCreate(@ModelAttribute Pojazdy pojazd, RedirectAttributes redirectAttributes) {
        try {
            pojazdyDAO.insert(pojazd);
            redirectAttributes.addFlashAttribute("success", "Pojazd został dodany pomyślnie");
            return "redirect:/admin/database/pojazdy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania pojazdu: " + e.getMessage());
            return "redirect:/admin/database/pojazdy/new";
        }
    }

    @GetMapping("/pojazdy/{id}/edit")
    public String pojazdyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pojazdy> pojazdOpt = pojazdyDAO.findById(id);
        if (pojazdOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Pojazd nie został znaleziony");
            return "redirect:/admin/database/pojazdy";
        }
        model.addAttribute("pojazd", pojazdOpt.get());
        model.addAttribute("modele", modeleDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/pojazdy/form";
    }

    @PostMapping("/pojazdy/{id}")
    public String pojazdyUpdate(@PathVariable Long id, @ModelAttribute Pojazdy pojazd, RedirectAttributes redirectAttributes) {
        try {
            pojazd.setIdPojazdu(id);
            pojazdyDAO.update(pojazd);
            redirectAttributes.addFlashAttribute("success", "Pojazd został zaktualizowany pomyślnie");
            return "redirect:/admin/database/pojazdy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji pojazdu: " + e.getMessage());
            return "redirect:/admin/database/pojazdy/" + id + "/edit";
        }
    }

    @PostMapping("/pojazdy/{id}/delete")
    public String pojazdyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pojazdyDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Pojazd został usunięty pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć pojazdu. Istnieją powiązane oferty.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania pojazdu: " + e.getMessage());
        }
        return "redirect:/admin/database/pojazdy";
    }

    // ===== OFERTY =====
    @GetMapping("/oferty")
    public String ofertyList(Model model) {
        model.addAttribute("oferty", ofertyDAO.findAll());
        return "admin/database/oferty/list";
    }

    @GetMapping("/oferty/new")
    public String ofertyNew(Model model) {
        model.addAttribute("oferta", new Oferty());
        model.addAttribute("salony", salonyDAO.findAll());
        model.addAttribute("pojazdy", pojazdyDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/oferty/form";
    }

    @PostMapping("/oferty")
    public String ofertyCreate(@ModelAttribute Oferty oferta, RedirectAttributes redirectAttributes) {
        try {
            ofertyDAO.insert(oferta);
            redirectAttributes.addFlashAttribute("success", "Oferta została dodana pomyślnie");
            return "redirect:/admin/database/oferty";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania oferty: " + e.getMessage());
            return "redirect:/admin/database/oferty/new";
        }
    }

    @GetMapping("/oferty/{id}/edit")
    public String ofertyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Oferty> ofertaOpt = ofertyDAO.findById(id);
        if (ofertaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Oferta nie została znaleziona");
            return "redirect:/admin/database/oferty";
        }
        model.addAttribute("oferta", ofertaOpt.get());
        model.addAttribute("salony", salonyDAO.findAll());
        model.addAttribute("pojazdy", pojazdyDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/oferty/form";
    }

    @PostMapping("/oferty/{id}")
    public String ofertyUpdate(@PathVariable Long id, @ModelAttribute Oferty oferta, RedirectAttributes redirectAttributes) {
        try {
            oferta.setIdOferty(id);
            ofertyDAO.update(oferta);
            redirectAttributes.addFlashAttribute("success", "Oferta została zaktualizowana pomyślnie");
            return "redirect:/admin/database/oferty";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji oferty: " + e.getMessage());
            return "redirect:/admin/database/oferty/" + id + "/edit";
        }
    }

    @PostMapping("/oferty/{id}/delete")
    public String ofertyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ofertyDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Oferta została usunięta pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć oferty. Istnieją powiązane sprzedaże.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania oferty: " + e.getMessage());
        }
        return "redirect:/admin/database/oferty";
    }

    // ===== SPRZEDAWCY =====
    @GetMapping("/sprzedawcy")
    public String sprzedawcyList(Model model) {
        model.addAttribute("sprzedawcy", sprzedawcyDAO.findAll());
        return "admin/database/sprzedawcy/list";
    }

    @GetMapping("/sprzedawcy/new")
    public String sprzedawcyNew(Model model) {
        model.addAttribute("sprzedawca", new Sprzedawcy());
        model.addAttribute("salony", salonyDAO.findAll());
        model.addAttribute("stanowiska", stanowiskaDAO.findAll());
        model.addAttribute("wynagrodzenia", wynagrodzeniaDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/sprzedawcy/form";
    }

    @PostMapping("/sprzedawcy")
    public String sprzedawcyCreate(@ModelAttribute Sprzedawcy sprzedawca, RedirectAttributes redirectAttributes) {
        try {
            sprzedawcyDAO.insert(sprzedawca);
            redirectAttributes.addFlashAttribute("success", "Sprzedawca został dodany pomyślnie");
            return "redirect:/admin/database/sprzedawcy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania sprzedawcy: " + e.getMessage());
            return "redirect:/admin/database/sprzedawcy/new";
        }
    }

    @GetMapping("/sprzedawcy/{id}/edit")
    public String sprzedawcyEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Sprzedawcy> sprzedawcaOpt = sprzedawcyDAO.findById(id);
        if (sprzedawcaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Sprzedawca nie został znaleziony");
            return "redirect:/admin/database/sprzedawcy";
        }
        model.addAttribute("sprzedawca", sprzedawcaOpt.get());
        model.addAttribute("salony", salonyDAO.findAll());
        model.addAttribute("stanowiska", stanowiskaDAO.findAll());
        model.addAttribute("wynagrodzenia", wynagrodzeniaDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/sprzedawcy/form";
    }

    @PostMapping("/sprzedawcy/{id}")
    public String sprzedawcyUpdate(@PathVariable Long id, @ModelAttribute Sprzedawcy sprzedawca, RedirectAttributes redirectAttributes) {
        try {
            sprzedawca.setIdPracownika(id);
            sprzedawcyDAO.update(sprzedawca);
            redirectAttributes.addFlashAttribute("success", "Sprzedawca został zaktualizowany pomyślnie");
            return "redirect:/admin/database/sprzedawcy";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji sprzedawcy: " + e.getMessage());
            return "redirect:/admin/database/sprzedawcy/" + id + "/edit";
        }
    }

    @PostMapping("/sprzedawcy/{id}/delete")
    public String sprzedawcyDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sprzedawcyDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Sprzedawca został usunięty pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć sprzedawcy. Istnieją powiązane sprzedaże.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania sprzedawcy: " + e.getMessage());
        }
        return "redirect:/admin/database/sprzedawcy";
    }

    // ===== SPRZEDAZE =====
    @GetMapping("/sprzedaze")
    public String sprzedazeList(Model model) {
        model.addAttribute("sprzedaze", sprzedazeDAO.findAll());
        return "admin/database/sprzedaze/list";
    }

    @GetMapping("/sprzedaze/new")
    public String sprzedazeNew(Model model) {
        model.addAttribute("sprzedaz", new Sprzedaze());
        model.addAttribute("pracownicy", sprzedawcyDAO.findAll());
        model.addAttribute("klienci", klienciDAO.findAll());
        model.addAttribute("oferty", ofertyDAO.findAll());
        model.addAttribute("isEdit", false);
        return "admin/database/sprzedaze/form";
    }

    @PostMapping("/sprzedaze")
    public String sprzedazeCreate(@ModelAttribute Sprzedaze sprzedaz, RedirectAttributes redirectAttributes) {
        try {
            sprzedazeDAO.insert(sprzedaz);
            redirectAttributes.addFlashAttribute("success", "Sprzedaż została dodana pomyślnie");
            return "redirect:/admin/database/sprzedaze";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania sprzedaży: " + e.getMessage());
            return "redirect:/admin/database/sprzedaze/new";
        }
    }

    @GetMapping("/sprzedaze/{id}/edit")
    public String sprzedazeEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Sprzedaze> sprzedazOpt = sprzedazeDAO.findById(id);
        if (sprzedazOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Sprzedaż nie została znaleziona");
            return "redirect:/admin/database/sprzedaze";
        }
        model.addAttribute("sprzedaz", sprzedazOpt.get());
        model.addAttribute("pracownicy", sprzedawcyDAO.findAll());
        model.addAttribute("klienci", klienciDAO.findAll());
        model.addAttribute("oferty", ofertyDAO.findAll());
        model.addAttribute("isEdit", true);
        return "admin/database/sprzedaze/form";
    }

    @PostMapping("/sprzedaze/{id}")
    public String sprzedazeUpdate(@PathVariable Long id, @ModelAttribute Sprzedaze sprzedaz, RedirectAttributes redirectAttributes) {
        try {
            sprzedaz.setIdSprzedazy(id);
            sprzedazeDAO.update(sprzedaz);
            redirectAttributes.addFlashAttribute("success", "Sprzedaż została zaktualizowana pomyślnie");
            return "redirect:/admin/database/sprzedaze";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji sprzedaży: " + e.getMessage());
            return "redirect:/admin/database/sprzedaze/" + id + "/edit";
        }
    }

    @PostMapping("/sprzedaze/{id}/delete")
    public String sprzedazeDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sprzedazeDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Sprzedaż została usunięta pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć sprzedaży. Istnieją powiązane rekordy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania sprzedaży: " + e.getMessage());
        }
        return "redirect:/admin/database/sprzedaze";
    }

    // ===== WYNAGRODZENIA =====
    @GetMapping("/wynagrodzenia")
    public String wynagrodzeniaList(Model model) {
        model.addAttribute("wynagrodzenia", wynagrodzeniaDAO.findAll());
        return "admin/database/wynagrodzenia/list";
    }

    @GetMapping("/wynagrodzenia/new")
    public String wynagrodzeniaNew(Model model) {
        model.addAttribute("wynagrodzenie", new Wynagrodzenia());
        model.addAttribute("isEdit", false);
        return "admin/database/wynagrodzenia/form";
    }

    @PostMapping("/wynagrodzenia")
    public String wynagrodzeniaCreate(@ModelAttribute Wynagrodzenia wynagrodzenie, RedirectAttributes redirectAttributes) {
        try {
            wynagrodzeniaDAO.insert(wynagrodzenie);
            redirectAttributes.addFlashAttribute("success", "Wynagrodzenie zostało dodane pomyślnie");
            return "redirect:/admin/database/wynagrodzenia";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas dodawania wynagrodzenia: " + e.getMessage());
            return "redirect:/admin/database/wynagrodzenia/new";
        }
    }

    @GetMapping("/wynagrodzenia/{id}/edit")
    public String wynagrodzeniaEdit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Wynagrodzenia> wynagrodzenieOpt = wynagrodzeniaDAO.findById(id);
        if (wynagrodzenieOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Wynagrodzenie nie zostało znalezione");
            return "redirect:/admin/database/wynagrodzenia";
        }
        model.addAttribute("wynagrodzenie", wynagrodzenieOpt.get());
        model.addAttribute("isEdit", true);
        return "admin/database/wynagrodzenia/form";
    }

    @PostMapping("/wynagrodzenia/{id}")
    public String wynagrodzeniaUpdate(@PathVariable Long id, @ModelAttribute Wynagrodzenia wynagrodzenie, RedirectAttributes redirectAttributes) {
        try {
            wynagrodzenie.setIdWynagrodzenia(id);
            wynagrodzeniaDAO.update(wynagrodzenie);
            redirectAttributes.addFlashAttribute("success", "Wynagrodzenie zostało zaktualizowane pomyślnie");
            return "redirect:/admin/database/wynagrodzenia";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas aktualizacji wynagrodzenia: " + e.getMessage());
            return "redirect:/admin/database/wynagrodzenia/" + id + "/edit";
        }
    }

    @PostMapping("/wynagrodzenia/{id}/delete")
    public String wynagrodzeniaDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            wynagrodzeniaDAO.delete(id);
            redirectAttributes.addFlashAttribute("success", "Wynagrodzenie zostało usunięte pomyślnie");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Nie można usunąć wynagrodzenia. Istnieją przypisani pracownicy.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Błąd podczas usuwania wynagrodzenia: " + e.getMessage());
        }
        return "redirect:/admin/database/wynagrodzenia";
    }
}