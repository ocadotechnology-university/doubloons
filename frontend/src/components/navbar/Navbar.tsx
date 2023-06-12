import React from 'react';
import "./Navbar.css"
import {NavLink} from 'react-router-dom'

const Navbar = () => {
    return (
        <>
            <div className="nav">
                <div className="nav-left">
                    <p className="nav-doubloons">Doubloons</p>
                    <NavLink to={'/rate-team'}
                             className={({ isActive}) =>
                                 isActive
                                     ? "nav-link nav-link-active"
                                     : "nav-link"}>
                        RATE YOUR TEAM
                    </NavLink>
                    <NavLink to={'/your-results'}
                             className={({ isActive}) =>
                                 isActive
                                     ? "nav-link nav-link-active"
                                     : "nav-link"}>
                        YOUR RESULTS
                    </NavLink>
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