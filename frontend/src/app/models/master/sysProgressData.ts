/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface SysProgressData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    id?: number;
    executeRow?: number;
	progress?: number;
    tableName?: string;
	toTemp?: number;
	toActual?: number;
}
