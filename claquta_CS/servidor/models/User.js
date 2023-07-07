const {Schema, model}  = require('mongoose')

const userSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    username: {
        type: String,
        required: true,
    },
    password: {
        type: String,
        required: true
    },
    birthdate: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true
    },
    totalReviews: {
        type: String,
        required: false
    },
})

module.exports = model( "User", userSchema)
