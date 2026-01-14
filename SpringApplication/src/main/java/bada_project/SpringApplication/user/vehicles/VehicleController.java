package bada_project.SpringApplication.user.vehicles;

import bada_project.SpringApplication.dao.KlienciDAO;
import bada_project.SpringApplication.dao.ReservationDAO;
import bada_project.SpringApplication.dao.VehicleDAO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user/vehicles")
public class VehicleController {

    private final VehicleDAO vehicleDAO;
    private final ReservationDAO reservationDAO;
    private final KlienciDAO klienciDAO;

    public VehicleController(VehicleDAO vehicleDAO, ReservationDAO reservationDAO, KlienciDAO klienciDAO) {
        this.vehicleDAO = vehicleDAO;
        this.reservationDAO = reservationDAO;
        this.klienciDAO = klienciDAO;
    }

    /**
     * Strona katalogu pojazdów
     */
    @GetMapping
    public String showCatalog(Model model) {
        try {
            List<Vehicle> vehicles = vehicleDAO.findAllAvailable();

            // Jeśli null, użyj pustej listy
            if (vehicles == null) {
                vehicles = Collections.emptyList();
            }

            model.addAttribute("vehicles", vehicles);
            model.addAttribute("filter", new VehicleSearchFilter());

            // Dane do filtrów - obsługa null
            List<String> marki = vehicleDAO.findAllMarki();
            List<String> typyNadwozia = vehicleDAO.findAllTypyNadwozia();
            List<String> typyPaliwa = vehicleDAO.findAllTypyPaliwa();

            model.addAttribute("marki", marki != null ? marki : Collections.emptyList());
            model.addAttribute("typyNadwozia", typyNadwozia != null ? typyNadwozia : Collections.emptyList());
            model.addAttribute("typyPaliwa", typyPaliwa != null ? typyPaliwa : Collections.emptyList());

            return "user/vehicles/catalog";

        } catch (Exception e) {
            System.err.println("ERROR in showCatalog: " + e.getMessage());


            // Nawet przy błędzie, pokaż stronę z pustymi danymi
            model.addAttribute("vehicles", Collections.emptyList());
            model.addAttribute("filter", new VehicleSearchFilter());
            model.addAttribute("marki", Collections.emptyList());
            model.addAttribute("typyNadwozia", Collections.emptyList());
            model.addAttribute("typyPaliwa", Collections.emptyList());
            model.addAttribute("error", "Error loading vehicles: " + e.getMessage());

            return "user/vehicles/catalog";
        }
    }

    /**
     * Wyszukiwanie z filtrami
     */
    @PostMapping("/search")
    public String searchVehicles(@ModelAttribute("filter") VehicleSearchFilter filter, Model model) {
        try {
            List<Vehicle> vehicles;

            // Jeśli filter ma jakiekolwiek wartości, użyj wyszukiwania
            if (filter.hasAnyFilter()) {
                vehicles = vehicleDAO.search(filter);
            } else {
                vehicles = vehicleDAO.findAllAvailable();
            }

            // Obsługa null
            if (vehicles == null) {
                vehicles = Collections.emptyList();
            }

            model.addAttribute("vehicles", vehicles);
            model.addAttribute("filter", filter);

            // Dane do filtrów
            List<String> marki = vehicleDAO.findAllMarki();
            List<String> typyNadwozia = vehicleDAO.findAllTypyNadwozia();
            List<String> typyPaliwa = vehicleDAO.findAllTypyPaliwa();

            model.addAttribute("marki", marki != null ? marki : Collections.emptyList());
            model.addAttribute("typyNadwozia", typyNadwozia != null ? typyNadwozia : Collections.emptyList());
            model.addAttribute("typyPaliwa", typyPaliwa != null ? typyPaliwa : Collections.emptyList());

            return "user/vehicles/catalog";

        } catch (Exception e) {
            System.err.println("ERROR in searchVehicles: " + e.getMessage());


            model.addAttribute("vehicles", Collections.emptyList());
            model.addAttribute("filter", filter);
            model.addAttribute("marki", Collections.emptyList());
            model.addAttribute("typyNadwozia", Collections.emptyList());
            model.addAttribute("typyPaliwa", Collections.emptyList());
            model.addAttribute("error", "Error searching vehicles: " + e.getMessage());

            return "user/vehicles/catalog";
        }
    }

    /**
     * Szczegóły pojazdu
     */
    @GetMapping("/{id}")
    public String showVehicleDetails(@PathVariable("id") Integer idPojazdu, Model model) {
        try {
            Vehicle vehicle = vehicleDAO.findById(idPojazdu);

            if (vehicle == null) {
                model.addAttribute("error", "Vehicle not found");
                return "redirect:/user/vehicles";
            }

            model.addAttribute("vehicle", vehicle);
            return "user/vehicles/details";

        } catch (Exception e) {
            System.err.println("ERROR in showVehicleDetails: " + e.getMessage());

            model.addAttribute("error", "Error loading vehicle details: " + e.getMessage());
            return "redirect:/user/vehicles";
        }
    }

    @PostMapping("/{id}/reserve")
    public String reserveVehicle(@PathVariable("id") Integer idOferty, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Integer idKlienta = klienciDAO.findIdByEmail(email);
            if (idKlienta == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Could not identify user client profile.");
                return "redirect:/user/vehicles/" + idOferty;
            }

            // Check if vehicle exists
            Vehicle v = vehicleDAO.findById(idOferty);
            if (v == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found.");
                return "redirect:/user/vehicles";
            }

            // Check if vehicle is already sold/reserved
            if (v.isSprzedany()) {
                redirectAttributes.addFlashAttribute("errorMessage", "This vehicle is already sold or reserved.");
                return "redirect:/user/vehicles/" + idOferty;
            }

            // Check if user already reserved this specific vehicle
            if (reservationDAO.existsForOffer(v.getIdOferty())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Reservation already exists for this vehicle.");
                return "redirect:/user/vehicles/" + idOferty;
            }

            reservationDAO.save(v.getIdOferty(), idKlienta);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Vehicle reserved successfully! Wait for seller contact.");
            return "redirect:/user/vehicles/" + idOferty;

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("errorMessage", "Reservation failed: " + e.getMessage());
            return "redirect:/user/vehicles/" + idOferty;
        }
    }
}