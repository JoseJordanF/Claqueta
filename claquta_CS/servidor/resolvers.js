const Film = require('./models/Film.js')
const Review = require('./models/Review.js')
const User = require('./models/User')
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const resolvers = {
    Query: {
        getAllFilms: async () => {
            return await Film.find()
        },
        getAllReviews: async () => {
            return await Review.find()
        },
        getAllUsers: async () => {
            return await User.find()
        },
        getFilm: async (_, args) => {
            return await Film.findById(args.id)
        },
        getReview: async(_, args) => {
            return await Review.findById(args.id)
        },
        getUser: async (_,args) => {
            const user = await User.findById(args.id)
            return user
        }
    },

    Mutation: {
        createFilm: async (_, args) => {
            const {title, description, releaseDate, score, movieDirector, duration, reviews, cover_1, cover_2} = args
            const newFilm = new Film({title, description, releaseDate, score, movieDirector, duration, reviews, cover_1, cover_2})
            await newFilm.save()
        },
        createReview: async (_, args) => {
            const {content, username, score, creationDate, comments} = args
            const newReview = new Review({content, username, score, creationDate, comments })
            await newReview.save()
        },
        createUser: async (_, args) => {
            const {name, username, birthdate, email, totalReviews} = args
            const password  = await bcrypt.hash(args.password,10)
            console.log(`This is encryp ${password}`);
            console.log(`This is encryp ${args.password}`);
            const newUser = new User({name, username, password, birthdate, email, totalReviews})
            await newUser.save()
        },
        login: async(_, args) => {
            //Buscar usuario en la bbdd
            const {email,password} = args
            const logEmail = await User.findOne({email});

            console.log(`This is BBDD ${logEmail.password}`);
            console.log(`This is BBDD ${logEmail.email}`);
            console.log(`This is ARG ${password}`);
            console.log(`This is ARG ${email}`);

            if(!logEmail){
                throw new Error('Usuario no encontrado');
            }

            //Verificar contraseñá
            const validPassword = await bcrypt.compare(password, logEmail.password);
            console.log(`VALID? ${validPassword}`);
            if(!validPassword){
                throw new Error('Usuario o contraseña incorrectos');
            }

            //Generar token de autenticacion
            const token = jwt.sign({userId: logEmail.id}, 'clave_secreta', {
                expiresIn: '1h',
            });
            console.log(`TOKEN ${token}`);
            return {token}
        }



    }
}

module.exports = { resolvers }