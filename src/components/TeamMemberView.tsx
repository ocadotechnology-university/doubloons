import React from 'react';
import {useState, useEffect} from "react";
import "./TeamMemberView.css"
import userView from "../types";

function TeamMemberView({email, teamId,firstName, lastName, avatar}: userView) {

    const initialState: userView = {
        email: "",
        teamId: "",
        firstName: "",
        lastName: "",
        avatar: "",
    }


    const [userData, setUserData] = useState(initialState);

    useEffect(() => {
        const newData: userView = {
            email: email,
            teamId: teamId,
            firstName: firstName,
            lastName: lastName,
            avatar: avatar,
        }

        setUserData(newData);

        // cleaning function
    }, []);


    const renderDate = () => {
        const date = new Date();
        const formattedDate = `${date.getMonth()}-${date.getFullYear()}`
        return <>{formattedDate}</>
    }


    return (
        <div className="team-member-container">
            <img className="user-avatar" src="https://cdn-icons-png.flaticon.com/512/149/149071.png"/>
            <h1>{userData.firstName} {userData.lastName}</h1>
            <h2>{userData.email}</h2>
            <div className="points-container">
                <p>Co - Collaboration</p>
                <p>Co - Trust</p>
                <p>Co - Autonomy</p>
                <p>Co - Learn Fast</p>
                <p>Cr - Craftsmanship</p>
            </div>
        </div>
    )
}

export default TeamMemberView;