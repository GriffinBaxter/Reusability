
-- INSERT INTO address (street_number, street_name, city, region, country, postcode) VALUES ("1", "Blue Street", "Upper Hutt", "Wellington", "New Zealand", "5018")

INSERT INTO user (first_name, last_name, middle_name, nickname, bio, email, created, address_id, password, date_of_birth, role)
VALUES ("Bob", "Doe", "S", "Johnny", "Biography", "email@email.com", LocalDate.of(2000, 2, 2), "0271316", "1", "Password123!",
        LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)), "DEFAULTGLOBALAPPLICATIONADMIN"))


