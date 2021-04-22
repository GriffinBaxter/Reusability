
INSERT INTO `address` (id, street_number, street_name, city, region, country, postcode) VALUES (1, '109', 'Blue Street', 'Upper Hutt', 'Wellington', 'New Zealand', '5018');

INSERT INTO `user` (id, first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES (2, 'Bob', 'Doe', 'S', 'Johnny', 'Biography', 'email@email.com', DATE'2000-05-14', '0271316', 1, 'Password123!', DATE'2019-05-14', 'DEFAULTGLOBALAPPLICATIONADMIN');


