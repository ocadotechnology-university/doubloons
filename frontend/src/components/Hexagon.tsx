import React from 'react';
import getClassNameForCategory from "../utils/getClassNameForCategory";
import './Hexagon.css';

interface HexagonProps {
    category: string | undefined;
    size: 'small' | 'medium';
}

const Hexagon: React.FC<HexagonProps> = ({ category, size }) => {

    if (category === undefined)
        category = 'Unknown';

    let hexagonStyle = '';
    let lettersStyle = '';

    switch (size) {
        case 'small': hexagonStyle = 'hexagon-small'; lettersStyle = 'hexagon-small-text'; break;
        case 'medium': hexagonStyle = 'hexagon-medium'; lettersStyle = 'hexagon-medium-text'; break;
    }

    const letters = category.slice(0, 2);

    const backgroundStyle = getClassNameForCategory(category);



    return (
        <div className={`hexagon ${backgroundStyle} ${hexagonStyle}`}>
            <div className={`hexagon-letters ${lettersStyle}`}>
                    {letters}
            </div>
        </div>
    );
};

export default Hexagon;
