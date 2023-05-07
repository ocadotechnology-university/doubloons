import React, {useState} from "react";
import "./DoubloonsAdder.css";
import DoubloonAdderType from "../types/DoubloonAdderType";
import Doubloon from "../types/Doubloon";

function DoubloonsAdder({doubloon}: DoubloonAdderType) {

    const [doubloonState, setDoubloonState] = useState(doubloon);

    const updateDoubloon = (newDoubloon: Doubloon) => {
        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(newDoubloon),
        };

        fetch('/api/doubloon/update', requestOptions)
            .then(() => {
                alert(`Doubloon updated!`);
            })
            .catch(e => console.log(e));
    }

    const deleteDoubloon = (currentDoubloon: Doubloon) => {
        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(currentDoubloon),
        };

        fetch('/api/doubloon/delete', requestOptions)
            .then(() => {
                alert(`Doubloon deleted!`);
            })
            .catch(e => console.log(e));
    }

    const createDoubloon = (newDoubloon: Doubloon) => {
        // just for now to satisfy back-end with pos-int, the value should be ignored on server-side
        newDoubloon.doubloonId = 1;
        newDoubloon.amount = 1;

        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(newDoubloon),
        };

        fetch('/api/doubloon/create', requestOptions)
            .then(response => response.json())
            .then(data => {
                if (typeof data === "number")
                    newDoubloon.doubloonId = data;
                alert(`created, id: ${data}`);
            })
            .catch(e => console.log(e));
    };

  function handleIncrement() {
      const newDoubloonState = {...doubloonState};

      newDoubloonState.amount += 1;

      if (doubloonState.amount > 0) {
          updateDoubloon(newDoubloonState);
      }
      else {
          createDoubloon(newDoubloonState);
      }

      setDoubloonState(newDoubloonState);
  }

  function handleDecrement() {
    const newDoubloonState = {...doubloonState};

    if (doubloonState.amount > 1) {
        newDoubloonState.amount -= 1;
        updateDoubloon(newDoubloonState);
    } 
    else {
        deleteDoubloon(newDoubloonState);
        newDoubloonState.amount = 0;
    }

    setDoubloonState(newDoubloonState);
  }

  return (
    <div className="doubloons-adder">
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