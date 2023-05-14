import React, {useEffect, useState} from 'react';
import "./Timetable.css"
import {TimetableType} from "../types/TimetableType";

function Timetable({userDoubloonStats}: TimetableType) {

    const [remainingTime, setRemainingTime] = useState<{days: number, hours: number, minutes: number}>({
        days: 0,
        hours: 0,
        minutes: 0
    });


    // calculate remaining time
    useEffect(() => {
        const intervalId = setInterval(() => {
            const now = new Date();
            const targetDate = new Date(`${now.getFullYear()}-${now.getMonth()+2}-01`);
            const differenceInS = (targetDate.getTime() - now.getTime()) / 1000;
            const days = Math.ceil(differenceInS / (3600 * 24));
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
                                remainingTime.days > 1 &&
                                <span> {remainingTime.days} Days</span>
                            }
                            {
                                remainingTime.days <= 1 &&
                                <span>
                                {remainingTime.hours > 0 && <> {remainingTime.hours} hour(s)</>}
                                    {remainingTime.minutes > 0 && <> {remainingTime.minutes} minute(s)</>}
                            </span>
                            }
                        </div>
                    </div>
                </div>
                <div className="timetable-column">
                    <p>
                        User left: {userDoubloonStats.left}
                    </p>
                    <p>
                        User spent: {userDoubloonStats.spent}
                    </p>
                </div>
                <div className="timetable-column">
                </div>

            </div>
        </>
    )
}

export default Timetable;