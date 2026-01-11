package bada_project.SpringApplication.auth;

import bada_project.SpringApplication.dao.AdresyDAO;
import bada_project.SpringApplication.dao.KlienciDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final KlienciDAO klienciDAO;
    private final AdresyDAO adresyDAO;
    private final PasswordEncoder passwordEncoder;

    public AuthService(KlienciDAO klienciDAO, AdresyDAO adresyDAO, PasswordEncoder passwordEncoder) {
        this.klienciDAO = klienciDAO;
        this.adresyDAO = adresyDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterForm f) {

        if (klienciDAO.existsByEmail(f.getEmail())) {
            throw new IllegalArgumentException("Konto z takim e-mailem już istnieje.");
        }

        String hash = passwordEncoder.encode(f.getPassword());

        Long idAdresu = adresyDAO.insertAdresAndReturnId(f);

        klienciDAO.insertClient(f, hash);
    }
}
