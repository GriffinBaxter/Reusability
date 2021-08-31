Feature: U10 Modifying Individuals

  Background:
    Given I am logged in as an user.

  Scenario: AC1 - As a registered individual, I can update all of my attributes.
    Given I have firstname "John", lastname "Doe", middle name "S", nickname "Generic", bio "Biography",email "email@email.com", date of birth "2000-02-02", phone number "0271316", password "Password123!".
    When I try to change my profile as firstname "John", lastname "Smith", middle name "H", nickname "Jeff", bio "Likes long walks on the beach",email "johnsmith99@email.com", date of birth "1998-03-11", phone number "201038", current password "Password123!", new password "New123456!".
    Then My profile been successfully modified as firstname "John", lastname "Smith", middle name "H", nickname "Jeff", bio "Likes long walks on the beach",email "johnsmith99@email.com", date of birth "1998-03-11", phone number "201038", password "New123456!".

  Scenario: AC1 - As a registered individual, I can update all of my address.
    Given My address is "3/24" "Ilam Road", "Ilam" "Christchurch", "New Zealand", my region is "Canterbury" and my post code is "90210".
    When I try to change my address as "45" "Matipo street", "Riccarton" "Christchurch", "New Zealand", and region be "Canterbury" and post code be "8042".
    Then My address been successfully modified as "45" "Matipo street", "Riccarton" "Christchurch", "New Zealand", and region is "Canterbury" and post code is "8042".

  Scenario: AC2 - All validation rules still apply. Like I can not change my email by a invalid email address like "com.com".
    Given My email address is "email@email.com".
    When I try to modify email address become "com.com".
    Then A BAD_REQUEST stats return and the message say "Invalid email address".

  Scenario: AC2 - All validation rules still apply. Like I can not change my email by other people's email (been use by others).
    Given My email address is "email@email.com", and email "Jeff99@email.com" been use by an user.
    When I try to modify email address become "Jeff99@email.com".
    Then A BAD_REQUEST stats return and the message say "The Email already been used.".

  Scenario: AC2 - All validation rules still apply. Like I can not change my bio longer then 600 characters.
    Given My bio is "Biography".
    When I try to modify bio by repeat "This is a invalid bio" 30 times.
    Then A BAD_REQUEST stats return and the message say "Invalid bio".

  Scenario: AC2 - All validation rules still apply. Like I can not change my age under "13" years old.
    Given My date of birth is "2000-02-02".
    When I try to modify date of birth become "2015-09-17".
    Then A BAD_REQUEST stats return and the message say "Invalid date of birth".

  Scenario: AC3 - Mandatory attributes still remain mandatory. Like I can not set my first name become null.
    Given My first name is "John".
    When I try to modify first name become null.
    Then A BAD_REQUEST stats return and the message say "Invalid first name".

  Scenario: AC3 - Mandatory attributes still remain mandatory. Like I can not set my email address become null.
    Given My email address is "email@email.com".
    When I try to modify email address become null.
    Then A BAD_REQUEST stats return and the message say "Invalid email address".

  Scenario: AC3 - Mandatory attributes still remain mandatory. Like I can not set my country become null.
    Given My country is "New Zealand".
    When I try to modify country become null.
    Then A BAD_REQUEST stats return and the message say "Invalid country".
