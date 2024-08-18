import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import Modal from 'react-modal'
import { router } from './common/routes/router.jsx'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import App from './App.jsx'

Modal.setAppElement('#root');
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
)
