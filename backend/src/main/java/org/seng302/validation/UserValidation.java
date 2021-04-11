package org.seng302.validation;

import java.time.LocalDate;
import java.time.Period;

public class UserValidation {

    // Values need for validation.
    private static final Integer FIRST_NAME_MIN_LENGTH = 2;
    private static final Integer FIRST_NAME_MAX_LENGTH = 20;

    private static final Integer MIDDLE_NAME_MIN_LENGTH = 0;
    private static final Integer MIDDLE_NAME_MAX_LENGTH = 20;

    private static final Integer LAST_NAME_MIN_LENGTH = 2;
    private static final Integer LAST_NAME_MAX_LENGTH = 20;

    private static final Integer NICKNAME_MIN_LENGTH = 0;
    private static final Integer NICKNAME_MAX_LENGTH = 20;

    private static final Integer BIO_MIN_LENGTH = 0;
    private static final Integer BIO_MAX_LENGTH = 600;

    private static final Integer EMAIL_MIN_LENGTH = 3;
    private static final Integer EMAIL_MAX_LENGTH = 30;

    private static final Integer MIN_AGE = 13;

    private static final Integer PHONE_NUMBER_MIN_LENGTH = 0;
    private static final Integer PHONE_NUMBER_MAX_LENGTH = 15;

    private static final Integer PASSWORD_MIN_LENGTH = 8;
    private static final Integer PASSWORD_MAX_LENGTH = 30;

    /**
     * Checks to see whether first name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param firstName The first name to be checked.
     * @return true when the first name is valid
     */
    public static boolean isValidFirstName(String firstName) {
        return (firstName.length() >= FIRST_NAME_MIN_LENGTH) &&
                (firstName.length() <= FIRST_NAME_MAX_LENGTH) &&
                (firstName.matches("^[a-zA-Z '-]+$"));
    }

    /**
     * Checks to see whether middle name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param middleName The middle name to be checked.
     * @return true when the middle name is valid
     */
    public static boolean isValidMiddleName(String middleName) {
        return (middleName.length() >= MIDDLE_NAME_MIN_LENGTH) &&
                (middleName.length() <= MIDDLE_NAME_MAX_LENGTH) &&
                (middleName.matches("^[a-zA-Z '-]*$"));
    }

    /**
     * Checks to see whether last name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param lastName The last name to be checked.
     * @return true when the last name is valid.
     */
    public static boolean isValidLastName(String lastName) {
        return (lastName.length() >= LAST_NAME_MIN_LENGTH) &&
                (lastName.length() <= LAST_NAME_MAX_LENGTH) &&
                (lastName.matches("^[a-zA-Z '-]+$"));
    }

    /**
     * Checks to see whether nickname is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param nickname The nickname to be checked.
     * @return true when the nickname is valid.
     */
    public static boolean isValidNickname(String nickname) {
        return (nickname.length() >= NICKNAME_MIN_LENGTH) &&
                (nickname.length() <= NICKNAME_MAX_LENGTH) &&
                (nickname.matches("^[a-zA-Z '-]*$"));
    }

    /**
     * Checks to see whether bio is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param bio The bio to be checked.
     * @return true when the bio is valid
     */
    public static boolean isValidBio(String bio) {
        return (bio.length() >= BIO_MIN_LENGTH) &&
                (bio.length() <= BIO_MAX_LENGTH);
    }

    /**
     * Checks to see whether email is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param email The email to be checked.
     * @return true when the email is valid.
     */
    public static boolean isValidEmail(String email) {
        return (email.length() >= EMAIL_MIN_LENGTH) &&
                (email.length() <= EMAIL_MAX_LENGTH) &&
                (email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"));
    }

    /**
     * Checks to see whether date of birth is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param dateOfBirth The date of birth to be checked.
     * @return true when the date of birth is valid.
     */
    public static boolean isValidDOB(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Integer minAge = Period.between(dateOfBirth, currentDate).getYears();
        return minAge >= MIN_AGE;
    }

    /**
     * Checks to see whether phone number is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param phoneNumber The phone number to be checked.
     * @return true when the phone number is valid.
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return (phoneNumber.length() >= PHONE_NUMBER_MIN_LENGTH) &&
                (phoneNumber.length() <= PHONE_NUMBER_MAX_LENGTH) &&
                (phoneNumber.matches("^[+0-9 ]*$"));
    }

    /**
     * Checks to see whether password is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param password The password to be checked.
     * @return true when the phone number is valid.
     */
    public static boolean isValidPassword(String password) {
        return (password.length() >= PASSWORD_MIN_LENGTH) &&
                (password.length() <= PASSWORD_MAX_LENGTH) &&
                (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})"));
    }

}
