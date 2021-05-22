/* Addresses for Users (20) */
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('109', 'Blue Street', 'Upper Hutt', 'Wellington', 'New Zealand', '5018');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('333', 'Ilam Road', 'Christchurch', 'Canterbury', 'New Zealand', '90210');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('57', 'Sydney Highway', 'Shire of Cocos Islands', 'West Island', 'Cocos (Keeling) Islands', '9732');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('47993', 'Norwood Garden', 'Mambere-Kadei', 'Central African Republic', 'Africa', '3428');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('129', 'Mastic Trail', 'Frank Sound Cayman Islands', 'Caribbean', 'North America', '3442');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('80416', 'Rodney Street', 'Jon Loop', 'Shaanxi', 'China', '2113');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('9205', 'Monique Vista', 'Bururi', 'Bigomogomo', 'Africa', '1000');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('240', 'Newson Street', 'Bernhard Run', 'Southland', 'New Zealand', '2839');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('16', 'Presidente Peron', 'Funes', 'Santa Fe', 'Argentina', 'S2132');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('190', 'Fort Washington Avenue', 'New York', 'New York', 'United States', '10040');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('32', 'Hunter Avenue', 'Fairview Shores', 'Florida', 'United States', '32804');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('32', 'Via Giuseppe Di Vittorio', 'Rovereto', 'Trentino-Alto Adige/Südtirol', 'Italy', '38068');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('3434', 'Russell Street', 'Detroit', 'Michigan', 'United States', '48207');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('16', 'Barnards Road', 'Scarborough', 'England', 'United Kingdom', 'YO21 1UX');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('199', 'Kwa Jongo Street', 'Dar es Salaam', 'Coastal Zone', 'Tanzania', '78570');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('76', 'Milltown Road', 'Strabane', 'Northern Ireland', 'United Kingdom', 'BT82 8AS');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('87', 'Ansancheonnam-ro', 'Ansan-si', 'Gyeonggi-do', 'South Korea', '15483');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('98', 'Sqaq il-Qenċ', 'Zebbug', 'Southern Region', 'Malta', 'ZBG9019');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('19', 'Heping Road', 'Zebbug', 'Tianjin', 'China', '300010');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('19b', 'IGIS Road', 'Awan Town', 'Islamabad Capital Territory', 'Pakistan', '44110');

