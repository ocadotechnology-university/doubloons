import React from "react";
import './ProgressBar.css';

interface ProgressBarProps {
    // current value
    now: number;
    max: number;
}

const ProgressBar: React.FC<ProgressBarProps> = ({now, max}) => {
    // this component is built with 2 divs, the inner one represents the `now` value

    // calculate the width of the inner div
    const innerWidthPercent = Math.ceil(100 * now / max);

    return (
        <div className="progress-bar">
            <div className="inner-progress-bar"
            style={{width: `${innerWidthPercent}%`}}>
            </div>
        </div>
    )
}

export default ProgressBar;