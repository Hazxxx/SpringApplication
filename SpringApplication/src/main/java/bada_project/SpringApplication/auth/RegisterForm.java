package bada_project.SpringApplication.auth;

import jakarta.validation.constraints.*;

public class RegisterForm {

    // ===== CLIENT =====

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain upper, lower and digit"
    )
    private String password;

    @NotBlank
    @Size(min = 2, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 40)
    private String lastName;

    // 📞 E.164 — bez spacji
    @NotBlank
    @Pattern(
            regexp = "^\\+[1-9]\\d{7,14}$",
            message = "Phone must be in format +48123456789"
    )
    private String phone;

    // ===== ADDRESS =====

    @NotBlank
    @Size(max = 80)
    private String street;

    @NotBlank
    @Size(max = 10)
    private String houseNumber;

    @Size(max = 10)
    private String apartmentNumber;

    @NotBlank
    @Pattern(
            regexp = "^\\d{2}-\\d{3}$",
            message = "Postal code must be XX-XXX"
    )
    private String postalCode;

    @NotBlank
    @Size(max = 60)
    private String city;

    // ===== GETTERS / SETTERS =====

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase();
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.replaceAll("\\s+", "");
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street.trim(); }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber.trim(); }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber == null ? null : apartmentNumber.trim();
    }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode.trim(); }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city.trim(); }
}
