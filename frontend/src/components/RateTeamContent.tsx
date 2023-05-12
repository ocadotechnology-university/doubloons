import React from 'react';
import "./RateTeamContent.css";
import "./TeamMemberView";
import {useEffect, useState} from "react";
import TeamMemberView from "./TeamMemberView";
import UserView from "../types/UserView";
import Doubloon from "../types/Doubloon";
import {CURRENT_USER} from "../types/CURRENT_USER";
import Category from "../types/Category";
import "./Popup";
import Popup from "./Popup";

function RateTeamContent() {

    const [doubloons, setDoubloons] = useState<Doubloon[]>([]);
    const [members, setMembers] = useState<UserView[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [showPopup, setShowPopup] = useState(false);


    useEffect(() => {
        getTeamMembers();
        getCategories();
        getDoubloons();
    }, []);


    const getTeamMembers = () => {
        fetch(`/api/users/${CURRENT_USER.teamId}`)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                const newMembers: UserView[] = data;
                setMembers(getMembersExceptCurrentUser(newMembers));
            })
            .catch((error) => {
                console.log(error);
            });
    };

    const getDoubloons = () => {
        fetch(`/api/doubloons/current/${CURRENT_USER.email}`)
            .then(response => {
                console.log(response);
                return response.json()
            })
            .then(data => {
                setDoubloons(data);
                console.log(JSON.stringify(data));
            })
            .catch(e => {
                console.log(e);
            });
    };

    const getCategories = () => {
      fetch('/api/categories/get')
          .then(response => response.json())
          .then(data => {
              setCategories(data);
          })
          .catch(e => {
              console.log(e);
          });
    };

    const getMembersExceptCurrentUser = (membersArr: UserView[]) => {
        console.log("DELETEINNGINS");
        return membersArr.filter((mem) => mem.email !== CURRENT_USER.email);
    }

    const getDoubloonsForMember = (email: string) => {

        const doubloonsForMember: Doubloon[] = [];

        for (let i = 0; i < doubloons.length; i++) {
            if (doubloons[i].givenTo === email)
                doubloonsForMember.push(doubloons[i]);
        }

        console.log(`DOUBLOONS FOR MEMBER:\n ${JSON.stringify(doubloonsForMember)}`);
        return doubloonsForMember;
    };

    const openPopup = () => {
        //
        setShowPopup(true);
    }

    return (
        <>
            <div className="content">
                <div className="heading">
                    <h2>WMS Business Processes</h2>
                    <div className="underline"></div>
                    <div className="team-members-container">
                        <>
                        {
                             doubloons.length > 0 && members.length > 0 && categories.length > 0 &&
                             members.map(member => (
                                <TeamMemberView key={member.email} categories={categories} userView={member} doubloons={getDoubloonsForMember(member.email)} comment={openPopup}/>
                            ))
                        }
                        </>
                    </div>
                </div>
            </div>
                {
                    showPopup && <Popup/>
                }
        </>
    )
}

export default RateTeamContent;
