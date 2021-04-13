/**
 * The NavLink class provides the constructor to build a route for the given navigation link.
 */
export class NavLink{

    constructor(name="missing-link-name", to="/") {
        this.name = name;
        this.to = to;
    }
}
