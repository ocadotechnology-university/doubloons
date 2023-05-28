import React, {useEffect, useState} from 'react'
import getCurrentDateString from "../../../utils/getCurrentDateString";
import Dropdown, {Option} from "react-dropdown";
import getMonthAndDateLabel from "../../../utils/getDateLabel";
import SelectableDate from "./SelectableDate";
import './SelectDate.css';

function SelectDate({defaultDate, onSelectedDateChange} : {defaultDate: SelectableDate, onSelectedDateChange: Function}) {


    const [selectableDates, setSelectableDates] = useState<SelectableDate[]>([]);


    useEffect(() => {
        fetchAvailableDates();
    }, []);

    const fetchAvailableDates = () => {
        fetch('api/doubloons/availableMonths')
            .then(response => response.json())
            .then(data => {

                // validate if the data is an array of strings
                if (Array.isArray(data) && data.every((item) => typeof item === 'string')) {

                    // if current date is not present, add it to the list, so that the user can select it anyway
                    if (!data.find(date => date === getCurrentDateString()))
                        data.push(getCurrentDateString());



                    const availableDates = data.map(date => {
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