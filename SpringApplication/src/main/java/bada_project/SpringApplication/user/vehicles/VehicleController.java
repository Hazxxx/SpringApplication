package bada_project.SpringApplication.user.vehicles;

import bada_project.SpringApplication.dao.VehicleDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user/vehicles")
public class VehicleController {

    private final VehicleDAO vehicleDAO;

    public VehicleController(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
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
            e.printStackTrace();

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
            e.printStackTrace();

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
            e.printStackTrace();
            model.addAttribute("error", "Error loading vehicle details: " + e.getMessage());
            return "redirect:/user/vehicles";
        }
    }
}