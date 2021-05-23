export default {
    /**
     * Opens or hides the autofill list items based on the given input.
     *
     * This function is based off of the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    toggleList(direction, list) {
        if (direction === 'open') {
            list.classList.remove('hidden-all');
        } else {
            list.classList.add('hidden-all');
        }
    },
    /**
     * Filters current autofill list items based on the input.
     *
     * This function is adapted from the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    filterOptions(inputValue, listItems, autofillState) {
        const listItemsArray = Array.from(listItems);
        const filteredOptions = listItemsArray.filter(function(item) {
            if (item.innerText.toUpperCase().includes(inputValue.toUpperCase())) {
                return true;
            }
        })

        listItemsArray.forEach(item => item.style.display = "none")
        filteredOptions.forEach(function(item) {
            item.style.display = "";
        })
        // eslint-disable-next-line
        autofillState = 'filtered';
    },
    /**
     * Move the current focus up and down the autofill list based on the given 'to' and 'from' values.
     *
     * This function is adapted from the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    moveFocus(from, to, input, listItemsRaw, currentItem) {
        const listItems = Array.from(listItemsRaw);
        // Get the currently showing items, which might have been filtered
        const currentOptions = listItems.filter(function(item) {
            if (item.style.display === '') {
                return true
            }
        })
        // Don't move if all options have been filtered out
        if (currentOptions.length === 0) {
            return;
        }
        if (to === 'input') {
            input.focus()
        }

        const whichOne = currentOptions.indexOf(currentItem);
        // Possible start points
        switch(from) {
            case input:
                if (to === 'forward') {
                    currentOptions[0].focus()
                } else if (to === 'back') {
                    currentOptions[currentOptions.length - 1].focus()
                }
                break;
            case currentOptions[0]:
                if (to === 'forward') {
                    currentOptions[1].focus()
                } else if (to === 'back') {
                    input.focus()
                }
                break;
            case currentOptions[currentOptions.length - 1]:
                if (to === 'forward') {
                    currentOptions[0].focus()
                } else if (to === 'back') {
                    currentOptions[currentOptions.length - 2].focus()
                }
                break;
            default: // In the middle of list or filtered items
                if (to === 'forward') {
                    const nextOne = currentOptions[whichOne + 1]
                    nextOne.focus()
                } else if (to === 'back' && whichOne > 0) {
                    const previousOne = currentOptions[whichOne - 1]
                    previousOne.focus()
                } else { // if whichOne = 0
                    input.focus()
                }
                break;
        }
    },
}
