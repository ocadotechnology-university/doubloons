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
import Timetable from "./Timetable";
import {UserDoubloonStats} from "../types/TimetableType";

function RateTeamContent() {

    const [doubloons, setDoubloons] = useState<Doubloon[]>([]);
    const [doubloonsGotFetched, setDoubloonsGotFetched] = useState(false);
    const [members, setMembers] = useState<UserView[]>([]);
    const [categories, setCategories] = useState<Category[]>([]);
    const [showPopup, setShowPopup] = useState(false);
    const [userDoubloonStats, setUserDoubloonStats] = useState<UserDoubloonStats>({spent: 0, left: 0});
    const [maxAmount, setMaxAmount] = useState(0);


    useEffect(() => {
        getTeamMembers();
        getCategories();
        getDoubloons();
        getMaxDoubloonsToSpendPerUser();
    }, []);

    useEffect(() => {
        let totalSpent = 0;
        for (let i = 0; i < doubloons.length; i++)
            totalSpent += doubloons[i].amount;



        const newStats = {
            spent: totalSpent,
            left: maxAmount - totalSpent,
        };

        setUserDoubloonStats(newStats)
    }, [doubloons, maxAmount]);


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
                return response.json()
            })
            .then(data => {
                if (Array.isArray(data))
                    setDoubloons(data);
                setDoubloonsGotFetched(true);
            })
            .catch(e => {
                console.log(e);
            });
    };

    const onDoubloonChange = (newDoubloon: Doubloon) => {
        let newDoubloons = JSON.parse(JSON.stringify(doubloons)) as Doubloon[];

        const foundDoubloon = newDoubloons.find(dbln => dbln.doubloonId === newDoubloon.doubloonId);

        if (foundDoubloon && newDoubloon.amount === 0) {
            newDoubloons = newDoubloons.filter(dbln => dbln.doubloonId !== newDoubloon.doubloonId);
        }
        else if (foundDoubloon) {
            foundDoubloon.amount = newDoubloon.amount;
        }
        else {
            newDoubloons.push(newDoubloon);
        }

        setDoubloons(newDoubloons);
    }


    const getMaxDoubloonsToSpendPerUser = () => {
        fetch(`/api/doubloon/getAmountToSpend/${CURRENT_USER.teamId}`)
            .then(data => data.json())
            .then(amount => {
                setMaxAmount(amount);
            })
            .catch(e => console.log(e));
    }

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
        return membersArr.filter((mem) => mem.email !== CURRENT_USER.email);
    }

    const getDoubloonsForMember = (email: string) => {

        const doubloonsForMember: Doubloon[] = [];

        for (let i = 0; i < doubloons.length; i++) {
            if (doubloons[i].givenTo === email)
                doubloonsForMember.push(doubloons[i]);
        }

        return doubloonsForMember;
    };

    const openPopup = () => {
        //
        setShowPopup(true);
    }

    return (
        <>

            <Timetable userDoubloonStats={userDoubloonStats}/>

            <div className="content">
                <div className="heading">
                    <h2>WMS Business Processes</h2>
                    <div className="underline"></div>
                    <div className="team-members-container">
                        <>
                        {
                             doubloonsGotFetched && members.length > 0 && categories.length > 0 &&
                             members.map(member => (
                                <TeamMemberView key={member.email}
                                                categories={categories}
                                                userView={member}
                                                doubloons={getDoubloonsForMember(member.email)}
                                                comment={openPopup}
                                                amountLeft={userDoubloonStats.left}
                                                onDoubloonChange={onDoubloonChange}/>
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
