import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './styles/index.css';

/**
 * React 19 Client Entry Point
 *
 * Mounts the GlobalPay Enterprise HRMS React Application into DOM root element `#root`.
 */
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
