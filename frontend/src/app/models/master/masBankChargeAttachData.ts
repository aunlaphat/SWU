/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface MasBankChargeAttachData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** PK */
    chargeAttachId?: number;
    /** fk อ้างอิงค่าธรรมเนียมการชำระเงิน */
    chargeId?: number;
    /** Link (มีอยู่แล้ว เพิ่มใหม่แค่ module, prefix ยังคงfilename ไว้)*/
    fileLink?: string;
    /** ชื่อไฟล์ */
    fileName?: string;
    first?: number;
    mode?: string;
    rowNum?: number;
    size?: number;
}
