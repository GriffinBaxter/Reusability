import {format, parseISO} from "date-fns";

export function formatDate(dateString) {
    if (typeof dateString === "string") {
        return format(parseISO(dateString), "do MMM yyyy h:m bbb")
    }
    else {
        throw new Error("MUST BE TYPE STRING")
    }
}