/* Users (20) */
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Alex', 'Doe', 'Joe', 'Johnny', 'Biography', 'emailUSER@email.com', DATE'2008-02-02', '0271317', 5, 'UGFzc3dvcmQxMjMh', DATE'2021-02-12', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Chad', 'Taylor', 'S', 'Chaddy', 'Biography', 'chad.taylor@example.com', DATE'2006-02-01', '0271316678', 3, 'UGFzc3dvcmQxMjMh', DATE'2021-03-14', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Naomi', 'Wilson', 'I', 'GM', 'Biography', 'naomi.wilson@example.com', DATE'2007-09-08', '54335522', 4, 'UGFzc3dvcmQxMjMh', DATE'2021-03-01', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Seth', 'Murphy', 'S', 'Sethy', 'Biography', 'seth.murphy@example.com', DATE'2003-06-19', '027188316', 5, 'UGFzc3dvcmQxMjMh', DATE'2021-03-03', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Minttu', 'Wainio', 'Anna', 'Minnie', 'Biography', 'minttu.wainio@example.com', DATE'2004-07-28', '0290316', 6, 'UGFzc3dvcmQxMjMh', DATE'2021-03-05', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Francisca', 'Benitez', 'Tina', 'Fran', 'Biography', 'francisca.benitez@example.com', DATE'2008-09-10', '12334456', 7, 'UGFzc3dvcmQxMjMh', DATE'2021-01-08', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Francisca', 'Bznitez', 'Tessa', 'Fran', 'Biography', 'francisca.bznitez@example.com', DATE'2001-07-17', '967352531', 8, 'UGFzc3dvcmQxMjMh', DATE'2021-01-01', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Frank', 'Smith', 'J', 'Frankie', 'Biography', 'frank.j.smith@email.com', DATE'2000-05-14', '0271316', 1, 'UGFzc3dvcmQxMjMh', DATE'2019-05-14', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Lina', 'Patterson', 'Jose Mari', 'Lina', 'Da', 'linap@email.com', DATE'2000-02-14', '0275431316', 9, 'UGFzc3dvcmQxMjMh', DATE'2010-05-14', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Evelia', 'Blanxart', 'Robert', 'Robby', 'I like art!', 'everblanxart@gmail.com', DATE'2007-04-13', '0272331323', 10, 'UGFzc3dvcmQxMjMh', DATE'2019-05-20', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Mirta', 'Lovel', 'Juan', 'Love', 'Pancakes', 'mjl25@uclive.ac.nz', DATE'1999-02-22', '0273321116', 11, 'UGFzc3dvcmQxMjMh', DATE'2021-01-20', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Jordan', 'Piper', 'Mervyn', 'Bag', 'I do not play the bagpipes!', 'jordanpiper@yahoo.com', DATE'2005-01-22', '0272121116', 12, 'UGFzc3dvcmQxMjMh', DATE'2019-05-22', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Ife', 'Weston', 'Missie', 'Missie', 'Miss me.', 'missie@gmail.com', DATE'2004-05-17', '0271316323', 13, 'UGFzc3dvcmQxMjMh', DATE'2014-02-14', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Pia', 'Kemp', 'Alex', 'Hemp', 'My cool bio.', 'piakemp13@email.com', DATE'2008-01-13', '0271316231', 14, 'UGFzc3dvcmQxMjMh', DATE'2020-01-12', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Alyce', 'Gibbs', 'Teddie', 'Teddie', 'Looking for cheap teddies.', 'alycegibbs@gmail.com', DATE'1965-02-19', '0271316943', 15, 'UGFzc3dvcmQxMjMh', DATE'2019-03-28', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Casandra', 'Dane', 'Fen', 'Cassie', 'I am not from Denmark!', 'cassie@dane.com', DATE'1982-01-17', '0271316226', 16, 'UGFzc3dvcmQxMjMh', DATE'2018-12-13', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Bennett', 'Garner', 'Finola', 'Garnish', 'I like apples.', 'garnish@yahoo.com', DATE'1989-09-12', '0271316096', 17, 'UGFzc3dvcmQxMjMh', DATE'2021-02-02', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Da', 'Meadows', 'Randolf', 'Da Baby', 'Looking for cheap bread.', 'dababy@email.com', DATE'2006-02-18', '0271316989', 18, 'UGFzc3dvcmQxMjMh', DATE'2019-12-12', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Tanner', 'Ilene', 'Levitt', 'Levy', 'Biography', 'tannerlevitt@gmail.com', DATE'1999-12-14', '0271316234', 19, 'UGFzc3dvcmQxMjMh', DATE'2020-01-12', 'USER');
INSERT INTO `user` (first_name, last_name, middle_name, nickname, bio, email, date_of_birth, phone_number, address_id, password, created, role) VALUES ('Juliana', 'Haden', 'Celeste', 'Jules', 'Biography', 'jules@email.com', DATE'2000-05-19', '0271316534', 20, 'UGFzc3dvcmQxMjMh', DATE'2021-02-03', 'USER');

/* Addresses for Businesses (3) */
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('86', 'High Street', 'Picton', 'Marlborough', 'New Zealand', '7220');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('1849', 'C Street Northwest', 'Washington', 'District of Columbia', 'United States', '20240');
INSERT INTO `address` (street_number, street_name, city, region, country, postcode) VALUES ('7', 'Wangjing Zhonghuan Nanlu', 'Chaoyang District', 'Beijing', 'China', '100102');


