import React from 'react';
import "./RateTeamContent.css";
import "./TeamMemberView";
import {useEffect, useState} from "react";
import TeamMemberView from "./TeamMemberView";
import UserView from "../../types/UserView";
import Doubloon from "../../types/Doubloon";
import {CURRENT_USER} from "../../types/CURRENT_USER";
import Category from "../../types/Category";
import "./CommentPopup";
import CommentPopup from "./CommentPopup";
import Timetable from "./Timetable";
import UserDoubloonStats from "../../types/UserDoubloonStats";
import CommentDTO from "../../types/CommentDTO";
import getCurrentDateString from "../../utils/getCurrentDateString";
import Team from "../../types/Team";

/**
 * main component for 'Rate Team' page
 */
const RateTeamContent = () => {
    const [teamInfo, setTeamInfo] = useState<Team>({teamId: CURRENT_USER.teamId, teamName: 'Team Name', teamDescription: ''});
    // list of currently spent doubloons
    const [doubloons, setDoubloons] = useState<Doubloon[]>([]);
    // ancillary variable to show the TeamMemberView only when the doubloons are already fetched
    const [doubloonsGotFetched, setDoubloonsGotFetched] = useState(false);
    // list of current comments
    const [comments, setComments] = useState<CommentDTO[]>([]);
    // list of members (eventually list of members except the current user)
    const [members, setMembers] = useState<UserView[]>([]);
    // list of categories
    const [categories, setCategories] = useState<Category[]>([]);
    // handles showing comment popup (undefined - popup should be closed, email - popup should be shown for a specific user)
    const [showCommentPopupForUser, setShowCommentPopupForUser] = useState<string | undefined>(undefined);
    // statistics of spent doubloons for the current user
    const [userDoubloonStats, setUserDoubloonStats] = useState<UserDoubloonStats>({spent: 0, left: 0});
    // limit of doubloons that single user can spend
    const [maxAmount, setMaxAmount] = useState(0);

    // fetch all the data that is needed on the current sub-page
    useEffect(() => {
        fetchTeamInfo();
        fetchTeamMembers();
        fetchCategories();
        fetchDoubloons();
        fetchComments();
        fetchMaxDoubloonsToSpendPerUser();
    }, []);

    // update the stats when doubloons or maxAmount change
    useEffect(() => {
        let totalSpent = 0;
        for (let i = 0; i < doubloons.length; i++)
            totalSpent += doubloons[i].amount;

        const newStats = {
            spent: totalSpent,
            left: maxAmount - totalSpent,
        };

        setUserDoubloonStats(newStats)
    }, [doubloons, maxAmount]);

    const fetchTeamInfo = () => {
        fetch(`/api/teams/${CURRENT_USER.teamId}`)
            .then(response => response.json())
            .then(data => {
                setTeamInfo(data);
            })
            .catch(e => console.log(e));
    }

    const fetchTeamMembers = () => {
        fetch(`/api/users/team/${CURRENT_USER.teamId}`)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                const newMembers: UserView[] = data;
                setMembers(getMembersExceptCurrentUser(newMembers));
            })
            .catch((error) => {
                console.log(error);
            });
    };

    const fetchDoubloons = () => {
        fetch(`/api/doubloons/current/${CURRENT_USER.email}`)
            .then(response => {
                return response.json()
            })
            .then(data => {
                if (Array.isArray(data))
                    setDoubloons(data);
                setDoubloonsGotFetched(true);
            })
            .catch(e => {
                console.log(e);
            });
    };

    const fetchComments = () => {
        fetch(`/api/comments/${CURRENT_USER.email}/${getCurrentDateString()}`)
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data))
                    setComments(data);
            })
            .catch(e => {
                console.log(e);
            });

        return true;
    }

    const onCommentChange = (newComment: CommentDTO) => {
        let currentComments = [...comments];

        // look for a specific comment in the state list
        const currentComment = currentComments.find(com =>
            com.monthAndYear === newComment.monthAndYear && com.givenTo === newComment.givenTo && com.givenBy === newComment.givenBy);

        // if it has not existed, add it to the list
        if (currentComment === undefined)
            currentComments.push(newComment);
        // if it has existed, and the new value is empty string, delete the comment
        else if (newComment.comment === '')
            currentComments = currentComments.filter(com => com !== currentComment);
        // otherwise update the comment
        else
            currentComment.comment = newComment.comment;

        setComments(currentComments);
    }

    const onDoubloonChange = (newDoubloon: Doubloon) => {
        // make sure to deep copy the object
        let newDoubloons = JSON.parse(JSON.stringify(doubloons)) as Doubloon[];

        // look for a specific doubloon in the state list
        const foundDoubloon = newDoubloons.find(dbln => dbln.doubloonId === newDoubloon.doubloonId);

        // delete the doubloon if it has existed and the new value is 0
        if (foundDoubloon && newDoubloon.amount === 0) {
            newDoubloons = newDoubloons.filter(dbln => dbln.doubloonId !== newDoubloon.doubloonId);
        }
        // update the doubloon if it has existed and the new value is other than 0
        else if (foundDoubloon) {
            foundDoubloon.amount = newDoubloon.amount;
        }
        // add the doubloon if it has not existed
        else {
            newDoubloons.push(newDoubloon);
        }

        setDoubloons(newDoubloons);
    }


    const fetchMaxDoubloonsToSpendPerUser = () => {
        fetch(`/api/doubloons/maxAmountToSpend/${CURRENT_USER.teamId}`)
            .then(data => data.json())
            .then(amount => {
                setMaxAmount(amount);
            })
            .catch(e => console.log(e));
    }

    const fetchCategories = () => {
      fetch('/api/categories')
          .then(response => response.json())
          .then(data => {
              if (Array.isArray(data))
                setCategories(data);
          })
          .catch(e => {
              console.log(e);
          });
    };

    /**
     * Returns list of users except the currently logged-in user
     * @param membersArr - list of all users that you want to filter
     */
    const getMembersExceptCurrentUser = (membersArr: UserView[]) => {
        return membersArr.filter((mem) => mem.email !== CURRENT_USER.email);
    }

    /**
     * Returns list of doubloons given to specific user
     * @param email - email of the user for which the doubloon list will be returned
     */
    const getDoubloonsForMember = (email: string) => {

        const doubloonsForMember: Doubloon[] = [];

        for (let i = 0; i < doubloons.length; i++) {
            if (doubloons[i].givenTo === email)
                doubloonsForMember.push(doubloons[i]);
        }

        return doubloonsForMember;
    };

    /**
     * Opens the comment popup for selected user
     * @param memberEmail - email of the user, that you want to give a comment to
     */
    const openCommentPopup = (memberEmail: string) => {
        setShowCommentPopupForUser(memberEmail);
    }

    /**
     * Closes the comment popup (and updates comments state if the comment param is not undefined)
     * If you just want to close the popup, do not pass any parameter. If you want to update the comments state, pass a new comment.
     * @param comment - default value: undefined (state is not updated)
     */
    const closeCommentPopup = (comment: CommentDTO | undefined = undefined) => {
        if (comment !== undefined)
            onCommentChange(comment);

        setShowCommentPopupForUser(undefined);
    }


    return (
        <>

            <Timetable userDoubloonStats={userDoubloonStats} otherTeamMembers={members.length} maxAmountPerUser={maxAmount}/>

            <div className="content">
                <div className="heading">
                    <h2>{teamInfo.teamName}</h2>
                    <div className="underline"></div>
                    <div className="team-members-container">
                        <>
                        {
                             doubloonsGotFetched && members.length > 0 && categories.length > 0 &&
                             members.map(member => (
                                <TeamMemberView key={member.email}
                                                categories={categories}
                                                userView={member}
                                                doubloons={getDoubloonsForMember(member.email)}
                                                openCommentPopup={openCommentPopup}
                                                amountLeft={userDoubloonStats.left}
                                                onDoubloonChange={onDoubloonChange}/>
                            ))
                        }
                        </>
                    </div>
                </div>
            </div>
                {
                    showCommentPopupForUser && <CommentPopup
                                                closeCommentPopup={closeCommentPopup}
                                                comment={
                                                    comments.filter((com) => com.givenTo === showCommentPopupForUser).length < 1 ?
                                                        // if there is no comment for specific user, pass a default comment object
                                                        {
                                                            monthAndYear: getCurrentDateString(),
                                                            givenTo: showCommentPopupForUser,
                                                            givenBy: CURRENT_USER.email,
                                                            comment: "",
                                                        }
                                                        :
                                                        comments.filter((com) => com.givenTo === showCommentPopupForUser)[0]
                                                }/>
                }
        </>
    )
}

export default RateTeamContent;
