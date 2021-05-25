Feature: U20 - Modify inventory entries

  Background:
    Given I am logged in as an administrator of a business.

  Scenario: AC1 -  I can edit any of the inventory entry attributes.
    Given I have an inventory item with Product Id "WATT-420-BEANS", quantity 5, price per item "10", total price "45", manufactured "2020-06-12", sell by "2022-07-12", best before "2022-07-12" and expires "2022-07-12".
    Given I have an product with Product Id "APPLE-RED", name "apple", description "good", manufacturer "apple maker", recommendPrice "10" and created "2020-10-10T00:00".
    Given New Product Id "APPLE-RED", new quantity 1, new price per item "20", new total price "20", new manufactured "2020-06-01", new sell by "2022-09-13", new best before "2022-09-13" and new expires "2022-09-13".
    When I modified the inventory.
    Then The inventory is successfully modified.

  Scenario: AC2 -  Validation rules still apply.
    Given I have an inventory item with Product Id "WATT-420-BEANS", quantity 5, price per item "10", total price "45", manufactured "2020-06-12", sell by "2022-07-12", best before "2022-07-12" and expires "2022-07-12".
    Given I have an product with Product Id "APPLE-RED", name "apple", description "good", manufacturer "apple maker", recommendPrice "10" and created "2020-10-10T00:00".
    Given New Product Id "APPLE-RED", new quantity 0, new price per item "20", new total price "20", new manufactured "2020-06-01", new sell by "2022-09-13", new best before "2022-09-13" and new expires "2022-09-13".
    When I modified the inventory.
    Then The inventory is fail to modified.

  Scenario: AC2 -  Mandatory attributes are still mandatory.
    Given I have an inventory item with Product Id "WATT-420-BEANS", quantity 5, price per item "10", total price "45", manufactured "2020-06-12", sell by "2022-07-12", best before "2022-07-12" and expires "2022-07-12".
    Given I have an product with Product Id "APPLE-RED", name "apple", description "good", manufacturer "apple maker", recommendPrice "10" and created "2020-10-10T00:00".
    When New Product Id "APPLE-RED", new quantity 1, new price per item "20", new total price "20", new manufactured "2020-06-01", new sell by "2022-09-13", new best before "2022-09-13" and no expires.
    When I modified the inventory.
    Then The inventory is fail to modified.