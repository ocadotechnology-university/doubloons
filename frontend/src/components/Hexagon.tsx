import React from 'react';
import getClassNameForCategory from "../utils/getClassNameForCategory";

interface HexagonProps {
    category: string;
    size: number,
}

const Hexagon: React.FC<HexagonProps> = ({ category, size }) => {

    const proportions = 11/12;
    const width = size;
    const height = (size * proportions);
    const fontSize = width / 2.2;

    const letters = category.slice(0, 2);

    const backgroundStyle = getClassNameForCategory(category);


    const hexagonStyle = {
        width: width,
        height: height,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        clipPath:  // rig Mid |rig Down|lef Down|lef Mid|left Up| rig Up
            'polygon(100% 50%, 75% 100%, 25% 100%, 0% 50%, 25% 0%, 75% 0%)',
    };

    const lettersStyle = {
        fontSize: fontSize,
        color: '#000',
    };

    return (
        <div style={hexagonStyle} className={backgroundStyle}>
            <div style={lettersStyle}>{letters}</div>
        </div>
    );
};

export default Hexagon;
