const express = require("express");
const router = express.Router();
//const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const passport = require("passport");

// Load Input Validation
const validateRegisterInput = require("../../validation/register");
const validateLoginInput = require("../../validation/login");

// Load User model
const User = require("../../models/User");

// @route   GET api/users/test
// @desc    Tests users route
// @access  Public
router.get("/test", (req, res) => res.json({ msg: "Users Works" }));

// @route   POST api/users/register
// @desc    Register user
// @access  Public
router.post("/register", (req, res) => {
  const { errors, isValid } = validateRegisterInput(req.body);

  // check validation
  if (!isValid) {
    return res.status(400).json(errors);
  }

  User.findOne({ socialId: req.body.id }).then(user => {
    if (user) {
      errors.id = "Id already exists";
      return res.status(400).json(errors);
    } else {
      const newUser = new User({
        socialId: req.body.id,
        name: req.body.name,
        email: req.body.email
        //password: req.body.password
      });

      newUser
        .save()
        .then(user => res.json(user))
        .catch(err => res.status(404).json(err));

      // bcrypt.genSalt(10, (err, salt) => {
      //   bcrypt.hash(newUser.password, salt, (err, hash) => {
      //     if (err) throw err;
      //     newUser.password = hash;
      //     newUser
      //       .save()
      //       .then(user => res.json(user))
      //       .catch(err => res.status(404).json(err));
      //   });
      // });
    }
  });
});

// @route   POST api/users/login
// @desc    Login User / Returning JWT Token
// @access  Public
router.post("/login", (req, res) => {
  const { errors, isValid } = validateLoginInput(req.body);

  // check validation
  if (!isValid) {
    return res.status(400).json(errors);
  }

  const id = req.body.id;

  // Find user by id
  User.findOne({ socialId: id }).then(user => {
    // Check for user
    if (!user) {
      errors.id = "User not found";
      return res.status(404).json(errors);
    }

    // Check password
    // bcrypt.compare(password, user.password).then(isMatch => {
    //   if (!isMatch) {
    //     errors.password = "Password incorrect";
    //     return res.status(400).json(errors);
    //   }
    //   // User creditial matched

    // Create JWT payload
    const payload = {
      id: user.id,
      name: user.name
    };

    // Sign Token
    jwt.sign(payload, keys.secretOrKey, { expiresIn: 3600 }, (err, token) => {
      res.json({
        success: true,
        token: "Bearer " + token
      });
      //   });
    });
  });
});

// @route   POST api/users/current
// @desc    Return current user
// @access  Private
router.get(
  "/current",
  passport.authenticate("jwt", { session: false }),
  (req, res) => {
    res.json({
      id: req.user.id,
      name: req.user.name,
      email: req.user.email
    });
  }
);

module.exports = router;
