import { createSlice } from '@reduxjs/toolkit'
import React from 'react'

const initState = {
  accessToken: ""
}

const jwtSlice = createSlice({
  name: 'jwtSlice',
  initialState: initState,
  reducers: {
    setAccessToken: (state, action) => {
      console.log("accessToken 저장...");
      state.accessToken = action.payload;
    },
    deleteAccessToken: (state) => {{
      state.accessToken = null;
    }}
  }
})

export const { setAccessToken, deleteAccessToken } = jwtSlice.actions
export default jwtSlice.reducer