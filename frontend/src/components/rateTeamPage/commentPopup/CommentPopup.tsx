import React, {useState} from 'react';
import "./CommentPopup.css"
import MDEditor from "@uiw/react-md-editor";
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

        fetch(`/api/comments`, requestOptions)
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
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify(deleteCommentRequestBody),
        };

        fetch(`/api/comments`, requestOptions)
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
                    <div className="comment-container-white">
                    <div className="comment-container">
                        <div className="header">
                            <h3>Your comment for {commentState.givenTo}</h3>
                        </div>
                        <div className="text-field-position">
                                <MDEditor
                                    style={{width: '100%'}}
                                    className="mardown-editor"
                                    height={450}
                                    minHeight={200}
                                    maxHeight={550}
                                    value={commentState.comment}
                                    onChange={(val) => setNewCommentValue(val)}
                                    data-color-mode={"light"}
                                />
                        </div>
                        <div className="buttons-position">
                            <button className='btn btn-save' onClick={handleSubmit}><p>save</p></button>
                            <button className='btn btn-cancel' onClick={handleClose}><p>cancel</p></button>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default CommentPopup;