/* Businesses (3) */
INSERT INTO `business` (business_type, created, description, name, primary_administrator_id, address_id) VALUES ('RETAIL_TRADE', DATE'2021-02-12', 'Description', 'Brink Food', 1, 21);
INSERT INTO `business` (business_type, created, description, name, primary_administrator_id, address_id) VALUES ('CHARITABLE_ORGANISATION', DATE'2021-02-14', 'Description', 'Sunburst Waste', 10, 22);
INSERT INTO `business` (business_type, created, description, name, primary_administrator_id, address_id) VALUES ('RETAIL_TRADE', DATE'2021-02-01', 'Description', 'Fringe Wasteless', 6, 23);

/* Link Users to Businesses */
INSERT INTO `users_businesses` (user_id, businesses_id) VALUES (1, 1);
INSERT INTO `users_businesses` (user_id, businesses_id) VALUES (10, 2);
INSERT INTO `users_businesses` (user_id, businesses_id) VALUES (11, 2);
INSERT INTO `users_businesses` (user_id, businesses_id) VALUES (13, 2);
INSERT INTO `users_businesses` (user_id, businesses_id) VALUES (6, 3);

/* Products for Business 1 (9) */
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'MANDARIN', DATE'2021-05-12', 'Fresh Produce Mandarins Satsuma', 'Countdown', 'Mandarins', 4.50);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'TP-12PK', DATE'2021-05-12', 'Purex toilet tissue offers you the features of a premium toilet tissue at a midmarket price. Double embossed for softness. Biodegradable.', 'Purex', 'Purex Toilet Paper 12Pk', 4.80);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'BEEF-MINCE', DATE'2021-05-12', 'Beef mince typically 82% meat, 18% fat with nothing else added.', 'Countdown', 'Countdown Beef Mince Value', 10.90);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'TP-6PK', DATE'2021-05-12', 'Purex 6 mega long white rolls, 450 sheets per roll (more than double purex standard rolls).', 'Purex', 'Purex Toilet Paper 6Pk', 4.80);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'KERI-OJ', DATE'2021-05-12', 'Made in New Zealand from quality imported ingredients.', 'Keri', 'Keri Premium Fruit Juice Orange 2.4L', 3.70);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'ARNOTTS-CSCOTCH', DATE'2021-05-12', 'Traditional scotch finger biscuits half coated in milk chocolate.', 'Arnotts', 'Arnotts Chocolate Biscuits Scotch Fingers', 2.99);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'BACON', DATE'2021-05-12', 'Made in New Zealand from imported and local ingredients.', 'Beehive', 'Streaky Bacon 250g', 6.00);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'CARAMILK', DATE'2021-05-12', 'Made in Australia from imported and local ingredients.', 'Cadbury ', 'Chocolate Block Caramilk 180g', 3.49);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (1, 'SNIFTER', DATE'2021-05-12', 'Enjoy the smooth creamy taste of cadbury dairy milk milk chocolate now with the inclusion of mint flavoured marshmallows inspired by pascall snifters!. This special edition flavour won’t be around for long so make sure you pick up one or two today.', 'Cadbury', 'Cadbury Dairy Milk Chocolate Block Pascall Snifters 180g', 3.49);

