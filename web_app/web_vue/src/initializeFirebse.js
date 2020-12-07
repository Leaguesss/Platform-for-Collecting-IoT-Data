const firebase = window['firebase']

const firebseConfig = {
    apiKey: "AIzaSyAc0lq0LSUmtmQGKWindP0VBqBD0dKkFa8",
    authDomain: "sensordatacollector-27ecd.firebaseapp.com",
    databaseURL: "https://sensordatacollector-27ecd.firebaseio.com",
    projectId: "sensordatacollector-27ecd",
    storageBucket: "sensordatacollector-27ecd.appspot.com",
    messagingSenderId: "204178601091",
    appId: "1:204178601091:web:75531efe1e0c40cd4857b6"
}
const app = firebase.default.initializeApp(firebseConfig);

export const db = app.database();
export const namesRef = db.ref('web_sensor_infor');
export const querySensor = db.ref('check_web_sensor');
export const users = db.ref('users')