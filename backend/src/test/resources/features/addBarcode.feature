Feature: BS1 Product Barcode

  Background:
    Given I am logged in as a business administrator.

    Scenario: AC2 - Add barcode when creating a product
      When I create a product with Product Id "WATT-420-BEANS", name "Watties Baked Beans - 420g can", description "Baked Beans as they should be.", manufacturer "Heinz Wattie's Limited", recommendPrice "2.2" and barcode "9400547002634"
      Then I expect the product to have a barcode of "9400547002634"

    Scenario: AC2 - Add barcode when modifying a product
      Given I have an existing product with Product Id "WATT-420-BEANS", name "Watties Baked Beans - 420g can", description "Baked Beans as they should be.", manufacturer "Heinz Wattie's Limited", recommendPrice "2.2" and barcode ""
      When I modify the product to have a barcode of "9400547002634"
      Then I expect the product to successfully be modified and have a barcode of "9400547002634"