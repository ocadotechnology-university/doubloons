import React, { useState } from 'react';
import './TextField.css';
const TextField: React.FC = () => {
    const [inputValue, setInputValue] = useState('');

    const handleChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setInputValue(event.target.value);
    };

    return (
            <textarea
                value={inputValue}
                onChange={handleChange}
                className="text-fields"
            />
    );
};

export default TextField;