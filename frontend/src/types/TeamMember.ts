import UserView from './UserView'
import Doubloon from "./Doubloon";
import Category from "./Category";

type TeamMember = {
    userView: UserView,
    categories: Category[],
    doubloons: Doubloon[],
    comment: Function,


}


export default TeamMember;