import React from 'react';
import Navbar from './components/Navbar';
import Content from './components/Content';
import "./App.css";
import Timetable from './components/Timetable';

function App() {
  return (
      <div className="wrapper">
        <Navbar />
        <Timetable />
        <Content />
      </div>
  );
}

export default App;