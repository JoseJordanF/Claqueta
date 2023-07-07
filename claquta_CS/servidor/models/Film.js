const {Schema, model}  = require('mongoose')

const filmSchema = new Schema({
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    releaseDate: {
        type: String,
        required: true
    },
    score: {
        type: String,
        required: true
    },
    movieDirector: {
        type: String,
        required: true

    },
    duration: {
        type: String,
        required: true
    },
    reviews: {
        type: [String],
        required: false
    },
    cover_1: {
        type: String,
        required: true
    },
    cover_2: {
        type: String,
        required: true
    }
})

module.exports = model( "Film", filmSchema)

