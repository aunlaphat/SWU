/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface MasBankData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** รหัสธนาคาร */
    bankCode: string;
    /** pk ธนาคาร */
    bankId?: number;
    /** ชื่อธนาคาร (ภาษาอังกฤษ) */
    bankNameEn: string;
    /** ชื่อธนาคาร (ภาษาไทย) */
    bankNameTh: string;
    first?: number;
    mode?: string;
    rowNum?: number;
    size?: number;
}
