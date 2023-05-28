import React, {useEffect, useState} from 'react';
import getCurrentDateString from "../../utils/getCurrentDateString";
import Dropdown, {Option} from 'react-dropdown';
import SelectDate from "./selectDate/SelectDate";
import './YourResultsContent.css';
import SelectableDate from "./selectDate/SelectableDate";
import getMonthAndDateLabel from "../../utils/getDateLabel";
import selectableDate from "./selectDate/SelectableDate";

function YourResultsContent() {

    const [selectedDate, setSelectedDate] = useState<SelectableDate>({
        value: getCurrentDateString(),
        label: getMonthAndDateLabel(getCurrentDateString())
    });

    const onSelectedDateChange = (newDate: SelectableDate) => {
        setSelectedDate(newDate);
    }

    return (
        <div className="your-results-container">
            <div className="your-results-left">
                <div className="pick-date-container">
                    <SelectDate defaultDate={selectedDate} onSelectedDateChange={onSelectedDateChange}/>
                </div>
            </div>
            <div className="your-results-right">

            </div>
        </div>
    )
}

export default YourResultsContent;