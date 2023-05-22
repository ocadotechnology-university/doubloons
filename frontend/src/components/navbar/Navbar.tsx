import React from 'react';
import "./Navbar.css"

function Navbar() {
    return (
        <>
            <div className="nav">
                <div className="nav-left">
                    <p className="nav-doubloons">Doubloons</p>
                    <a href=".#"><p className="nav-rateyourteam"><b>RATE YOUR TEAM</b></p></a>
                    <a href=".#" ><p className="nav-yourresults"><b>YOUR RESULTS</b></p></a>
                </div>
                <div className="nav-right">
                    <a href=".#"><p className="nav-logout"><b>LOG OUT</b></p></a>
                    <i className="fa-solid fa-right-from-bracket fa-rotate-180 logout-icon"></i>
                </div>
            </div>
        </>
    )
}

export default Navbar;