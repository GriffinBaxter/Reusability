const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const fs = require('fs');
let users;

const app = express();

// Tell the express app to expect JSON in the body of the request
app.use(bodyParser.json());
app.use(cors());

// Read the users file upon startup
fs.readFile('./users.json', (err, data) => {
    if (err) {
        console.error(err)
        return
    }
    users = JSON.parse(data).users;
})

function adduser(user) {
    /*
    Adds a user to the user.json file
     */
    users.push(user)
    const usersString = JSON.stringify({users});
    fs.writeFile('./users.json', usersString, (err) => {
        if (err) {
            console.error(err)
            return;
        }
        //file written successfully
    })
}

app.post('/users', (req, res) => {
    /*
    Adds the given user to the users file and sends the new ID back upon success
     */
    const newUser = req.body;
    const invalidData = false; // TODO: used for testing error codes manually by setting to true and changing the error code

    // Replacement for error checking with the data
    if (invalidData) {
        res.status(409).send("Error thrown by server")
    } else {
        let highest = -1;
        for (const user1 of users) {
            if (user1.id >= highest) {
                highest = user1.id + 1;
                console.log(highest);
            }
        }
        newUser.id = highest;
        newUser.created = (new Date()).toISOString();
        adduser(newUser)
        res.status(201).header('Set-Cookie', 'JSESSIONID=abcde12345; Path=/; HttpOnly').send(
            {userId: highest}
            )
    }

})

app.get('/', (req, res) => {
    res.send('HTTP request: GET /');
});

app.get('/users/search', (req, res) => {
    /*
    Searches through the list of users for users with names/nicknames matching the given string.
     */
    const finalList = [];
    for (const user of users) {
        if (req.query.searchQuery === '') {
            finalList.push(user);
        } else if (user.firstName.toLowerCase() === req.query.searchQuery.toLowerCase() ||
            user.middleName.toLowerCase() === req.query.searchQuery.toLowerCase() ||
            user.lastName.toLowerCase() === req.query.searchQuery.toLowerCase() ||
            user.nickname.toLowerCase() === req.query.searchQuery.toLowerCase()) {
            finalList.push(user);

        } else if (user.firstName.toLowerCase().includes(req.query.searchQuery.toLowerCase()) ||
            user.middleName.toLowerCase().includes(req.query.searchQuery.toLowerCase()) ||
            user.lastName.toLowerCase().includes(req.query.searchQuery.toLowerCase()) ||
            user.nickname.toLowerCase().includes(req.query.searchQuery.toLowerCase())) {
            finalList.push(user);
        }
    }

    res.status(200).header('Set-Cookie', 'JSESSIONID=abcde12345; Path=/; HttpOnly').json(finalList);
});

app.get('/users/:id', (req, res) => {
    /*
    Gets and returns the user with the given ID
     */
    const id = req.params.id;
    let resStatus = 406;

    let response = `No user with id ${id}`;
    for (const user of users) {
        console.log(user);
        if (parseInt(id) === user.id) {
            response = user;
            resStatus = 200;
            break;
        }
    }
    // Backend will need to check whether the JSESSION token is valid
    // For now we will accept any here
    // resStatus = 401 if token is invalid
    console.log(req.headers.cookie);
    res.status(resStatus)
        .send(response);
});

app.post('/login', (req, res) => {
    /*
    Checks if the given login credentials are valid and if so sends a JSESSION cookie and userID.
    Otherwise sends a 400 code.
     */
    console.log(req.body)
    let finalStatus = 400;
    let validLogin = false;
    let userCode;
    for (const user of users) {
        if (user.email === req.body.email && user.password === req.body.password) {
            validLogin = true;
            userCode = user.id;
        }
    }
    if (validLogin) {
        finalStatus = 200;
        res.status(finalStatus).header('Set-Cookie', 'JSESSIONID=abcde12345; Path=/;').send({userId: userCode});
    } else {
        res.status(finalStatus).send();
    }
});

app.listen(9499, () => {
    console.log('Team 6 Test Server listening on port 9499!')
});

