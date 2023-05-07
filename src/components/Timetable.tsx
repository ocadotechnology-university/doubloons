import React from 'react';
import "./Timetable.css"

const renderDate = () => {
    const date = new Date();
    const monthNames = ["January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"
    ];
    const formattedDate = `${monthNames[date.getMonth()]} ${date.getFullYear()}`
    return <>{formattedDate}</>
}

function Timetable() {
    return (
        <>
            <div className="timetable">
                <div className="title">
                    <h2>{renderDate()}</h2>
                  <div className="highlight"></div>
                </div>
            </div>
        </>
    )
}

export default Timetable;