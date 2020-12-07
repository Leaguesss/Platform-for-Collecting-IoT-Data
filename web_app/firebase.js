// import firebase from "firebase/app";
// require('firebase/firestore')
// require('firebase/auth')

const firebase = require("firebase");

const app = firebase.default.initializeApp({
    apiKey: "AIzaSyAc0lq0LSUmtmQGKWindP0VBqBD0dKkFa8",
    authDomain: "sensordatacollector-27ecd.firebaseapp.com",
    databaseURL: "https://sensordatacollector-27ecd.firebaseio.com",
    projectId: "sensordatacollector-27ecd",
    storageBucket: "sensordatacollector-27ecd.appspot.com",
    messagingSenderId: "204178601091",
    appId: "1:204178601091:web:75531efe1e0c40cd4857b6"
});

// firebase.initializeApp(app);

export const db = app.database();
export const namesRef = db.ref('web_researcher');


