package bada_project.SpringApplication.user.profile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserProfileDAO dao;

    public UserProfileService(UserProfileDAO dao) {
        this.dao = dao;
    }

    public UserProfile getByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Transactional
    public void updateProfile(UserProfile p) {
        dao.updateClient(p);
        dao.updateAddress(p);
    }
}
