import React, {useEffect, useState} from 'react';
import "./Timetable.css";
import {TimetableType} from "./TimetableType";
import ProgressBar from "../progressBar/ProgressBar";
import {CURRENT_USER} from "../../../types/CURRENT_USER";
import getCurrentDateString from "../../../utils/getCurrentDateString";

function Timetable({userDoubloonStats, otherTeamMembers}: TimetableType) {

    const [remainingTime, setRemainingTime] = useState<{days: number, hours: number, minutes: number}>({
        days: 0,
        hours: 0,
        minutes: 0
    });
    const [spentByOthersList, setSpentByOthersList] = useState<{email: string, totalAmountSpent: number}[]>([]);
    const [totalSpentByOthers, setTotalSpentByOthers] = useState(-1);

    const maxDoubloonsForOthers = (userDoubloonStats.left + userDoubloonStats.spent) * otherTeamMembers;

    useEffect(() => {
        fetchSpentByOthersList();
    }, []);

    useEffect(() => {
        let totalSpentByOthers = 0;
        for (let i = 0; i < spentByOthersList.length; i++) {
            totalSpentByOthers += spentByOthersList[i].totalAmountSpent;
        }

        if (Number.isNaN(totalSpentByOthers))
            totalSpentByOthers = 0;

        setTotalSpentByOthers(totalSpentByOthers);
    }, [spentByOthersList]);

    const fetchSpentByOthersList = () => {

        const requestBody = {
            email: CURRENT_USER.email,
            teamId: CURRENT_USER.teamId,
            monthAndYear: getCurrentDateString(),
        }

        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'},
            body: JSON.stringify(requestBody),
        };

        fetch(`/api/doubloons/getSpentByOthers`, requestOptions)
            .then(result => {
                console.log(result);
                return result.json()
            })
            .then(data => {
                setSpentByOthersList(data);
            })
            .catch(e => console.log(e));
    }


    // calculate remaining time
    useEffect(() => {
        const intervalId = setInterval(() => {
            const now = new Date();
            const targetDate = new Date(`${now.getFullYear()}-${now.getMonth()+2}-01`);
            const differenceInS = (targetDate.getTime() - now.getTime()) / 1000;
            const days = Math.floor(differenceInS / (3600 * 24));
            let hours = Math.floor(differenceInS / 3600);
            let minutes = Math.ceil((differenceInS/3600 - hours) * 60);

            if (minutes === 60 && hours > 0) {
                hours += 1;
                minutes = 0;
            }
            
            setRemainingTime({ days, hours, minutes });
        }, 1000);

        return () => clearInterval(intervalId);
    }, []);

    const renderDate = () => {
        const date = new Date();
        const monthNames = ["January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        ];
        return `${monthNames[date.getMonth()]} ${date.getFullYear()}`;
    }

    return (
        <>
            <div className="timetable">
                <div className="timetable-column">
                    <div className="title">
                        <h2>{renderDate()}</h2>
                        <div className="time-counter">
                            Time left:
                            {
                                remainingTime.days >= 1 &&
                                <span> {remainingTime.days} Day(s)</span>
                            }
                            {
                                remainingTime.days < 1 &&
                                <span>
                                {remainingTime.hours > 0 && <> {remainingTime.hours} hour(s)</>}
                                    {remainingTime.minutes > 0 && <> {remainingTime.minutes} minute(s)</>}
                            </span>
                            }
                        </div>
                    </div>
                </div>
                <div className="timetable-column">
                    <div className="timetable-column-title">
                        Your Doubloons
                    </div>
                    <div className="timetable-doubloon-stats">
                        <p>
                            {userDoubloonStats.left} Left
                        </p>
                        <p>
                            {userDoubloonStats.spent} Spent
                        </p>
                    </div>
                    <ProgressBar now={userDoubloonStats.left} max={userDoubloonStats.spent + userDoubloonStats.left}/>
                </div>
                <div className="timetable-column">
                    <div className="timetable-column-title">
                        Other's Doubloons
                    </div>
                    <div className="timetable-doubloon-stats">
                        <p>
                            {maxDoubloonsForOthers - totalSpentByOthers} Left
                        </p>
                        <p>
                            {totalSpentByOthers} Spent
                        </p>
                    </div>
                    {totalSpentByOthers > -1 &&
                        <ProgressBar now={maxDoubloonsForOthers - totalSpentByOthers} max={maxDoubloonsForOthers}/>}
                </div>

            </div>
        </>
    )
}

export default Timetable;