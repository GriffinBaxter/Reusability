Feature: U16 - Product Images

  Scenario: AC1 - I can upload one image to a product of a business
    Given I am logged in as the administrator with first name "Hayley" and last name "Krippner" of the existing business with name "Crazy Dave's"
    And this business has the product with the product name of "Apples"
    When I upload an image for this product with the filename of "apples.jpg"
    Then this image is stored and displayed

  Scenario: AC2 - I can change which image is the primary image of a product
    Given I am logged in as the administrator with first name "Hayley" and last name "Krippner" of the existing business with name "Crazy Dave's"
    And this business has the product with the product name of "Apples"
    And the primary image of this product is "apples.js"
    And it has a non-primary image of "apples_chopped.js"
    When I change the primary image to "apples_chopped.js"
    Then the primary image is updated

  Scenario: AC4 - I can delete a product image
    Given I am logged in as the administrator with first name "Hayley" and last name "Krippner" of the existing business with name "Crazy Dave's"
    And this business has the product with the product name of "Apples"
    And this business only has the image of "apples.js"
    When this file is deleted
    Then this business has no images


