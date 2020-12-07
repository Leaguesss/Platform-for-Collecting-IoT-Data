const router = require('express').Router();
const accelerometerModel = require('../models/Accelerometer');
const createCsvWriter = require('csv-writer').createObjectCsvWriter;



// const MONGO = require('mongodb').MongoClient;
// const url = 'http://localhost:8080';



const csvdata = createCsvWriter({
  path: 'out2.csv',
  header: [
    {id: 'UID', title: 'UID'},
    {id: 'sensorType', title: 'SensorType'},
    {id: 'data', title: 'Data'},
    {id: 'time', title: 'Time'},
    {id: 'freq', title: 'Frequency'}
  ]
});



router.post('/temp', async (req, res) =>{
    const sensor = new accelerometerModel({
        uid: "4",
        data: [0,1,2]
    })

    try{
        const savedSensor = await sensor.save();
        res.send(savedSensor)
    } catch (err) {
        res.status(400).send(err)
    }
    
});



router.post('/csv', async (req, res)=>{
//test aacelermeter

    // const db = await MONGO.connect(url);
    // const MyCollection = db.collection('accelerometers');
    // const result = await MyCollection.find(query).toArray();
    // console.log(result);





    // let user = await accelerometerModel.findOne({uid: "2"}).then((res)=>{
    //     console.log(res.uid);
    //     accelerometer = res.data;

        const read_data = req.body.dataset;
        // const data =[{
        //     uid: '2',
        //     userdata: accelerometer
        // }];



        csvdata
            .writeRecords(read_data)
            .then(()=> console.log('The CSV file was written successfully'));
    // })

    res.json({url:"not null"})
});

// router.get('/auth', verifyToken, async (req, res)=>{
//     const user = await userModel.findById(req.user._id);
//     res.status(200).json({
//             username: user.username,
//             _id: user._id,
//             todo: user.todo
//         })
// })

module.exports = router