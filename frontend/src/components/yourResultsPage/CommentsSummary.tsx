import React from 'react';
import './CommentsSummary.css';
import {CommentSummaryType, DoubloonSummaryType} from "./YourResultsContent";
import Hexagon from "../Hexagon";
import Category from "../../types/Category";
import MDEditor from "@uiw/react-md-editor";

interface CommentsSummaryProps {
    doubloonsSummary: DoubloonSummaryType[];
    commentsSummary: CommentSummaryType[];
    categories: Category[];
}

const CommentsSummary: React.FC<CommentsSummaryProps> = ({doubloonsSummary, commentsSummary, categories}) => {

    return (
        <div className="comments-summary-content">
            { commentsSummary.length > 0 &&
                commentsSummary.map(comment => (
                    <div className="comment-summary-row">
                        <div className="comment-summary-row-doubloons">
                            {doubloonsSummary.map(doubloon => {
                                if (doubloon.givenBy === comment.givenBy)
                                    return (
                                        <div className="comment-summary-doubloon">
                                            <h4 className="comment-summary-doubloon-amount">
                                                {doubloon.amount}x
                                            </h4>
                                            <Hexagon category={categories.find(cat => cat.categoryId === doubloon.categoryId)?.categoryName} size={40}/>
                                            <h4 className="comment-summary-doubloon-name">
                                                {categories.find(cat => cat.categoryId === doubloon.categoryId)?.categoryName}
                                            </h4>
                                        </div>
                                    )
                            })}
                        </div>
                        <div className="comment-summary-row-comment">
                            <MDEditor.Markdown
                                className="markdown-preview"
                                source={comment.comment}/>
                        </div>
                    </div>
                ))
            }
            {
                commentsSummary.length === 0 &&
                <div className="comment-summary-empty">
                    You did not receive any comments this month.
                </div>
            }
        </div>
    );
}

export default CommentsSummary;