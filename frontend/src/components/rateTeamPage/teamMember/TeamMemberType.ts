import UserView from '../../../types/UserView'
import Doubloon from "../../../types/Doubloon";
import Category from "../../../types/Category";

type TeamMemberType = {
    userView: UserView,
    categories: Category[],
    doubloons: Doubloon[],
    comment: Function,
    amountLeft: number,
    onDoubloonChange: Function,

}


export default TeamMemberType;