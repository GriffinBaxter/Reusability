Feature: U11 - Modifying Businesses

  Scenario: AC1 - I can modify all attributes of a business as a primary administrator
    Given I am logged in as a primary admin of a business
    When I try to modify all the attributes of a business
    Then The business is modified

  Scenario: AC4 - I can upload one or more images.
    Given I am logged in as a primary admin of a business
    And I have a business image with filename of "apples.jpg".
    When I upload this image for my business.
    Then This business image will be stored.

  Scenario: AC5 - One of these images is deemed to be the primary image.
    Given I am logged in as a primary admin of a business
    And There is no image for my business.
    And I have a business image with filename of "apples.jpg".
    When I upload this image for my business.
    Then The primary business image is "apples.jpg".

  Scenario: AC5 - I can change the primary image.
    Given I am logged in as a primary admin of a business
    And I have a business image with filename of "apples.jpg".
    And The primary business image is "apples.jpg".
    Given I have a business image with filename of "Iphone13.jpg".
    When I set the second business image be the primary business image.
    Then The primary business image is "Iphone13.jpg".

  Scenario: AC6 - A thumbnail of the primary image is automatically created.
    Given I am logged in as a primary admin of a business
    And There is no image for my business.
    And I have a business image with filename of "apples.jpg".
    When I upload this image for my business.
    Then A thumbnail of this business image is automatically created.
