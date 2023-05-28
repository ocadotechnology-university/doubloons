const getMonthAndDateLabel = (monthAndDate: string) => {
    const monthNames = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    ]

    const currentMonth = Number(monthAndDate.slice(0, 2));
    const currentYear = monthAndDate.slice(3);

    return `${monthNames[currentMonth-1]} ${currentYear}`
}

export default getMonthAndDateLabel;