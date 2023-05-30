import React, {useEffect, useState} from 'react';
import getCurrentDateString from "../../utils/getCurrentDateString";
import SelectDate from "./selectDate/SelectDate";
import './YourResultsContent.css';
import SelectableDate from "./selectDate/SelectableDate";
import getMonthAndDateLabel from "../../utils/getDateLabel";
import DoubloonsSummary, {DoubloonsByCategoryType} from "./DoubloonsSummary";
import {CURRENT_USER} from "../../types/CURRENT_USER";
import Category from "../../types/Category";

type DoubloonSummaryType = {
    givenBy: string,
    categoryId: number,
    amount: number,
}

function YourResultsContent() {

    const [selectedDate, setSelectedDate] = useState<SelectableDate>({
        value: getCurrentDateString(),
        label: getMonthAndDateLabel(getCurrentDateString())
    });

    const [categories, setCategories] = useState<Category[]>([]);
    const [doubloonsState, setDoubloonsState] = useState<DoubloonSummaryType[]>([]);

    const [doubloonsByCategory, setDoubloonsByCategory] = useState<DoubloonsByCategoryType[]>([]);

    useEffect(() => {
        fetchCategories();
        fetchDoubloonsSummary();
    }, []);

    useEffect(() => {
        setDoubloonsSummary(doubloonsState, categories);
    }, [categories, doubloonsState]);

    useEffect(() => {
        fetchDoubloonsSummary();
    }, [selectedDate]);

    const fetchCategories = () => {
        fetch(`/api/categories`)
            .then(response => response.json())
            .then(data => {
                setCategories(data);
            })
            .catch(e => {
                console.log(e);
            });
    };

    const fetchDoubloonsSummary = () => {
        fetch(`/api/doubloons/summary/${CURRENT_USER.email}/${selectedDate.value}`)
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data))
                    setDoubloonsState(data);
                else
                    setDoubloonsState([]);
            })
            .catch(e => {
                console.log(e);
            });
    };

    const setDoubloonsSummary = (doubloons: DoubloonSummaryType[], categories: Category[]) => {


        const newDoubloonsSummary: DoubloonsByCategoryType[] = [];

        for (let i = 0; i < categories.length; i++)
            newDoubloonsSummary.push({
                categoryId: categories[i].categoryId,
                amount: 0,
            });

        if (doubloons.length < 1) {
            setDoubloonsByCategory(newDoubloonsSummary);
            return;
        }

        doubloons.forEach(doubloon => {
            const doubloonSummaryCategory = newDoubloonsSummary.find(el => el.categoryId === doubloon.categoryId)
            if (doubloonSummaryCategory !== undefined)
                doubloonSummaryCategory.amount += doubloon.amount;
        });

        setDoubloonsByCategory(newDoubloonsSummary);
    }

    const onSelectedDateChange = (newDate: SelectableDate) => {
        setSelectedDate(newDate);
    }

    return (
        <div className="your-results-container">
            <div className="your-results-left">
                <div className="pick-date-container">
                    <SelectDate defaultDate={selectedDate} onSelectedDateChange={onSelectedDateChange}/>
                </div>
                <div className="doubloons-summary-container">
                    {categories.length > 0 && doubloonsByCategory.length > 0 &&
                    <DoubloonsSummary doubloons={doubloonsByCategory} categories={categories}/>}
                </div>
            </div>
            <div className="your-results-right">

            </div>
        </div>
    );
}

export default YourResultsContent;