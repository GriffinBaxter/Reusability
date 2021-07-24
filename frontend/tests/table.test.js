import {afterEach, beforeAll, describe, expect, jest, test} from "@jest/globals";
import Table from "../src/components/Table";
import {shallowMount} from "@vue/test-utils";

describe("Testing the Search page functionality", () => {

    let $router;
    let updateTableSpy;
    let sortRowsSpy;
    let updateOrderBySpy;
    let buildRowsSpy;
    let loadCurrentPageRowsSpy;

    beforeAll(() => {
        $router = {
            push: jest.fn()
        };

        updateTableSpy = jest.spyOn(Table.methods, 'updateTable');
        sortRowsSpy = jest.spyOn(Table.methods, 'sortRows');
        updateOrderBySpy = jest.spyOn(Table.methods, 'updateOrderBy');
        buildRowsSpy = jest.spyOn(Table.methods, 'buildRows');
        loadCurrentPageRowsSpy = jest.spyOn(Table.methods, 'loadCurrentPageRows');
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe("Testing the updatePage method", () => {

        test("Testing that when currentPageOverride is null then updateTable is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1"],
                        tableData: ["data"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: null,
                        orderByOverride: null
                    },
                });

            tableWrapper.vm.updatePage(2);

            expect(tableWrapper.vm.currentPage).toEqual(2);
            // 2 as it was called once during mount
            expect(updateTableSpy).toHaveBeenCalledTimes(2);
        });

        test("Testing that when currentPageOverride is not null then an emit occurs", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1"],
                        tableData: ["data"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: null
                    },
                });

            tableWrapper.vm.updatePage(2);
            tableWrapper.vm.$nextTick();

            expect(tableWrapper.emitted('update-current-page')[0]).toStrictEqual([{tableId: "1", newPageNumber: 2}]);
            expect(tableWrapper.vm.currentPage).toEqual(1);
            // 1 as it was called once during mount
            expect(updateTableSpy).toHaveBeenCalledTimes(1);
        });
    });

    describe("Testing the updatePage method", () => {

        test("Testing that when orderByOverride is null and orderBy does not equal newHeaderIndex then isAscending is true and sortRows is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: null
                    },
                });

            tableWrapper.vm.orderBy = 1;
            tableWrapper.vm.isAscending = false;

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);

            tableWrapper.vm.updateOrderBy(0);

            expect(tableWrapper.vm.orderBy).toEqual(0);
            expect(tableWrapper.vm.isAscending).toEqual(true);
            expect(sortRowsSpy).toHaveBeenCalledTimes(1);
        });

        test("Testing that when orderByOverride is null and orderBy equals newHeaderIndex then the value of isAscending is flipped and sortRows is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: null
                    },
                });

            tableWrapper.vm.orderBy = 1;
            tableWrapper.vm.isAscending = false;

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);

            tableWrapper.vm.updateOrderBy(1);

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(true);
            expect(sortRowsSpy).toHaveBeenCalledTimes(1);

            tableWrapper.vm.updateOrderBy(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);
        });

        test("Testing that when orderByOverride is not null and orderBy does not equal newHeaderIndex then isAscending is true and an emit occurs", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.orderBy = 1;
            tableWrapper.vm.isAscending = false;

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);

            tableWrapper.vm.updateOrderBy(0);

            tableWrapper.vm.$nextTick();

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);
            expect(sortRowsSpy).toHaveBeenCalledTimes(0);
            expect(tableWrapper.emitted('order-by-header-index')[0]).toStrictEqual([{tableId: "1", orderBy: 0, isAscending: true}]);
        });

        test("Testing that when orderByOverride is not null and orderBy equals newHeaderIndex then the value of isAscending is flipped and an emit occurs", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.orderBy = 1;
            tableWrapper.vm.isAscending = true;

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(true);

            tableWrapper.vm.updateOrderBy(1);

            tableWrapper.vm.$nextTick();

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(true);
            expect(sortRowsSpy).toHaveBeenCalledTimes(0);
            expect(tableWrapper.emitted('order-by-header-index')[0]).toStrictEqual([{tableId: "1", orderBy: 1, isAscending: false}]);
        });
    });

    describe("Testing the sortRows method", () => {

        test("Testing that when orderBy is null, then updateTable is not called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.orderBy = null;

            tableWrapper.vm.sortRows();

            // 1 because updateTable is called once during mount
            expect(updateTableSpy).toHaveBeenCalledTimes(1);
        });

        test("Testing that when orderBy is not null, then updateTable is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.orderBy = 0;

            tableWrapper.vm.sortRows();

            // 2 because updateTable is called once during mount
            expect(updateTableSpy).toHaveBeenCalledTimes(2);
        });
    });

    describe("Testing the handleHeaderClick method", () => {

        test("Testing that when handleHeaderClick is triggered then updateOrderBy is called with headerIndex", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.handleHeaderClick(1);

            expect(updateOrderBySpy).toHaveBeenCalledWith(1);
        });
    });

    describe("Testing the handleHeaderKeyDown method", () => {

        test("Testing that when handleHeaderKeyDown is triggered with an enter press then updateOrderBy is called with headerIndex", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            let event = {
                keyCode: 13
            };

            tableWrapper.vm.handleHeaderKeyDown(event, 1);

            expect(updateOrderBySpy).toHaveBeenCalledWith(1);
        });

        test("Testing that when handleHeaderKeyDown is triggered with a key press other than enter then updateOrderBy is not called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            let event = {
                keyCode: 14
            };

            tableWrapper.vm.handleHeaderKeyDown(event, 1);

            expect(updateOrderBySpy).toHaveBeenCalledTimes(0);
        });
    });

    describe("Testing the handleRowClick method", () => {

        test("Testing that when handleRowClick is triggered then an emit occurs with relativeRowIndex", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.currentPage = 1;

            tableWrapper.vm.handleRowClick(1);

            tableWrapper.vm.$nextTick();

            expect(tableWrapper.emitted('row-selected')[0]).toStrictEqual([{tableId: "1", index: 1 + 5 * 1}]);
        });
    });

    describe("Testing the handleRowKeyDown method", () => {

        test("Testing that when handleRowKeyDown is triggered with an enter press then an emit occurs with relativeRowIndex", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.currentPage = 1;

            let event = {
                keyCode: 13
            };

            tableWrapper.vm.handleRowKeyDown(event, 1);

            tableWrapper.vm.$nextTick();

            expect(tableWrapper.emitted('row-selected')[0]).toStrictEqual([{tableId: "1", index: 1 + 5 * 1}]);
        });

        test("Testing that when handleRowKeyDown is triggered with a key press other than enter then an emit occurs doesn't occur", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.currentPage = 1;

            let event = {
                keyCode: 14
            };

            tableWrapper.vm.handleRowKeyDown(event, 1);

            tableWrapper.vm.$nextTick();

            expect(tableWrapper.emitted('row-selected')).toBeFalsy();
        });
    });

    describe("Testing the updateTable method", () => {

        test("Testing that if there is a currentPageOverride then currentPage is set to it", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.currentPage = 0;
            expect(tableWrapper.vm.currentPage).toEqual(0);

            tableWrapper.vm.updateTable();

            expect(tableWrapper.vm.currentPage).toEqual(1);
        });

        test("Testing that if there is an orderByOverride then orderBy and isAscending are set to its values", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.orderBy = 1;
            tableWrapper.vm.isAscending = false;
            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toEqual(false);

            tableWrapper.vm.updateTable();

            expect(tableWrapper.vm.orderBy).toEqual(0);
            expect(tableWrapper.vm.isAscending).toEqual(true);
        });

        test("Testing that if totalPages is greater than zero and currentPage is greater than totalPages - 1 then there is a router push to /pageDoesNotExist", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.totalPages = 1;
            tableWrapper.vm.currentPage = 1;

            tableWrapper.vm.updateTable();

            expect($router.push).toHaveBeenLastCalledWith({path: "/pageDoesNotExist"});
        });

        test("Testing that if newData is true then buildRows is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.updateTable(true, false);

            // 2 as buildRows and loadCurrentPageRows are called once during mount by updateTable
            expect(buildRowsSpy).toHaveBeenCalledTimes(2);
            expect(loadCurrentPageRowsSpy).toHaveBeenCalledTimes(2);
            expect(tableWrapper.vm.dataIsReady).toBeTruthy();
        });

        test("Testing that if rebuildRows is true then buildRows is called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.updateTable(false, true);

            // 2 as buildRows and loadCurrentPageRows are called once during mount by updateTable
            expect(buildRowsSpy).toHaveBeenCalledTimes(2);
            expect(loadCurrentPageRowsSpy).toHaveBeenCalledTimes(2);
            expect(tableWrapper.vm.dataIsReady).toBeTruthy();
        });

        test("Testing that if both newData and rebuildRows are false then buildRows is not called", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data", "data2"],
                        maxRowsPerPage: 5,
                        totalRows: 1,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.updateTable(false, false);

            // 1 as buildRows is called once during mount by updateTable
            expect(buildRowsSpy).toHaveBeenCalledTimes(1);
            // 2 as loadCurrentPageRows is called once during mount by updateTable
            expect(loadCurrentPageRowsSpy).toHaveBeenCalledTimes(2);
            expect(tableWrapper.vm.dataIsReady).toBeTruthy();
        });
    });

    describe("Testing the buildRows method", () => {

        test("Testing that the rows are built correctly", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 5,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.buildRows();

            expect(tableWrapper.vm.rows).toStrictEqual([["data11", "data12"], ["data21", "data22"]]);
        });
    });

    describe("Testing the loadCurrentPageRows method", () => {

        test("Testing that when tableDataIsPage is true the page rows are loaded correctly", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 1,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: true,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.buildRows();

            expect(tableWrapper.vm.rows).toStrictEqual([["data11", "data12"], ["data21", "data22"]]);

            tableWrapper.vm.loadCurrentPageRows();

            expect(tableWrapper.vm.currentPageRows).toStrictEqual([["data11", "data12"]]);
        });

        test("Testing that when tableDataIsPage is false the page rows are loaded correctly", () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 1,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: false,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            tableWrapper.vm.buildRows();

            expect(tableWrapper.vm.rows).toStrictEqual([["data11", "data12"], ["data21", "data22"]]);

            tableWrapper.vm.currentPage = 1;

            tableWrapper.vm.loadCurrentPageRows();

            expect(tableWrapper.vm.currentPageRows).toStrictEqual([["data21", "data22"]]);
        });
    });

    describe("Testing the watch functionality", () => {

        test("Testing that when tableData is changed then updateTable is called with true", async () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 1,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: false,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            expect(updateTableSpy).toHaveBeenCalledTimes(1);

            await tableWrapper.setProps({tableData: ["data", "moreData"]});
            await tableWrapper.vm.$nextTick();

            expect(updateTableSpy).toHaveBeenCalledTimes(2);
        });

        test("Testing that when currentPageOverride is changed then currentPage is set to the new value and updateTable is called with true", async () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 1,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: false,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            expect(updateTableSpy).toHaveBeenCalledTimes(1);

            await tableWrapper.setProps({currentPageOverride: 0});
            await tableWrapper.vm.$nextTick();

            expect(tableWrapper.vm.currentPage).toEqual(0);
            expect(updateTableSpy).toHaveBeenCalledTimes(2);
        });

        test("Testing that when orderByOverride is changed then orderBy is set to the new value and updateTable is called with true", async () => {
            const tableWrapper = shallowMount(
                Table,
                {
                    mocks: {
                        $router,
                    },
                    propsData: {
                        tableId: "1",
                        tableTabIndex: 0,
                        tableHeaders: ["header1", "header2"],
                        tableData: ["data11", "data12", "data21", "data22"],
                        maxRowsPerPage: 1,
                        totalRows: 2,
                        nullStringValue: "",
                        nullWeightHigher: true,
                        tableDataIsPage: false,
                        currentPageOverride: 1,
                        orderByOverride: {orderBy: 0, isAscending: true}
                    },
                });

            expect(updateTableSpy).toHaveBeenCalledTimes(1);

            await tableWrapper.setProps({orderByOverride: {orderBy: 1, isAscending: false}});
            await tableWrapper.vm.$nextTick();

            expect(tableWrapper.vm.orderBy).toEqual(1);
            expect(tableWrapper.vm.isAscending).toBeFalsy();
            expect(updateTableSpy).toHaveBeenCalledTimes(2);
        });
    });
});