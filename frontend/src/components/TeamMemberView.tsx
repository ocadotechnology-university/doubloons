import React from 'react';
import "./TeamMemberView.css"
import DoubloonsAdder from './DoubloonsAdder';
import TeamMember from "../types/TeamMember";
import {CURRENT_USER} from "../types/CURRENT_USER";
import getCurrentDateString from "../utils/getCurrentDateString";



function TeamMemberView({userView, categories, doubloons, comment}: TeamMember) {

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

    const handleClick = () => {
        comment();
    }

    return (
        <div className="team-member-container">
            <img className="user-avatar" src="https://cdn-icons-png.flaticon.com/512/149/149071.png"/>
            <h1>{userView.firstName} {userView.lastName}</h1>
            <h2>{userView.email}</h2>
            <div className="points-container">
                <ul className="doubloon-ul">
                {categories.map(category => (
                    <li className="doubloon-li">
                        <div className="category-display">
                            <div className="category-logo">
                                {category.categoryName.slice(0, 2)}
                            </div>
                            <div className="category-name">
                                {category.categoryName}
                            </div>
                        </div>
                        <div className="points-adder">
                            <DoubloonsAdder doubloon={getDoubloon(category.categoryId)}/>
                        </div>
                    </li>
                ))}
                </ul>
            </div>
            <button className='btn' onClick={handleClick}><p>edit the comment</p></button>
        </div>
    )
}

export default TeamMemberView;