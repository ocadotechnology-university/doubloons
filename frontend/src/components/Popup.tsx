import React from 'react';
import "./Popup.css"
import TextField from './TextField';

function Popup() {
    const handleClose = () => {

    }
    return (
        <>
            <div className="popup-wrapper">
                <div className="comment-wrapper">
                    <div className="comment-container">
                        <div className="header">
                            <h3>Your comment for ...</h3>
                        </div>
                        <div className="text-field-position">
                                <TextField />
                                <TextField />
                        </div>
                        <div className="buttons-position">
                            <button className='btn save'><p>save</p></button>
                            <button className='btn cancel' onClick={handleClose}><p>cancel</p></button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Popup;