const { gql } = require('apollo-server-express')

const typeDefs = gql`

type Film {
    id: ID
    title: String
    description: String
    releaseDate: String
    score: String
    movieDirector: String
    mainCharacters: [String]
    reviews: [String]
}


type Review {
    id: ID
    content: String
    username: String
    score: String
    creationDate: String
}

type User {
    id: ID
    name: String
    username: String
    password: String
    birthdate: String
    email: String
    totalReviews: String
}

type AuthPayload {
  token: String!
}

input FilmInput {
    id: ID
    title: String
    description: String
    releaseDate: String
    score: String
    movieDirector: String
    mainCharacters: [String]
    reviews: [String]
}

type Query {
    getAllFilms: [Film]
    getAllPeople: [People]
    getAllReviews: [Review]
    getAllUsers: [User]
    getFilm (id: ID!): Film
    getPeople (id: ID!): People
    getReview (id: ID!): Review
    getUser (id: ID!): User
}

type Mutation {
    ##createFilm(film: FilmInput!): Film
    createFilm(title: String!, description: String!, releaseDate: String!, score: String!, movieDirector: String!, duration: String!, reviews: [String], 
    cover_1: String!, cover_2: String!): Film
    createReview(content: String!, username: String!, score: String!, creationDate: String!, comments: [String]): Review
    createUser(name: String!, username: String!, password: String!, birthdate: String!, email: String!, totalReviews: String): User
    login(email: String!, password: String!): AuthPayload
}
`

module.exports = { typeDefs }