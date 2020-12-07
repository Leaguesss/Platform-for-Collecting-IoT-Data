const express = require('express');
var ejs = require('ejs');
// to get post request body
const bodyParser = require('body-parser');
const mongoose = require('mongoose');

const researcherAuthRouter = require('./api/researcherAuth');
const sensorAuthRouter = require('./api/sensorAuth');

const conf = require('./web_vue/config/conf')
const app = express();
const PORT = conf.PORT || 8080;
console.log(process.env.IP)

app.use(bodyParser.urlencoded({
    extended:true
}));
app.engine('html', ejs.__express);
app.set('view engine', 'html');

// allow cross domain access
app.all('*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header('Access-Control-Allow-Headers', 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With ,auth_token, yourHeaderFeild');
    res.header("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
    res.header("X-Powered-By",' 3.2.1')
    // res.header("Content-Type", "application/json;charset=utf-8");
    next();
});

app.use('/api/researcherAuth', researcherAuthRouter)
app.use('/api/sensorAuth', sensorAuthRouter)

app.use( express.static('web_vue/dist'))
app.get('/', (req, res)=>{
    res.type('html')
    res.sendFile(__dirname+"/web_vue/dist/index.html")
})

// TODO: connect to mongoDB
const uri = "mongodb+srv://qsftw:qsftw123@rest.46pyk.mongodb.net/IotData?retryWrites=true&w=majority"
mongoose.connect(process.env.MONGODB_URI || uri,
    { useNewUrlParser: true,  useUnifiedTopology: true },
    ()=> console.log('Connected to db'))

app.listen(PORT, '0.0.0.0');

console.log(conf.PORT);