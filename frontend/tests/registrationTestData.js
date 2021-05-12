const validRegistrationPayload = {
    "firstName": "John",
    "lastName": "Smith",
    "middleName": "Hector",
    "nickname": "Jonny",
    "bio": "Likes long walks on the beach",
    "email": "johnsmith99@gmail.com",
    "dateOfBirth": "1999-04-27",
    "phoneNumber": "+64 3 555 0129",
    "homeAddress": {
        "streetNumber": "3/24",
        "streetName": "Ilam Road",
        "city": "Christchurch",
        "region": "Canterbury",
        "country": "New Zealand",
        "postcode": "90210"
    },
    "password": "1337-H%nt3r2"
};

const invalidRegistrationPayload = {
    "firstName": "John",
    "lastName": "Smith",
    "middleName": "Hector",
    "nickname": "Jonny",
    "bio": "Likes long walks on the beach",
    "email": "johnsmith99@gmail.com",
    "dateOfBirth": "1999-04-27",
    "phoneNumber": "+64 3 555 0129",
    "homeAddress": {
        "streetNumber": "3/24",
        "streetName": "Ilam Road",
        "city": "Christchurch",
        "region": "Canterbury",
        "country": "New Zealand",
        "postcode": "90210"
    },
    "password": "badpassword",
};

module.exports = {
    validRegistrationPayload,
    invalidRegistrationPayload
}