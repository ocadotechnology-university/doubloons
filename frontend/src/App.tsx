import React from 'react';
import Navbar from './components/Navbar';
import RateTeamContent from './components/RateTeamContent';
import "./App.css";

function App() {
  return (
      <div className="wrapper">
        <Navbar />
        <RateTeamContent />
      </div>
  );
}

export default App;