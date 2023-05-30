import React from 'react';
import getClassNameForCategory from "../../utils/getClassNameForCategory";
import './DoubloonBar.css';

interface DoubloonBarProps {
    amount: number,
    max: number,
    // category name
    category: string | undefined,
}

const DoubloonBar: React.FC<DoubloonBarProps> = ({amount, max, category}) => {

    if (max === 0)
        max = 1;

    let innerWidthPercent = Math.ceil(100 * amount / max);
    if (innerWidthPercent === 0)
        innerWidthPercent = 1;

    let backgroundStyle = '';

    if (category !== undefined)
        backgroundStyle = getClassNameForCategory(category);


    return (
        <div className="doubloon-bar">
            <div className={`doubloon-inner-bar ${backgroundStyle}`}
                 style={{width: `${innerWidthPercent}%`}}>
            </div>
            <div className="doubloon-bar-amount-label">
                {amount}
            </div>
        </div>
    );
}

export default DoubloonBar;
