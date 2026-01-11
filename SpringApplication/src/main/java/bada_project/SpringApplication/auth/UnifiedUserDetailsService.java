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

        System.out.println(">>> LOGIN ATTEMPT: " + email);

        // First, try to find as employee
        var employeeOpt = pracownicyDAO.findAuthByEmail(email.toLowerCase());
        if (employeeOpt.isPresent()) {
            var employee = employeeOpt.get();

            System.out.println(">>> EMPLOYEE FOUND: " + employee.email());
            System.out.println(">>> Position: " + employee.position());
            System.out.println(">>> Is Admin: " + employee.isAdmin());
            System.out.println(">>> PASSWORD HASH: " + employee.passwordHash());

            // Build authorities based on admin status
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            if (employee.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                System.out.println(">>> GRANTED: ROLE_USER, ROLE_ADMIN");
            } else {
                System.out.println(">>> GRANTED: ROLE_USER");
            }

            return new User(
                    employee.email(),
                    employee.passwordHash(),
                    authorities
            );
        }

        // If not found as employee, try as client
        var clientOpt = klienciDAO.findAuthByEmail(email.toLowerCase());
        if (clientOpt.isPresent()) {
            var client = clientOpt.get();

            System.out.println(">>> CLIENT FOUND: " + client.email());
            System.out.println(">>> PASSWORD HASH: " + client.passwordHash().substring(0, 20) + "...");
            System.out.println(">>> GRANTED: ROLE_USER");

            return new User(
                    client.email(),
                    client.passwordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        // Not found in either table
        System.out.println(">>> USER NOT FOUND: " + email);
        throw new UsernameNotFoundException("User not found: " + email);
    }
}