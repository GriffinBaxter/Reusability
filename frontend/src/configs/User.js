export const UserRole = {
    USER: "USER",
    GLOBALAPPLICATIONADMIN: "GLOBALAPPLICATIONADMIN",
    DEFAULTGLOBALAPPLICATIONADMIN: "DEFAULTGLOBALAPPLICATIONADMIN"
}

export default class User{

    // This is a config for the user requirement details
    static config = {
        firstName: {
            name: "First name",
            minLength: 2,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]+$/
        },
        middleName: {
            name: "Middle name",
            minLength: 0,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]*$/
        },
        lastName: {
            name: "Last name",
            minLength: 2,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]+$/
        },
        nickname: {
            name: "Nickname",
            minLength: 0,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]*$/
        },
        bio: {
            name: "Bio",
            minLength: 0,
            maxLength: 600
        },
        email: {
            name: "Email",
            minLength: 5,
            maxLength: 30,
            regex: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
            regexMessage: "Invalid email. Expected format is example123@gmail.com."

        },
        dateOfBirth: {
            name: "Date of birth",
            minAgeMs: 13 * 365 * 24 * 60 * 60 * 1000
        },

        phoneNumber: {
            name: "Phone number",
            minLength: 0,
            maxLength: 15,
            regex: /^[+0-9 ]*$/,
            regexMessage: "Invalid phone number. Must only contain numbers, +, and spaces."
        },
        homeAddress: {
            name: "Home address",
            minLength: 0,
            maxLength: 255,
            regex: /^[a-zA-Z0-9 '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",

        },

        streetNumber: {
            name: "Street number",
            minLength: 0,
            maxLength: 255
        },

        streetName: {
            name: "Street name",
            minLength: 0,
            maxLength: 255
        },

        city: {
            name: "City",
            minLength: 0,
            maxLength: 255
        },

        postcode: {
            name: "Postcode",
            minLength: 0,
            maxLength: 255
        },

        region: {
            name: "Region",
            minLength: 0,
            maxLength: 255
        },

        country: {
            name: "Country",
            minLength: 1,
            maxLength: 255
        },

        password: {
            name: "Password",
            minLength: 8,
            maxLength: 30,
            regexStrong: new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})"),
            regexStrongMessage: "Invalid password format",
            regexContainLowerCase: /^[\s\S]*[a-z][\s\S]*$/,
            regexContainUpperCase: /^[\s\S]*[A-Z][\s\S]*$/,
            regexContainNumber: /^[\s\S]*[0-9][\s\S]*$/,
            regexContainLength: /^[\s\S]{8,}$/,
            regexContainSymbol: /^[\s\S]*[!@#$%^&*][\s\S]*$/

            // Regex resource: https://www.thepolyglotdeveloper.com/2015/05/use-regex-to-test-password-strength-in-javascript/
        }
    };

    constructor({firstName, lastName, middleName, nickname, bio, email, dateOfBirth, phoneNumber, homeAddress, password}) {
        this.data = {
            firstName,
            lastName,
            middleName,
            nickname,
            bio,
            email,
            dateOfBirth,
            phoneNumber,
            homeAddress,
            password
        }

    }

}