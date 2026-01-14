package bada_project.SpringApplication.auth;

import bada_project.SpringApplication.dao.KlienciDAO;
import bada_project.SpringApplication.dao.PracownicyDAO;
import bada_project.SpringApplication.dao.KlienciDAO.ClientAuthRow;
import bada_project.SpringApplication.dao.PracownicyDAO.EmployeeAuthRow;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final KlienciDAO klienciDAO;
    private final PracownicyDAO pracownicyDAO;

    public AuthService(KlienciDAO klienciDAO, PracownicyDAO pracownicyDAO) {
        this.klienciDAO = klienciDAO;
        this.pracownicyDAO = pracownicyDAO;
    }

    /*
     * =========================
     * LOGIN — używane przez UnifiedUserDetailsService
     * =========================
     */

    public Optional<EmployeeAuthRow> findEmployeeByEmail(String email) {
        return pracownicyDAO.findAuthByEmail(normalizeEmail(email));
    }

    public Optional<ClientAuthRow> findClientByEmail(String email) {
        return klienciDAO.findAuthByEmail(normalizeEmail(email));
    }

    /*
     * =========================
     * REGISTER — używane przez RegisterService
     * =========================
     */

    public void registerClient(RegisterForm form, String passwordHash) {

        String email = normalizeEmail(form.getEmail());

        // ===== HARD CHECKS (ostatnia linia obrony) =====
        if (!email.matches("^[a-z0-9@._+-]{5,100}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!form.getPhone().matches("^\\+[1-9]\\d{7,14}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        if (klienciDAO.existsByEmail(email)) {
            throw new IllegalStateException("Client with this email already exists");
        }

        // DAO robi całą robotę transakcyjną
        klienciDAO.insertClient(form, passwordHash);
    }

    /*
     * =========================
     * UTILS
     * =========================
     */
    private String normalizeEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email is null");
        }
        return email.trim().toLowerCase();
    }
}
