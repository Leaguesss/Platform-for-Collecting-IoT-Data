const mongoose = require('mongoose')

const researcherSchema = mongoose.Schema({
    email: {
        type: String,
        required: true,
        max: 255
    },
    password: {
        type: String,
        required: true,
        max: 1024,
        min: 6
    },
    organisation:{
        type: String,
        required: true,
        max:255
    },
})

module.exports = mongoose.model('Researcher', researcherSchema )