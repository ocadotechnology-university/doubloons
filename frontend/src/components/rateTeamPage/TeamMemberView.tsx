import React from 'react';
import "./TeamMemberView.css"
import DoubloonsAdder from './DoubloonsAdder';
import {CURRENT_USER} from "../../types/CURRENT_USER";
import getCurrentDateString from "../../utils/getCurrentDateString";
import Hexagon from "../Hexagon";
import UserView from "../../types/UserView";
import Category from "../../types/Category";
import Doubloon from "../../types/Doubloon";

interface TeamMemberProps {
    userView: UserView;
    categories: Category[];
    doubloons: Doubloon[];
    openCommentPopup: Function;
    amountLeft: number;
    onDoubloonChange: Function;
}

const TeamMemberView: React.FC<TeamMemberProps> = ({userView, categories, doubloons, openCommentPopup, amountLeft, onDoubloonChange}) => {

    /**
     * gets doubloon and it's value for a specific category
     * @param categoryId
     */
    const getDoubloon = (categoryId: number) => {
      // if the doubloon matching the category exists, return it
      for (let i = 0; i < doubloons.length; i++)
          if (doubloons[i].categoryId === categoryId)
              return doubloons[i];

      // else, return doubloon object with default values
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

    // variable needed to enable/disable opening comment popup
    // if the user did not give any doubloons to selected team member, the user should not be able to leave a comment
    let totalAmount = 0;
    doubloons.forEach(dbln => {
        totalAmount += dbln.amount;
    });

    return (
        <div className="team-member-container">
            <img className="user-avatar" src={userView.avatar} alt={"user-avatar"}/>
            <h1>{userView.firstName} {userView.lastName}</h1>
            <h2>{userView.email}</h2>
            <div className="points-container">
                <ul className="doubloon-ul">
                {
                    categories.map(category => (
                    <li className="doubloon-li">
                        <div className="category-display">
                            <div className="category-logo">
                                <Hexagon category={category.categoryName} size={'small'}/>
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
                ))
                }
                </ul>
            </div>
            <button className='btn btn-open-comment-popup'
                    onClick={handleCommentClick}
                    disabled={totalAmount <= 0}>
                Leave a comment
            </button>
        </div>
    )
}

export default TeamMemberView;