import React, {useEffect, useState} from 'react'
import getCurrentDateString from "../../../utils/getCurrentDateString";
import Dropdown, {Option} from "react-dropdown";
import getMonthAndDateLabel from "../../../utils/getDateLabel";
import SelectableDate from "../../../types/SelectableDate";
import './SelectDate.css';
import {CURRENT_USER} from "../../../types/CURRENT_USER";

interface SelectDateProps {
    defaultDate: SelectableDate;
    onSelectedDateChange: Function;
}

const SelectDate: React.FC<SelectDateProps> = ({defaultDate, onSelectedDateChange}) => {

    // list of available dates to be selected by the user
    const [selectableDates, setSelectableDates] = useState<SelectableDate[]>([]);


    useEffect(() => {
        fetchAvailableDates();
    }, []);

    const fetchAvailableDates = () => {
        fetch(`api/doubloons/availableMonths/${CURRENT_USER.email}`)
            .then(response => response.json())
            .then(data => {

                // validate if the data is an array of strings
                if (Array.isArray(data) && data.every((item) => typeof item === 'string')) {

                    // if current date is present, delete it from the list so that the user cannot select it
                    data = data.filter(date => date !== getCurrentDateString());

                    const availableDates = data.map((date: string) => {
                        return {
                            value: date,
                            label: getMonthAndDateLabel(date),
                        };
                    });

                    setSelectableDates(availableDates);
                }

            })
            .catch(e => {
                console.log(e);
            });
    }

    const onSelectDateChange = (newDate: Option) => {

        if (typeof newDate.label !== 'string')
            return;

        const newSelectedDate: SelectableDate = {
            value: newDate.value,
            label: newDate.label,
        }

        onSelectedDateChange(newSelectedDate);
    }

    // just some visuals for the dropdown menu
    const arrowClosed = (<span className="arrow-closed"> ↓ </span>);
    const arrowOpen = (<span className="arrow-open"> ↑ </span>);

    return (
        <>
            {selectableDates.length > 0 &&
                            <Dropdown
                            options={selectableDates}
                            value={defaultDate}
                            onChange={onSelectDateChange}

                            className={"date-dropdown"}
                            arrowClosed={arrowClosed}
                            arrowOpen={arrowOpen}
                            menuClassName={"dropdown-menu"}
                            />}
        </>
    )
}

export default SelectDate;