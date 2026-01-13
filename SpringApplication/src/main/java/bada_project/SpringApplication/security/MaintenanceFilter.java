package bada_project.SpringApplication.security;

import bada_project.SpringApplication.admin.SystemFlagsDAO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MaintenanceFilter extends OncePerRequestFilter {

    private final SystemFlagsDAO dao;

    public MaintenanceFilter(SystemFlagsDAO dao) {
        this.dao = dao;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        System.out.println("=== MAINTENANCE FILTER DEBUG ===");
        System.out.println("Path: " + path);

        // === ZAWSZE DOZWOLONE ŚCIEŻKI ===
        if (path.startsWith("/maintenance")
                || path.startsWith("/assets")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/webjars")
                || path.equals("/login")
                || path.equals("/logout")) {
            System.out.println("Decision: ALLOWED (public path)");
            filterChain.doFilter(request, response);
            return;
        }

        // === SPRAWDŹ CZY MAINTENANCE JEST WŁĄCZONY ===
        boolean maintenanceEnabled = dao.isMaintenanceEnabled();
        System.out.println("Maintenance enabled: " + maintenanceEnabled);

        if (!maintenanceEnabled) {
            System.out.println("Decision: ALLOWED (maintenance disabled)");
            filterChain.doFilter(request, response);
            return;
        }

        // === SPRAWDŹ CZY UŻYTKOWNIK JEST ADMINEM ===
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + auth);

        if (auth != null) {
            System.out.println("Authenticated: " + auth.isAuthenticated());
            System.out.println("Principal: " + auth.getPrincipal());
            System.out.println("Authorities: " + auth.getAuthorities());

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            System.out.println("Is Admin: " + isAdmin);

            if (auth.isAuthenticated() && isAdmin) {
                System.out.println("Decision: ALLOWED (admin user)");
                filterChain.doFilter(request, response);
                return;
            }
        }

        // === ZABLOKUJ WSZYSTKICH INNYCH ===
        System.out.println("Decision: BLOCKED - redirecting to /maintenance");
        response.sendRedirect("/maintenance");
    }
}