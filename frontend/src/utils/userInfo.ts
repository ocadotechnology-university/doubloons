import UserInfo from "../types/UserInfo";


const noUser: UserInfo = {
    email: "",
    teamId: "",
}

let currentUser = noUser;

const getUserInfo = () => {
    if (currentUser !== noUser) return Promise.resolve(currentUser);
    return fetch(`/api/user/info`)
        .then(response => response.json())
        .then(jsonResponse => { 
            currentUser = { email: jsonResponse["email"], teamId: jsonResponse["teamId"] }
            return currentUser
        })
        .catch(e => {
            console.log(`Failed to fetch user info ${e}`);
            return noUser;
        });
}

export default getUserInfo;