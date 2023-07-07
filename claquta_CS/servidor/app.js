require('dotenv').config()

const express = require('express')
const { ApolloServer } = require('apollo-server-express')
const { connectDB } = require('./db-connection')
const { resolvers } = require('./resolvers.js')
const { typeDefs } = require('./typeDefs')


const app = express()

connectDB()

app.get('/', (req, res) => res.send('Welcome to my custom films GRAPHQL API'))

module.exports = app


async function start() {

    const apolloServer = new ApolloServer({
        typeDefs,
        resolvers
    })

    await apolloServer.start()

    apolloServer.applyMiddleware({ app })

    app.use('*', (req, res) => res.status(404).send('NOT FOUND'))

    app.listen(process.env.PORT, () => {
        console.log('Servidor abierto en el puerto: ', process.env.PORT)
    })
}


start()