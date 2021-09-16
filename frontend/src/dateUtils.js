import {format, parseISO} from "date-fns";
import compareAsc from "date-fns/compareAsc";

/**
 * Converts a string that is a date in the ISO format to a format of e.g. "17th May 2021 8:30 PM".
 * If the date string provided does not contain a date, then the format will be in the form of "17th May 2021".
 * @param dateString
 * @param dateAndTime
 * @return {string|null}
 */
export function formatDate(dateString, dateAndTime=true) {
    if (dateString === "") {
        return null
    }
    if (dateAndTime) {
        if (typeof dateString === "string") {
            return format(parseISO(dateString), "do MMM yyyy h:mm aaa")
        } else {
            return dateString ? format(dateString, "do MMM yyyy h:mm aaa") : null
        }
    } else {
        if (typeof dateString === "string") {
            return format(parseISO(dateString), "do MMM yyyy")
        } else {
            return dateString ? format(dateString, "do MMM yyyy") : null
        }
    }
}

/**
 * Checks that the second date is after the first date. Returns true if this is the case.
 * @param firstDate first date
 * @param secondDate second date
 * @return {boolean}
 */
export function isFirstDateBeforeSecondDate(firstDate, secondDate) {
    if (firstDate != null && secondDate != null) {
        const outcome = compareAsc(parseISO(firstDate), parseISO(secondDate))
        return (outcome === -1) // -1 if the first date is before the second
    } else {
        return true
    }
}
