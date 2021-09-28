/**
 * Jest tests for HomeSales.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeAll} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import HomeSales from "../../src/components/saleInsights/HomeSales";