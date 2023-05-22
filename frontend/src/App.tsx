import React from 'react';
import Navbar from './components/navbar/Navbar';
import RateTeamContent from './components/rateTeamPage/RateTeamContent';
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