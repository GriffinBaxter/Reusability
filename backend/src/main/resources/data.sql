INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('109', 'Blue Street', 'Upper Hutt', 'Wellington', 'New Zealand', '5018');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('333', 'Ilam Road', 'Christchurch', 'Canterbury', 'New Zealand', '90210');

INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Bob', 'Doe', 'S', 'Johnny', 'Biography', 'emailDGAA@email.com', DATE'2000-05-14', '0271316', 1, 'Password123!', DATE'2019-05-14', 'DEFAULTGLOBALAPPLICATIONADMIN');


INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Bob', 'Doe', 'S', 'Johnny', 'Biography', 'emailUSER@email.com', DATE'2000-05-14', '0271316', 2, 'Password123!', DATE'2019-05-14', 'USER');