/* Products for Business 2 (8) */
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'CHOCOLATE', DATE'2021-05-12', 'Made in Australia from imported and local ingredients.', 'Cadbury', 'Cadbury Chocolate Block Dairy Milk 180g', 3.49);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'BLACK-FOREST', DATE'2021-05-12', 'Made in Australia from imported and local ingredients.', 'Cadbury', 'Cadbury Chocolate Block Dairy Milk Black Forest 180g', 3.49);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'CHICKEN-NUGGETS', DATE'2021-05-12', 'Made in New Zealand from local and imported ingredients.', 'Tegel', 'Tegel Chicken Nuggets Tempura Battered 1kg', 10.00);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'KERI-APPLE', DATE'2021-05-12', 'Made in New Zealand from quality imported ingredients.', 'Keri', 'Keri Original Fruit Juice Apple 3L', 3.70);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'CHICKEN-TENDERS', DATE'2021-05-12', 'Proudly made right here in New Zealand from local and imported ingredients.', 'Tegel', 'Tegel Chicken Tenders Crispy', 10.00);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'CARAMELLO', DATE'2021-05-12', 'Made in Australia from imported and local ingredients.', 'Cadbury', 'Cadbury Chocolate Block Dairy Milk Caramello', 3.49);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'POTATO-CHIPS', DATE'2021-05-12', 'Made in New Zealand from local and imported ingredients.', 'Eta', 'Eta Ripple Cut Potato Chips Salt & Vinegar', 2.20);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (2, 'CHICKEN-STEAKS', DATE'2021-05-12', 'Proudly made right here in New Zealand from local and imported ingredien', 'Tegel', 'Tegel Chicken Steaks Chargrilled Breast', 10.00);

/* Products for Business 3 (8) */
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'POTATO-CHIPS', DATE'2021-05-12', 'Made in New Zealand from local and imported ingredients.', 'Eta', 'Eta Ripple Cut Potato Chips Chicken', 2.20);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'FR-CHICKEN-TEND', DATE'2021-05-12', 'Proudly made right here in New Zealand from local and imported ingredients.', 'Tegel', 'Tegel Free Range Chicken Tenders Panko Bread Crumbs', 10.00);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'KERI-OJ-W-APPLE', DATE'2021-05-12', 'Keri juices have been delighting New Zealanders for over 40 years. All our favourites are real crowd pleasers and packed full of deliciousness. This tasty orange with apple base is one of New Zealand''s favourite flavours.', 'Keri', 'Keri Original Fruit Juice Orange With Apple 3L', 3.70);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'COFFEE-CARAMEL', DATE'2021-05-12', 'Made in Australia from at least 50% Australian ingredients.', 'Nescafe', 'Nescafe Coffee Mix Caramel 442G', 9.00);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'ARNOTTS-CSNAP', DATE'2021-05-12', 'Crunchy biscuits made with rolled oats golden syrup and coconut with a coating of delicious milk chocolate.', 'Arnotts', 'Arnotts Chocolate Biscuits Butternut Snap', 2.99);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'PC-SALTED', DATE'2021-05-12', 'Made in New Zealand from local and imported ingredients.', 'Eta', 'Eta Ripple Cut Potato Chips Ready Salted', 2.20);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'ARNOTTS-MINT', DATE'2021-05-12', 'Smooth dark chocolate with crunchy chocolate biscuit and delicious mint cream hidden on the inside.', 'Arnotts', 'Arnotts Chocolate Biscuits Mint Slice', 3.65);
INSERT INTO `product` (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (3, 'COFFEE-MOCHA', DATE'2021-05-12', 'Made in Australia from at least 50% Australian ingredients.', 'Nescafe', 'Nescafe Coffee Mix Mocha 468G', 9.00);

/* Inventory for Business 1 (9) */
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 1, DATE'2021-11-12', DATE'2021-01-12', 4.50, 'MANDARIN', 99, DATE'2021-09-10', 445.50);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 1, DATE'2021-09-12', DATE'2021-01-12', 4.20, 'MANDARIN', 40, DATE'2021-06-10', 168.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-05-16', 1, DATE'2021-05-30', DATE'2021-05-10', 10.00, 'BEEF-MINCE', 68, DATE'2021-05-14', 680.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-11-12', 1, DATE'2022-11-12', DATE'2020-06-29', 3.70, 'KERI-OJ', 244, DATE'2021-11-10', 900.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 1, DATE'2021-11-12', DATE'2021-01-12', 2.99, 'ARNOTTS-CSCOTCH', 13, DATE'2021-09-10', 38.87);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 1, DATE'2021-06-12', DATE'2021-05-12', 5.69, 'BACON', 44, DATE'2021-06-10', 250.36);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 1, DATE'2021-11-12', DATE'2021-05-12', 3.49, 'CARAMILK', 100, DATE'2021-09-10', 349.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 1, DATE'2021-08-12', DATE'2021-05-12', 2.99, 'SNIFTER', 40, DATE'2021-07-10', 120.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 1, DATE'2021-08-12', DATE'2021-05-12', 3.99, 'SNIFTER', 60, DATE'2021-07-10', 240.00);

