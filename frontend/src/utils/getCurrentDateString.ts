function getCurrentDateString() {
    const date = new Date();
    const month = date.getMonth() + 1; // Add 1 since getMonth() returns zero-based index
    const year = date.getFullYear();

    return `${month.toString().padStart(2, '0')}-${year.toString()}`;
}

export function getLastDateString() {
    const date = new Date();
    const month = date.getMonth(); // Add 1 since getMonth() returns zero-based index
    const year = date.getFullYear();

    return `${month.toString().padStart(2, '0')}-${year.toString()}`;
}

export default getCurrentDateString;