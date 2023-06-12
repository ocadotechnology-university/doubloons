import React, {useState} from "react";
import "./DoubloonsAdder.css";
import Doubloon from "../../types/Doubloon";

interface DoubloonAdderProps {
    doubloon: Doubloon;
    amountLeft: number;
    onDoubloonChange: Function;
}

const DoubloonsAdder: React.FC<DoubloonAdderProps> = ({doubloon, amountLeft, onDoubloonChange}) => {

    const [doubloonState, setDoubloonState] = useState(doubloon);
    // state for the shake animation
    const [shake, setShake] = useState(false);

    const updateDoubloon = (newDoubloon: Doubloon) => {
        const requestOptions: RequestInit = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(newDoubloon),
        };

        fetch('/api/doubloons', requestOptions)
            .then(() => {

            })
            .catch(e => console.log(e));
    }

    const deleteDoubloon = (currentDoubloon: Doubloon) => {
        if (currentDoubloon.doubloonId === undefined)
            return;
        const requestOptions: RequestInit = {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(currentDoubloon),
        };

        fetch('/api/doubloons', requestOptions)
            .then(() => {

            })
            .catch(e => console.log(e));
    }

    const createDoubloon = (newDoubloon: Doubloon) => {
        // just for now to satisfy back-end with pos-int, the value should be ignored on server-side
        newDoubloon.doubloonId = 1;

        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(newDoubloon),
        };

        fetch('/api/doubloons', requestOptions)
            .then(response => response.json())
            .then(data => {
                if (typeof data === "number")
                    newDoubloon.doubloonId = data;
            })
            .catch(e => console.log(e));
    };

  function handleIncrement() {
      // if the user exceeded amount of points to spend, shake the button
      if (amountLeft === 0) {
          setShake(true);
          setTimeout(() => setShake(false), 400);
          return;
      }


      const newDoubloonState = {...doubloonState};

      newDoubloonState.amount += 1;

      if (doubloonState.amount > 0) {
          updateDoubloon(newDoubloonState);
      }
      else {
          createDoubloon(newDoubloonState);
      }

      setDoubloonState(newDoubloonState);
      onDoubloonChange(newDoubloonState);
  }

  function handleDecrement() {
    const newDoubloonState = {...doubloonState};

    if (doubloonState.amount > 1) {
        newDoubloonState.amount -= 1;
        updateDoubloon(newDoubloonState);

        setDoubloonState(newDoubloonState);
        onDoubloonChange(newDoubloonState);
    } 
    else if (doubloonState.amount === 1) {
        deleteDoubloon(newDoubloonState);
        newDoubloonState.amount = 0;

        onDoubloonChange(newDoubloonState);
        newDoubloonState.doubloonId = undefined;
        setDoubloonState(newDoubloonState);
    }
    else {
        setShake(true);
        setTimeout(() => setShake(false), 400);
    }
  }

  return (
    <div className={`doubloons-adder${shake ? ' shake': ''}`}>
        <button className="counter">
            <span className="icon minus" onClick={handleDecrement}>
                <p>-</p>
            </span>
            <span className="value">
                {doubloonState.amount}
            </span>
            <span className="icon plus" onClick={handleIncrement}>
                <p>+</p>
            </span>
        </button>
    </div>
  );
}


export default DoubloonsAdder;