/* Inventory for Business 2 (9) */
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 2, DATE'2021-11-12', DATE'2021-01-12', 3.99, 'CHOCOLATE', 99, DATE'2021-09-10', 395.01);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 2, DATE'2021-09-12', DATE'2021-01-12', 3.29, 'CHOCOLATE', 40, DATE'2021-06-10', 131.60);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2023-05-16', 2, DATE'2023-05-30', DATE'2021-05-10', 4.00, 'BLACK-FOREST', 68, DATE'2023-05-14', 250.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-11-12', 2, DATE'2022-11-12', DATE'2020-06-29', 10.00, 'CHICKEN-NUGGETS', 244, DATE'2021-11-10', 2440.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 2, DATE'2021-11-12', DATE'2021-01-12', 2.99, 'KERI-APPLE', 13, DATE'2021-09-10', 38.87);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 2, DATE'2021-06-12', DATE'2021-05-12', 9.99, 'CHICKEN-TENDERS', 44, DATE'2021-06-10', 439.56);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 2, DATE'2021-11-12', DATE'2021-05-12', 3.49, 'CARAMELLO', 100, DATE'2021-09-10', 349.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 2, DATE'2021-08-12', DATE'2021-05-12', 2.20, 'POTATO-CHIPS', 40, DATE'2021-07-10', 88.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 2, DATE'2021-08-12', DATE'2021-05-12', 2.19, 'POTATO-CHIPS', 60, DATE'2021-07-10', 131.40);

/* Inventory for Business 3 (9) */
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 3, DATE'2021-11-12', DATE'2021-01-12', 2.20, 'POTATO-CHIPS', 99, DATE'2021-09-10', 215.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 3, DATE'2021-09-12', DATE'2021-01-12', 1.99, 'POTATO-CHIPS', 40, DATE'2021-06-10', 79.60);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2023-05-16', 3, DATE'2023-05-30', DATE'2021-05-10', 12.00, 'FR-CHICKEN-TEND', 68, DATE'2023-05-14', 816.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-11-12', 3, DATE'2022-11-12', DATE'2020-06-29', 3.70, 'KERI-OJ-W-APPLE', 244, DATE'2021-11-10', 902.80);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 3, DATE'2021-11-12', DATE'2021-01-12', 9.00, 'COFFEE-CARAMEL', 13, DATE'2021-09-10', 115.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-06-12', 3, DATE'2021-06-12', DATE'2021-05-12', 2.99, 'ARNOTTS-CSNAP', 44, DATE'2021-06-10', 131.56);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-09-12', 3, DATE'2021-11-12', DATE'2021-05-12', 2.49, 'PC-SALTED', 100, DATE'2021-09-10', 249.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 3, DATE'2021-08-12', DATE'2021-05-12', 9.00, 'COFFEE-MOCHA', 40, DATE'2021-07-10', 360.00);
INSERT INTO `inventory_item` (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (DATE'2021-07-12', 3, DATE'2021-08-12', DATE'2021-05-12', 9.00, 'COFFEE-MOCHA', 60, DATE'2021-07-10', 500.00);

/* Listings for Business 1 (8) */
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 180.00, 40, 1, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 250.00, 59, 1, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Willing to accept lower offers.', 168.00, 40, 2, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-05-14', DATE'2021-05-12', 'No low ballers.', 600.00, 60, 3, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Fresh', 800.00, 200, 4, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-10', DATE'2021-05-12', 'Limited Stock.', 38.87, 13, 5, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Selling quick.', 20.00, 4, 6, 1);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-07-10', DATE'2021-05-12', 'Limited Edition', 349.00, 100, 7, 1);

