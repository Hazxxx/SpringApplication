package bada_project.SpringApplication.auth;

import bada_project.SpringApplication.dao.KlienciDAO;
import bada_project.SpringApplication.dao.PracownicyDAO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified UserDetailsService that handles both:
 * - Clients (KLIENCI) with ROLE_USER
 * - Employees (PRACOWNICY) with ROLE_USER and optionally ROLE_ADMIN
 */
@Service
public class UnifiedUserDetailsService implements UserDetailsService {

    private final KlienciDAO klienciDAO;
    private final PracownicyDAO pracownicyDAO;

    public UnifiedUserDetailsService(KlienciDAO klienciDAO, PracownicyDAO pracownicyDAO) {
        this.klienciDAO = klienciDAO;
        this.pracownicyDAO = pracownicyDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // ===== SANITYZACJA WEJŚCIA =====
        if (email == null) {
            throw new UsernameNotFoundException("Email is null");
        }

        String login = email.trim().toLowerCase();

        // twarda walidacja formatu – zanim poleci do DB
        if (!login.matches("^[a-z0-9@._+-]{5,100}$")) {
            System.out.println(">>> INVALID LOGIN FORMAT: " + login);
            throw new UsernameNotFoundException("Invalid login format");
        }

        System.out.println(">>> LOGIN ATTEMPT: " + login);

        // ===== 1. PRACOWNIK =====
        var employeeOpt = pracownicyDAO.findAuthByEmail(login);
        if (employeeOpt.isPresent()) {
            var employee = employeeOpt.get();

            System.out.println(">>> EMPLOYEE FOUND: " + employee.email());
            System.out.println(">>> Position: " + employee.position());
            System.out.println(">>> Is Admin: " + employee.isAdmin());
            System.out.println(">>> PASSWORD HASH: " + employee.passwordHash());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

            if (employee.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                System.out.println(">>> GRANTED: ROLE_USER, ROLE_EMPLOYEE, ROLE_ADMIN");
            } else {
                System.out.println(">>> GRANTED: ROLE_USER, ROLE_EMPLOYEE");
            }

            return new User(
                    employee.email(),
                    employee.passwordHash(),
                    authorities
            );
        }

        // ===== 2. KLIENT =====
        var clientOpt = klienciDAO.findAuthByEmail(login);
        if (clientOpt.isPresent()) {
            var client = clientOpt.get();

            System.out.println(">>> CLIENT FOUND: " + client.email());
            System.out.println(">>> PASSWORD HASH: " +
                    client.passwordHash().substring(0, Math.min(20, client.passwordHash().length())) + "...");
            System.out.println(">>> GRANTED: ROLE_USER");

            return new User(
                    client.email(),
                    client.passwordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        // ===== BRAK USERA =====
        System.out.println(">>> USER NOT FOUND: " + login);
        throw new UsernameNotFoundException("User not found: " + login);
    }
}
