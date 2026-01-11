package bada_project.SpringApplication.auth;

import bada_project.SpringApplication.dao.KlienciDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final KlienciDAO dao;
    private final PasswordEncoder encoder;

    public RegisterService(KlienciDAO dao, PasswordEncoder encoder) {
        this.dao = dao;
        this.encoder = encoder;
    }

    public void register(RegisterForm form) {
        String email = form.getEmail().trim().toLowerCase();

        if (dao.existsByEmail(email)) {
            throw new IllegalArgumentException("Konto o takim emailu już istnieje.");
        }

        String hash = encoder.encode(form.getPassword());
        form.setEmail(email);

        dao.insertClient(form, hash);
    }
}
