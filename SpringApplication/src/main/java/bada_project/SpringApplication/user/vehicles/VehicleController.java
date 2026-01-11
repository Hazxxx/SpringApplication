package bada_project.SpringApplication.user.vehicles;

import bada_project.SpringApplication.dao.VehicleDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<Vehicle> vehicles = vehicleDAO.findAllAvailable();

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("filter", new VehicleSearchFilter());

        // Dane do filtrów
        model.addAttribute("marki", vehicleDAO.findAllMarki());
        model.addAttribute("typyNadwozia", vehicleDAO.findAllTypyNadwozia());
        model.addAttribute("typyPaliwa", vehicleDAO.findAllTypyPaliwa());

        return "user/vehicles/catalog";
    }

    /**
     * Wyszukiwanie z filtrami
     */
    @PostMapping("/search")
    public String searchVehicles(@ModelAttribute("filter") VehicleSearchFilter filter, Model model) {
        List<Vehicle> vehicles;

        // Jeśli filter ma jakiekolwiek wartości, użyj wyszukiwania
        if (filter.hasAnyFilter()) {
            vehicles = vehicleDAO.search(filter);
        } else {
            vehicles = vehicleDAO.findAllAvailable();
        }

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("filter", filter);

        // Dane do filtrów
        model.addAttribute("marki", vehicleDAO.findAllMarki());
        model.addAttribute("typyNadwozia", vehicleDAO.findAllTypyNadwozia());
        model.addAttribute("typyPaliwa", vehicleDAO.findAllTypyPaliwa());

        return "user/vehicles/catalog";
    }

    /**
     * Szczegóły pojazdu
     */
    @GetMapping("/{id}")
    public String showVehicleDetails(@PathVariable("id") Integer idPojazdu, Model model) {
        Vehicle vehicle = vehicleDAO.findById(idPojazdu);

        if (vehicle == null) {
            model.addAttribute("error", "Pojazd nie został znaleziony");
            return "redirect:/user/vehicles";
        }

        model.addAttribute("vehicle", vehicle);
        return "user/vehicles/details";
    }
}