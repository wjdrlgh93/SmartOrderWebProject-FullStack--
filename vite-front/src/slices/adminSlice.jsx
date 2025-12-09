import { createSlice } from "@reduxjs/toolkit";
import React from "react";

const initState = {
  requestStatus: false,
};

const adminSlice = createSlice({
  name: "adminSlice",
  initialState: initState,
  reducers: {
    hasRequestStatus: (state, action) => {
      state.requestStatus = action.payload;
    },
  },
});

export const { hasRequestStatus } = adminSlice.actions;
export default adminSlice.reducer;
