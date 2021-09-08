Feature: U28 - Product Search

  Scenario: AC2: I can search the catalogue for a business I am currently acting as admin for.
    Given I am logged in as a user with id 1 who is an administrator of a business with id 1
    When I search the product catalogue of the business with id 1
    Then I receive a 200 response and a payload with a list of products

  Scenario: AC2: I cannot search the catalogue for a business I am not currently acting as admin for.
    Given I am logged in as a user with id 2 who is not an administrator of a business with id 1
    When I search the product catalogue of the business with id 1
    Then I receive a 403 response

  Scenario: AC4: I can specify (by radio buttons, checkboxes, ...) which fields are to be searched.  ... there is also an option to search all fields.
    Given There is a business with id 1 and it has products
    When I search the product catalogue of the business with id 1 using fields "manufacturer" and "description" and query "Watties"
    Then I receive a 200 response and a payload with a list of products with manufacturer or description matching "Watties"

  Scenario: AC4: ... By default only the product name field is searched ...
    Given There is a business with id 1 and it has products
    When I search the product catalogue of the business with id 1 using no fields and query "Beans"
    Then I receive a 200 response and a product with the name "Beans"