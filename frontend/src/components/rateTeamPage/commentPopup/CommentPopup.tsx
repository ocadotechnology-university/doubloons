import React, {useState} from 'react';
import "./CommentPopup.css"
import MDEditor, { selectWord } from "@uiw/react-md-editor";
import CommentDTO from "../../../types/CommentDTO";
import DeleteCommentDTO from "../../../types/DeleteCommentDTO";

function CommentPopup({closeCommentPopup, comment} : {closeCommentPopup: Function, comment: CommentDTO, }) {

    const [commentState, setCommentState] = useState({...comment});

    const upsertComment = () => {

        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(commentState),
        };

        fetch(`/api/comments/upsert`, requestOptions)
            .catch(e => {
                console.log(e);
            });
    }

    const deleteComment = () => {

        const deleteCommentRequestBody: DeleteCommentDTO = {
            monthAndYear: commentState.monthAndYear,
            givenTo: commentState.givenTo,
            givenBy: commentState.givenBy,
        }

        const requestOptions: RequestInit = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(deleteCommentRequestBody),
        };

        fetch(`/api/comments/delete`, requestOptions)
            .catch(e => {
                console.log(e);
            });
    }

    /**
     * Makes the right call to the API and closes the comment popup
     */
    const handleSubmit = () => {
        if (commentState.comment === '')
            deleteComment();
        else
            upsertComment();

        closeCommentPopup(commentState);
    }

    const handleClose = () => {
        closeCommentPopup();
    }

    const setNewCommentValue = (newVal: string | undefined) => {
        const currentComment = {...commentState};

        currentComment.comment = newVal === undefined ? '' : newVal;

        setCommentState(currentComment);
    }

    return (
        <>
            <div className="popup-wrapper">
                <div className="comment-wrapper">
                    <div className="comment-container">
                        <div className="header">
                            <h3>Your comment for {commentState.givenTo}</h3>
                        </div>
                        <div className="text-field-position">
                                <MDEditor height={600} value={commentState.comment} onChange={(val) => setNewCommentValue(val)}/>
                        </div>
                        <div className="buttons-position">
                            <button className='btn save' onClick={handleSubmit}><p>save</p></button>
                            <button className='btn cancel' onClick={handleClose}><p>cancel</p></button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default CommentPopup;