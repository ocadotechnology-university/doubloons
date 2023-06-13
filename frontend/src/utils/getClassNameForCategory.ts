const getClassNameForCategory = (categoryName: string) => {
    const categoryNameLowerCase = categoryName.toLowerCase();

    if (categoryNameLowerCase === 'learn fast')
        return 'learn-fast';
    else
        return categoryNameLowerCase;
}

export default getClassNameForCategory;