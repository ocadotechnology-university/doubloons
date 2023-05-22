import React from "react";
import './ProgressBar.css';

function ProgressBar({now, max}: {now: number, max: number}) {

    const innerWidthPercent = Math.ceil(100 * now / max);

    return (
        <div className="progress-bar">
            <div className="inner-progress-bar"
            style={{width: `${innerWidthPercent}%`}}>
            </div>
        </div>
    )
}

export default ProgressBar