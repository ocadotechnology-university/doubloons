import React from 'react';
import "./Content.css"
import "./TeamMemberView"
import {useEffect, useState} from "react";
import TeamMemberView from "./TeamMemberView";
import userView from "../types";

function Content() {

    const teamId = '1';

    const initialState: userView[] = [];

    const [members, setMembers] = useState(initialState);

    useEffect(() => {
        fetch(`http://localhost:8080/api/users/${teamId}`, {
            mode: "cors",
        })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log(data);
                setMembers(data);
            })
            .catch((error) => {
                console.log(error);
            });

    }, []);

    return (
        <>
            <div className="content">
                <div className="heading">
                    <h2>WMS Business Processes</h2>
                    <div className="underline"></div>
                    <div className="team-members-container">
                        <>
                        {
                            members.map(member => (
                                <TeamMemberView email={member.email} teamId={member.teamId} firstName={member.firstName} lastName={member.lastName} avatar={member.avatar}/>   
                            ))
                        }
                        </>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Content;