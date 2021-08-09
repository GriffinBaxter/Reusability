import {format, parseISO} from "date-fns";

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
            return format(parseISO(dateString), "do MMM yyyy h:m bbb")
        } else {
            return dateString ? format(dateString, "do MMM yyyy h:m bbb") : null
        }
    } else {
        if (typeof dateString === "string") {
            return format(parseISO(dateString), "do MMM yyyy")
        } else {
            return dateString ? format(dateString, "do MMM yyyy") : null
        }
    }
}
