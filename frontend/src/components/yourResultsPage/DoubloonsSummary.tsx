import React from 'react';
import Category from "../../types/Category";
import DoubloonBar from "./DoubloonBar";
import Hexagon from "../Hexagon";
import './DoubloonsSummary.css';

interface DoubloonsSummaryProps {
    doubloons: DoubloonsByCategoryType[];
    categories: Category[];
}

export type DoubloonsByCategoryType = {
    categoryId: number,
    amount: number,
}

const DoubloonsSummary: React.FC<DoubloonsSummaryProps> = ({categories, doubloons}) => {

    let totalAmount = 0;

    doubloons?.forEach(dbln => {
        totalAmount += dbln.amount;
    });


    return (
        <div className="doubloons-summary-content">
            <h3>
                Doubloons received this month: {totalAmount}
            </h3>
            {
                doubloons.length > 0 &&
                doubloons?.map(doubloon => (

                    <div className="doubloon-summary-row">
                        <div className="doubloons-summary-row-logo">
                            <div className="doubloons-summary-hexagon-container">
                                <Hexagon category={categories.find(category => category.categoryId === doubloon.categoryId)?.categoryName} size={40}/>
                            </div>
                            <div className="doubloons-summary-category-title">
                                <h4>{categories.find(category => category.categoryId === doubloon.categoryId)?.categoryName}</h4>
                            </div>
                        </div>
                        <div className="doubloons-summary-row-bar">
                            <DoubloonBar amount={doubloon.amount}
                                         max={totalAmount}
                                         category={categories.find(category => category.categoryId === doubloon.categoryId)?.categoryName}/>
                        </div>
                    </div>
                ))
            }



        </div>
    );
}

export default DoubloonsSummary;