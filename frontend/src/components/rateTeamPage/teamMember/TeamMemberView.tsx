import React from 'react';
import "./TeamMemberView.css"
import DoubloonsAdder from '../doubloonAdder/DoubloonsAdder';
import TeamMemberType from "./TeamMemberType";
import {CURRENT_USER} from "../../../types/CURRENT_USER";
import getCurrentDateString from "../../../utils/getCurrentDateString";
import Hexagon from "../../Hexagon";



function TeamMemberView({userView, categories, doubloons, openCommentPopup, amountLeft, onDoubloonChange}: TeamMemberType) {

    /**
     * gets doubloon and it's value for a specific category
     * @param categoryId
     */
    const getDoubloon = (categoryId: number) => {

      for (let i = 0; i < doubloons.length; i++)
          if (doubloons[i].categoryId === categoryId)
              return doubloons[i];

      return {
          doubloonId: undefined,
          categoryId: categoryId,
          givenTo: userView.email,
          givenBy: CURRENT_USER.email,
          amount: 0,
          monthAndYear: getCurrentDateString(),
      };
    };

    const handleCommentClick = () => {
        openCommentPopup(userView.email);
    }

    return (
        <div className="team-member-container">
            <img className="user-avatar" src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt={"user-avatar"}/>
            <h1>{userView.firstName} {userView.lastName}</h1>
            <h2>{userView.email}</h2>
            <div className="points-container">
                <ul className="doubloon-ul">
                {categories.map(category => (
                    <li className="doubloon-li">
                        <div className="category-display">
                            <div className="category-logo">
                                <Hexagon category={category.categoryName} size={32}/>
                            </div>
                            <div className="category-name">
                                {category.categoryName}
                            </div>
                        </div>
                        <div className="points-adder">
                            <DoubloonsAdder doubloon={getDoubloon(category.categoryId)}
                                            amountLeft={amountLeft}
                                            onDoubloonChange={onDoubloonChange}/>
                        </div>
                    </li>
                ))}
                </ul>
            </div>
            <button className='btn btn-open-comment-popup' onClick={handleCommentClick}>Leave a comment</button>
        </div>
    )
}

export default TeamMemberView;