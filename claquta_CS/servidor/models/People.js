const {Schema, model}  = require('mongoose')

const peopleSchema = new Schema({
    name: {
        type: String,
        required: true,
    },
    surname: {
        type: String,
        required: true
    },
    age: {
        type: String,
        required: true
    },
    imageLink: {
        type: String,
        required: true
    },
    shortBio: {
        type: String,
        required: true
    }
})

module.exports = model( "People", peopleSchema)
