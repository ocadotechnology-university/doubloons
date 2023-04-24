import React from 'react';
import "./Content.css"
import "./TeamMemberView"
import TeamMemberView from "./TeamMemberView";
import userView from "../types";

function Content() {

    const exampleUser: userView = {
        name: "Name Surname",
        email: "name.surname@example.com",
    }

    return (
        <>
            <div className="content">
                <div className="title">
                    <h2>WMS Business Processes</h2>
                    <div className="underline"></div>
                    <div className="team-members-container">
                        <TeamMemberView name={exampleUser.name} email={exampleUser.email}/>
                        <TeamMemberView name={exampleUser.name} email={exampleUser.email}/>
                        <TeamMemberView name={exampleUser.name} email={exampleUser.email}/>
                        <TeamMemberView name={exampleUser.name} email={exampleUser.email}/>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Content;