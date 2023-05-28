import React from 'react';
import Navbar from './components/navbar/Navbar';
import "./App.css";
import "./categories.css";
import {Outlet} from 'react-router-dom'

function App() {
  return (
      <div className="wrapper">
        <Navbar />
        <Outlet/>
      </div>
  );
}

export default App;