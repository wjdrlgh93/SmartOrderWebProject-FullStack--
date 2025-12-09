import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  items: []
};

const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addCart: (state, action) => {
      state.items.push(action.payload);
    },
    removeCart: (state, action) => {
      state.items = state.items.filter(item => item.id !== action.payload);
    }
  }
});

export const { addCart, removeCart } = cartSlice.actions;  
export default cartSlice.reducer;