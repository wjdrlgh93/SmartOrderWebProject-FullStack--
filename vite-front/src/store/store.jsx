import React from "react";
import { configureStore } from "@reduxjs/toolkit";
import loginSlice from "../slices/loginSlice";
import jwtSlice from "../slices/jwtSlice";
import CartSlice from "../slices/CartSlice";
import adminSlice from "../slices/adminSlice";

const store = configureStore({
  reducer: {
    loginSlice: loginSlice,
    jwtSlice: jwtSlice,
    cartSlice: CartSlice,
    adminSlice: adminSlice,
  },
});

export default store;
