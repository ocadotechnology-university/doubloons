import React, {useEffect, useState} from 'react';
import {getLastDateString} from "../../utils/getCurrentDateString";
import SelectDate from "./selectDate/SelectDate";
import './YourResultsContent.css';
import SelectableDate from "../../types/SelectableDate";
import getMonthAndDateLabel from "../../utils/getDateLabel";
import DoubloonsSummary, {DoubloonsByCategoryType} from "./DoubloonsSummary";
import {CURRENT_USER} from "../../types/CURRENT_USER";
import Category from "../../types/Category";
import CommentsSummary from "./CommentsSummary";

export type DoubloonSummaryType = {
    givenBy: string,
    categoryId: number,
    amount: number,
}

export type CommentSummaryType = {
    givenBy: string,
    comment: string,
}

function YourResultsContent() {

    const [selectedDate, setSelectedDate] = useState<SelectableDate>({
        // initialize it as last month by default
        value: getLastDateString(),
        label: getMonthAndDateLabel(getLastDateString())
    });

    const [categories, setCategories] = useState<Category[]>([]);
    const [doubloonsState, setDoubloonsState] = useState<DoubloonSummaryType[]>([]);
    const [commentsState, setCommentsState] = useState<CommentSummaryType[]>([]);

    const [doubloonsByCategory, setDoubloonsByCategory] = useState<DoubloonsByCategoryType[]>([]);

    useEffect(() => {
        fetchCategories();
        fetchDoubloonsSummary();
        fetchCommentsSummary();
    }, []);

    useEffect(() => {
        setDoubloonsSummary(doubloonsState, categories);
    }, [categories, doubloonsState]);

    useEffect(() => {
        fetchDoubloonsSummary();
        fetchCommentsSummary();
    }, [selectedDate]);

    const fetchCategories = () => {
        fetch(`/api/categories`)
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data))
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

    const fetchCommentsSummary = () => {
        fetch(`/api/comments/summary/${CURRENT_USER.email}/${selectedDate.value}`)
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data))
                    setCommentsState(data);
                else
                    setCommentsState([]);
            })
            .catch(e => {
                console.log(e);
            });
    }

    const setDoubloonsSummary = (doubloons: DoubloonSummaryType[], categories: Category[]) => {


        const newDoubloonsSummary: DoubloonsByCategoryType[] = [];

        // initialize the array with each category and amount=0
        for (let i = 0; i < categories.length; i++)
            newDoubloonsSummary.push({
                categoryId: categories[i].categoryId,
                amount: 0,
            });

        // if no doubloons were fetched, return the default array
        if (doubloons.length < 1) {
            setDoubloonsByCategory(newDoubloonsSummary);
            return;
        }

        // calculate total amount for each category
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
                <div className="comments-summary-container">
                    <div className="comments-summary-title">
                        <h3>Comments</h3>
                    </div>
                    <CommentsSummary doubloonsSummary={doubloonsState} commentsSummary={commentsState} categories={categories}/>
                </div>
            </div>
        </div>
    );
}

export default YourResultsContent;