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
    i = 1;
    while i <= 1000:
        if i < 900:
            num_of_products = random.randint(1, 2)
        elif i < 995:
            num_of_products = random.randint(2, 3)
        elif i == 1000:
            num_of_products = 50
        else:
            num_of_products = random.randint(20, 750)
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
            total_price = recommended_price * quantity
            
            sql.append("INSERT INTO product (business_id, id, created, description, manufacturer, name, recommended_retail_price) VALUES (" + str(i) + ", '" + 
                       prod_id + "', DATE'2021-05-12', '" + description + "', '" + manufacturer.replace("'", "''") + "', '" + name.replace("'", "''") + "', " + str("%.2f" % round(recommended_price ,2)) + ");")
            
            sql.append("INSERT INTO inventory_item (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES (" + "DATE'2022-05-12', " + str(i) + ", DATE'2023-05-12', " + "DATE'2020-05-12', " + str("%.2f" % round(recommended_price ,2)) + ", '" +
                       prod_id + "', " + str(quantity) + ", DATE'2022-10-12', " + str("%.2f" % round(total_price ,2)) + ");")
            j += 1
        i += 1
    with open('products_inventoryitems.sql', "w") as output_file:
        output_file.write('\n'.join(sql))
print("Done")
