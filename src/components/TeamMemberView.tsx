import React from 'react';
import {useState, useEffect} from "react";
import "./TeamMemberView.css"
import userView from "../types";
import DoubloonsAdder from './DoubloonsAdder';



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

    return (
        <div className="team-member-container">
            <img className="user-avatar" src="https://cdn-icons-png.flaticon.com/512/149/149071.png"/>
            <h1>{userData.firstName} {userData.lastName}</h1>
            <h2>{userData.email}</h2>
            <div className="points-container">
                <div className="shortcuts">
                    <div className="rotate red">
                        <p>Co</p>
                    </div>
                    <div className="rotate blue">
                        <p>Tr</p>
                    </div>
                    <div className="rotate yellow">
                        <p>Au</p>
                    </div>
                    <div className="rotate purple">
                        <p>Le</p>
                    </div>
                    <div className="rotate orange">
                        <p>Cr</p>
                    </div>
     
                </div>
                <div className="group-names">
                    <p>Colaboration</p>
                    <p>Trust</p>
                    <p>Autonomy</p>
                    <p>Learn Fast</p>
                    <p>Craftsmenship</p>
                </div>
                <div className="p-m-column">
                    <div className="plus-minus">
                        <DoubloonsAdder/>
                    </div>
                    <div className="plus-minus">
                        <DoubloonsAdder/>
                    </div>
                    <div className="plus-minus">
                        <DoubloonsAdder/>
                    </div>
                    <div className="plus-minus">
                        <DoubloonsAdder/>
                    </div>
                    <div className="plus-minus">
                        <DoubloonsAdder/>
                    </div>
                </div>
            </div>
            <button className='btn'><p>edit the comment</p></button>
        </div>
    )
}

export default TeamMemberView;