import React, { useState } from 'react';
import './TextField.css';
const TextField: React.FC = () => {
    const [inputValue, setInputValue] = useState('');

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(event.target.value);
    };

    return (
            <input
                type="text"
                value={inputValue}
                onChange={handleChange}
                className="text-fields"
            />
    );
};

export default TextField;