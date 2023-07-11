const { Schema, model } = require('mongoose')

const reviewSchema = new Schema({
    content: {
        type: String,
        required: true
    },
    username: {
        type: String,
        required: true,
    },
    score: {
        type: String,
        required: true,
    },
    creationDate: {
        type: String,
        required: true
    },
    comments: {
        type: [String],
        required: false
    }

})

module.exports = model("Review", reviewSchema)
