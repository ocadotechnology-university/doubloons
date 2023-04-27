import React, { useState } from "react";
import "./DoubloonsAdder.css";

function DoubloonsAdder() {
    let [count, setCount] = useState(0);

  function handleIncrement() {
    setCount(count + 1);
  }

  function handleDecrement() {
    if (count > 0) {
      setCount(count - 1);
    } 
    else {
      setCount(0);
    }
  }

  return (
    <div className="doubloons-adder">
        <button className="counter">
            <span className="icon minus" onClick={handleDecrement}>
                <p>-</p>
            </span>
            <span className="value">
                {count}
            </span>
            <span className="icon plus" onClick={handleIncrement}>
                <p>+</p>
            </span>
        </button>
    </div>
  );
}


export default DoubloonsAdder;