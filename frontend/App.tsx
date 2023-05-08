import React from 'react';
import Navbar from './components/Navbar';
import RateTeamContent from './components/RateTeamContent';
import "./App.css";
import Timetable from './components/Timetable';

function App() {
  return (
      <div className="wrapper">
        <Navbar />
        <Timetable />
        <RateTeamContent />
      </div>
  );
}

export default App;