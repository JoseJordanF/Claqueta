const { connect } = require('mongoose')

const connectDB = async () => {
    try{
        await connect(process.env.MONGODB_URI);
        console.log('Mongodb connected')
    } catch(error) {
        console.error(error)
    }

};

module.exports = {connectDB}