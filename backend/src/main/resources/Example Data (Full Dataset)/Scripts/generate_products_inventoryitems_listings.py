import random

with open('product_data.csv') as input_file:
    names = []
    descriptions = []
    manufacturers = []
    for line in input_file:
        name, description, manufacturer = line.split(",")
        if description == "blank":
            description = ""
        if manufacturer == "blank":
            manufacturer = ""
        names.append(name)
        descriptions.append(description)
        manufacturers.append(manufacturer)
    sql = []
    lines = 0
    file_num = 1
    i = 1
    inventory_id = 1
    while i <= 1000:
        if i < 995:
            num_of_products = random.randint(1, 2)
        elif i < 1000:
            num_of_products = 250
        elif i == 1000:
            num_of_products = 1000
        j = 0
        created_ids = []
        while j < num_of_products:
            name = names[random.randint(0, len(names) - 1)]
            prod_id = name[:4].upper().replace(" ", "-")
            k = 1
            while prod_id in created_ids:
                prod_id = prod_id[:4] + str(k)
                k += 1
            created_ids.append(prod_id)
            description = descriptions[random.randint(0, len(descriptions) - 1)]
            manufacturer = manufacturers[random.randint(0, len(manufacturers) - 1)]
            recommended_price = random.uniform(10.00, 50.00)
            quantity = random.randint(1, 100)
            listing_quantity = random.randint(1, quantity)
            total_price = recommended_price * quantity
            listing_price = recommended_price * listing_quantity
            more_info = \
                ['Willing to accept lower offers.', 'No low ballers.', 'Fresh', 'Limited Stock.', 'Selling quick.',
                 'Limited Edition'][random.randint(0, 5)]

            sql.append("INSERT INTO product (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (" + str(i) + ", '" + prod_id + "', DATE'2021-05-12', '" + description + "', '" + manufacturer.replace("'", "''").replace("\n", "") + "', '" + name.replace("'", "''") + "', " + str("%.2f" % round(recommended_price, 2)) + ");")

            sql.append("INSERT INTO inventory_item (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (" + "DATE'2022-05-12', " + str(i) + ", DATE'2023-05-12', " + "DATE'2020-05-12', " + str("%.2f" % round(recommended_price, 2)) + ", '" + prod_id + "', " + str(quantity) + ", DATE'2022-10-12', " + str("%.2f" % round(total_price, 2)) + ");")

            sql.append("INSERT INTO listing (closes, created, more_info, price, quantity, inventory_item_id, business_id) VALUES (" + "DATE'2022-05-12', " + "DATE'2020-05-12', '" + more_info + "', " + str("%.2f" % round(listing_price, 2)) + ", " + str(listing_quantity) + ", " + str(inventory_id) + ", " + str(i) + ");")

            day = str(random.randint(1, 28))
            sale_date = "2021-{}-{}".format("0" + str(random.randint(1, 9)), "0" + day if len(day) == 1 else day)

            sql.append(
                "INSERT INTO sold_listing (bookmarks, listing_date, price, product_id, quantity, sale_date, business, customer) VALUES " +
                "({}, DATE'{}', {}, '{}', {}, DATE'{}', {}, {});".format(
                    random.randint(0, 100), "2020-05-12", round(listing_price, 2), prod_id, listing_quantity,
                    sale_date, i, random.randint(1, 1000)
                )
            )

            j += 1
            inventory_id += 1

            if lines % 2000 == 0 and lines != 0:
                with open('products_inventory_items_listings_' + str(file_num) + '.sql', "w") as output_file:
                    output_file.write('\n'.join(sql))
                file_num += 1
                sql = []

            lines += 1
        i += 1
    if len(sql) != 0:
        with open('products_inventory_items_listings_' + str(file_num) + '.sql', "w") as output_file:
            output_file.write('\n'.join(sql))
print("Done")