/* Listings for Business 2 (8) */
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 140.00, 40, 10, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 206.00, 59, 10, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Willing to accept lower offers.', 139.60, 40, 11, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-05-14', DATE'2021-05-12', 'No low ballers.', 32.00, 8, 12, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Fresh', 2000.00, 200, 13, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-10', DATE'2021-05-12', 'Limited Stock.', 38.87, 13, 14, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Selling quick.', 40.00, 4, 15, 2);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-07-10', DATE'2021-05-12', 'Limited Edition', 349.00, 100, 16, 2);

/* Listings for Business 3 (8) */
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 88.00, 40, 19, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Willing to accept lower offers.', 130.00, 59, 19, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Willing to accept lower offers.', 130.00, 40, 20, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-05-14', DATE'2021-05-12', 'No low ballers.', 69.69, 8, 21, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-12', DATE'2021-05-12', 'Fresh', 740.00, 200, 22, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-09-10', DATE'2021-05-12', 'Limited Stock.', 120.00, 13, 23, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-06-10', DATE'2021-05-12', 'Selling quick.', 11.96, 4, 24, 3);
INSERT INTO `listing` (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (DATE'2021-07-10', DATE'2021-05-12', 'Limited Edition', 249.00, 100, 25, 4);

/* Keywords (10) */
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (1, '2021-05-22 11:28:29', 'Coffee');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (2, '2021-05-22 11:28:29', 'Drink');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (3, '2021-05-22 11:28:29', 'Tea');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (4, '2021-05-22 11:28:29', 'Frozen');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (5, '2021-05-22 11:28:29', 'Vegetables');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (6, '2021-05-22 11:28:29', 'Meat');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (7, '2021-05-22 11:28:29', 'Chicken');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (8, '2021-05-22 11:28:29', 'Fish');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (9, '2021-05-22 11:28:29', 'Beef');
INSERT INTO `keyword` (`id`, `created`, `name`) VALUES (10, '2021-05-22 11:28:29', 'Fresh');

/* For Sale Cards for Users (10) */
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (1, 'FORSALE', DATE'2021-05-12', DATE'2022-05-19', 'Nescafe Cafe Menu Coffee Mix Caramel Latte 170G', 'Nescafé café menu caramel is made with quality coffee beans with a delicious caramel flavour, a great frothy afternoon treat!');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (1, 'FORSALE', DATE'2021-05-15', DATE'2022-05-19', 'Red Bull Energy Drink Sugar Free', 'red bull sugar free special formula contains ingredients of high quality: caffeine, taurine, b-group vitamins, sucralose & acesulfame k, alpine water.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (1, 'FORSALE', DATE'2021-05-10', DATE'2022-05-19', 'Chanui Organic Green Tea 170G', 'A lovely, organic chun mee-style green tea. Without green tea''s typical astringency, this gentle tea tastes too good to be good for you!.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (3, 'FORSALE', DATE'2021-05-09', DATE'2022-05-19', 'Mccain Mixed Vegetables Peas, Corn & Carrots', 'Our recipe is simple: good food starts from real ingredients. A mixture of peas, corn and carrot picked for perfection & snap frozen for freshness.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (5, 'FORSALE', DATE'2021-04-12', DATE'2022-05-19', 'Tegel Take Outs Chicken Tenders Louisiana Style', 'Proudly made right here in New Zealand from local and imported ingredients.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (5, 'FORSALE', DATE'2021-05-12', DATE'2022-05-19', 'Mccain Corn Super Juicy Kernels', 'Our recipe is simple: good food starts from real ingredients. Sweet and tender super juicy corn kernels picked for perfection & snap frozen for freshness.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (5, 'FORSALE', DATE'2021-05-15', DATE'2022-05-19', 'Sealord Fish Fillets Hoki Classic Crumbed', 'Wild caught new zealand hoki pieces, coated in a crunchy crumb. Pair this flaky, white fish with potatoes for a take on traditional fish and chips, or serve with vegetables or salad for an easy, healthy meal for the family.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (7, 'FORSALE', DATE'2021-05-12', DATE'2022-05-19', 'Peas Baby', 'Snap frozen for freshness. Baby peas are a source of fibre for healthy digestion, a source of vitamin c for a healthy immune system, and a source of iron to reduce fatigue and tiredness (as part of a healthy balanced diet).');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (8, 'FORSALE', DATE'2021-05-20', DATE'2022-05-19', 'Beef Frying', 'Approx 5 pieces per 1kg - but actual quantity may vary.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (9, 'FORSALE', DATE'2021-05-18', DATE'2022-05-19', 'Beef Mince Premium', 'Premium beef mince typically 95% meat, 5% fat with nothing else added.');

/* Exchange Cards for Users (10) */
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (1, 'EXCHANGE', DATE'2021-05-12', DATE'2021-05-19', '1L Trim Milk', 'Looking to exchange a 1L bottle of trim milk for whole milk. Bought by mistake.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (1, 'EXCHANGE', DATE'2021-05-15', DATE'2021-05-22', 'Punnet of Strawberries', 'Looking to exchange a punnet of around 10 strawberries for another fruit.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (2, 'EXCHANGE', DATE'2021-05-08', DATE'2021-05-15', 'Tinned Peaches for Tinned Apricots', 'Looking to exchange a tin of peaches for a tin of apricots. Bought by mistake.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (2, 'EXCHANGE', DATE'2021-05-01', DATE'2021-05-08', 'Whiskers Cat Food', 'Looking to exchange a 1KG bag of Whiskers cat biscuits for any other brand of cat biscuits.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (3, 'EXCHANGE', DATE'2021-05-13', DATE'2021-05-20', '1L Unsweetened Almond Milk', 'Looking to exchange a 1L bottle of unsweetened almond milk for original almond milk. Unopened.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (3, 'EXCHANGE', DATE'2021-05-12', DATE'2021-05-19', 'Hot Fuzz DVD', 'Looking to exchange Hot Fuzz DVD for another movie. Open to any offers.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (4, 'EXCHANGE', DATE'2021-05-11', DATE'2021-05-18', 'Freshly Caught Salmon', 'Looking to exchange some freshly caught salmon for some trout. I have 10 salmon available, make me an exchange offer if you have any freshly caught trout.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (4, 'EXCHANGE', DATE'2021-05-02', DATE'2021-05-09', '12 Banana Muffins', 'Looking to exchange banana muffins for other baked goods. I have 12 available, will exchange for 12 of the same baked good or for a variety.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (5, 'EXCHANGE', DATE'2021-05-12', DATE'2021-05-19', 'Pokemon Ruby for Pokemon Sapphire', 'Looking to exchange Pokemon Ruby for Pokemon Sapphire. Open to a permanent or temporary trade.');
INSERT INTO `marketplace_card` (creator_id, section, created, display_period_end, title, description) VALUES (6, 'EXCHANGE', DATE'2021-05-13', DATE'2021-05-20', '20 Feijoa', 'Looking to exchange feijoa for other fruit, preferably peaches. Homegrown and organic.');

/* Relation Between Card And Keyword (22) */
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('1', '1');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('1', '2');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('2', '2');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('3', '2');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('3', '3');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('4', '4');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('4', '5');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('5', '4');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('5', '6');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('5', '7');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('6', '4');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('6', '5');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('7', '4');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('7', '8');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('8', '4');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('8', '5');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('9', '6');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('9', '9');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('9', '10');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('10', '6');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('10', '9');
INSERT INTO `card_keywords` (`card_id`, `keyword_id`) VALUES ('10', '10');