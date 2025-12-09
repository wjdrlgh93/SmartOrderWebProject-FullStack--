import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import axios from 'axios'
import './index.css'
import App from './App.jsx'
import store from './store/store.jsx'
import { Provider } from 'react-redux'
import { BrowserRouter } from 'react-router'

axios.defaults.withCredentials = true // 쿠키 허용

createRoot(document.getElementById('root')).render(
  // <StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  // </StrictMode> 
)
