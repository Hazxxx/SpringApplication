package bada_project.SpringApplication.user.employee;

import bada_project.SpringApplication.dao.PracownicyDAO;
import bada_project.SpringApplication.dao.ReservationDAO;
import bada_project.SpringApplication.dao.VehicleDAO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee")
// Access control is also handled in SecurityConfiguration, but good to have
// here too
public class EmployeeController {

    private final PracownicyDAO pracownicyDAO;
    private final ReservationDAO reservationDAO;
    private final VehicleDAO vehicleDAO;

    public EmployeeController(PracownicyDAO pracownicyDAO, ReservationDAO reservationDAO, VehicleDAO vehicleDAO) {
        this.pracownicyDAO = pracownicyDAO;
        this.reservationDAO = reservationDAO;
        this.vehicleDAO = vehicleDAO;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Integer employeeId = pracownicyDAO.findIdByEmail(email);
        if (employeeId == null) {
            return "redirect:/logout"; // Should not happen for authenticated employee
        }

        var reservations = reservationDAO.findAllByEmployeeId(employeeId);
        model.addAttribute("reservations", reservations);

        var assignedVehicles = vehicleDAO.findAssignedToEmployee(employeeId);
        model.addAttribute("assignedVehicles", assignedVehicles);

        return "employee/dashboard";
    }

    @PostMapping("/reservations/{id}/approve")
    public String approveReservation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservationDAO.approve(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation approved and Sale recorded.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving reservation: " + e.getMessage());
        }
        return "redirect:/employee/dashboard";
    }

    @PostMapping("/reservations/{id}/reject")
    public String rejectReservation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservationDAO.updateStatus(id, "ODRZUCONA");
            redirectAttributes.addFlashAttribute("successMessage", "Reservation rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting reservation.");
        }
        return "redirect:/employee/dashboard";
    }
}
