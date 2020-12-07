const router = require('express').Router();
const researcherModel = require('../models/Researcher');


// TODO enable later
const bcrypt = require('bcryptjs')
// const jwt =require('jsonwebtoken')

// const verifyToken  = require('./verifyToken')
// const {registerValidation, loginValidation} = require('../validation')


router.post('/register', async (req, res) =>{
    // Validate
    // const {error} = registerValidation(req.body)
    // if (error) return res.status(400).send(error.details[0].message)

    // Check whether email exists already
    const emailExists = await researcherModel.findOne({email: req.body.email})
    if (emailExists) return res.status(400).send('Email already exists')

    // Hash the password
    const salt = await bcrypt.genSalt(10)
    const hash = await bcrypt.hash(req.body.password, salt)

    const user = new researcherModel({
        email: req.body.email,
        // Todo change to hash later
        password: hash,
        organisation: req.body.organisation
    })

    try{
        const savedUser = await user.save();
        savedUser.password = ''
        res.send(savedUser)
    } catch (err) {
        res.status(400).send(err)
    }

    
});

router.post('/login', async (req, res)=>{
    // Validate
    // const {error} = loginValidation(req.body)
    // if (error) return res.status(400).send(error.details[0].message)

    // Check whether email exists already



    var pw;
    var email;
    var organisation;
    var response = res;
    let user = await researcherModel.findOne({email: req.body.username}).then(async (res)=>{
        if(res != null){
            const validPassword = await bcrypt.compare(req.body.password, res.password)
                                    .catch(error => console.log('caught', error.message))


            if(!validPassword) response.status(400).send('Wrong Password');
            else{
                email = res.email;
                organisation = res.organisation;
                response.json({
                    email_now:email,
                    organisation_now:organisation})
            }
        }else{
            response.json({
                    email_now:null,
                    organisation_now:null})

        }
    })
    
    


    // res.status(200).json({
    //     password: user
    // })
    // // console.log(res);
    // console.log(req);
     
    // if (!user) return res.status(400).send('Wrong username')

    // Check whether password is correct
    // const validPassword = await bcrypt.compare(req.body.password, res.password)
    // .catch(error => console.log('caught', error.message))

    // if(!validPassword) res.status(404).send('Wrong Password')

    // create and assign token
    // const token = jwt.sign({_id: user._id}, process.env.TOKEN_SECRET)
    // res.status(200).json({
    //     username: res.username,
    //     password: res.password,
    //     _id: res._id
    //     // auth_token: token,
    //     // todo: user.todo
    // })
})

// router.get('/auth', verifyToken, async (req, res)=>{
//     const user = await userModel.findById(req.user._id);
//     res.status(200).json({
//             username: user.username,
//             _id: user._id,
//             todo: user.todo
//         })
// })

module.exports = router