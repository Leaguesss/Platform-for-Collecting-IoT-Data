const mongoose = require('mongoose')

const accelerometerSchema = mongoose.Schema({
    UID: {
        type: String,
        required: true,
        min: 5,
        max: 255
    },
    sensorType: {
        type: String,
        required: true,
        max: 255
    },
    data: {
        type: String,
        required: true,
        max: 255
    },
    time: {
        type: Date
    },
    freq: {
        type: String,
        required: true,
        max: 255
    },
})

module.exports = mongoose.model('Accelerometer', accelerometerSchema)