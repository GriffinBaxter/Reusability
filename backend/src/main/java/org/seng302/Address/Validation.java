package org.seng302.Address;

import org.seng302.business.Business;

import java.util.List;

public class Validation {

    /**
     * Checks if a string is empty.
     * @param string String to be checked
     * @return True if the string is empty
     */
    public static boolean isEmpty(String string) {
        return string.equals("");
    }

    /**
     * Checks if a string is alpha.
     * @param string String to be checked
     * @param optional Optional field
     * @return True if a given string consists of alpha characters and if it is not empty if it is not optional
     */
    public static boolean isAlpha(String string, Boolean optional) {
        if (optional) {
            return string.matches("^[a-zA-Z]*$");
        } else {
            return string.matches("^[a-zA-Z]*$") && !isEmpty(string);
        }
    }

    /**
     * Checks if a string is a phone number.
     * @param string String to be checked
     * @param optional Optional field
     * @return True if a given string consists of numeric and other phone characters and if it is not empty if it is
     * not optional
     */
    public static boolean isPhoneNumber(String string, Boolean optional) {
        if (optional) {
            return string.matches("^[0-9 +]*$");
        } else {
            return string.matches("^[0-9 +]*$") && !isEmpty(string);
        }
    }

    /**
     * Checks if a string is alphanumeric.
     * @param string String to be checked
     * @param optional Optional field
     * @return True if a given string consists of alphanumeric characters and if it is not empty if it is not optional
     */
    public static boolean isAlphanumeric(String string, Boolean optional) {
        if (optional) {
            return string.matches("^[a-zA-Z0-9]*$");
        } else {
            return string.matches("^[a-zA-Z0-9]*$") && !isEmpty(string);
        }
    }

    /**
     * Checks if a string is an email, by checking its common elements.
     * @param string String to be checked
     * @param optional Optional field
     * @return True if a given string follows the email address format and if it is not empty if it is not optional
     */
    public static boolean isEmail(String string, Boolean optional) {
        if (optional) {
            return string.matches("^\\S+@\\S+\\.\\S+$");
        } else {
            return string.matches("^\\S+@\\S+\\.\\S+$") && !isEmpty(string);
        }
    }

    /**
     * Check if a password is longer than 8 and without space
     * @param password password
     * @return True if it is longer than 8 and without space
     */
    public static boolean isPassword(String password){
        String stringWithoutSpace = password.replace(" ", "");
        return (password.length() >= 8 && password.equals(stringWithoutSpace));
    }

    /**
     * Checks if a string is alpha and space.
     * @param string String to be checked
     * @return True if a given string consists of alpha characters and if it is not empty if it is not optional
     */
    public static boolean isName(String string) {
        boolean flag = true;
        String[] strings = string.split(" ");
        for (String s: strings){
            if (!s.matches("^[a-zA-Z]*$") || isEmpty(string)){
                flag = false;
            }
        }
        return flag;
    }

    /**
     * check if the length of name is between 1 to 100
     * @param name name
     * @return true when it is right name
     */
    public static boolean isBusinessName(String name) {
        return (name.length() <= 100 &&
                name.replace(" ", "").length() > 0);
    }

    /**
     * check if the length of description is less then 600
     * @param description description
     * @return true when it is right description
     */
    public static boolean isDescription(String description) {
        return (description.length() <= 600);
    }

    /**
     * check if the length of address is less then 255 and country is not null
     * @param address address
     * @return true when it is right address
     */
    public static boolean isAddress(Address address) {
        return (address.toString().length() <= 255 &&
                address.getCountry() != null &&
                !address.getCountry().replace(" ", "").equals(""));
    }

    /**
     * check if a business is in business repository
     * @param businesses a list of business
     * @param name business name
     * @param address business address
     * @return true when name and address not been used
     */
    public static boolean isNewBusiness(List<Business> businesses, String name, Address address) {
        boolean flag = true;
//        List<Business> businesses = businessRepository.findBusinessesByAddress(address.toString());
        if (!businesses.isEmpty()){
            for (Business business: businesses){
                if (business.getName().equals(name)){
                    flag = false;
                }
            }
        }
        return flag;
    }
}
