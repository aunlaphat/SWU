/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

import { AutUserData } from "../user-management";

export interface ResponseLoginData {
    user: AutUserData;
    allMenu: AllMenu[];
}

export interface User {
    userId?: number;
    firstname?: string;
    lastname?: string;
    email?: string;
    username?: string;
    tel?: string;
    isLocal?: boolean;
    activeFlag?: boolean;
}

export interface AllMenu {
    menuId?: number;
    menuCode?: string;
    menuType?: string;
    parentId?: number;
    orderNumber?: number;
    name?: string;
    showNameKey?: string;
    link?: string;
    activeFlag?: boolean;
    routerLink?: string;
}
