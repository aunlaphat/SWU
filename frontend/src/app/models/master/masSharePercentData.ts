/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface MasSharePercentData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** ส่วนแบ่งคณะ */
    costShareDepPercent?: number;
    /** ส่วนแบ่งมหาวิทยาลัย */
    costShareGlobalPercent?: number;
    /** ส่วนแบ่งศูนย์บริการวิชาการ */
    costShareCenterPercent?:number;
    /** สร้างโดย */
    createByUserId?: number;
    /** fk (dep_id) */
    depId?: number;
    /** ชื่อคณะ (ภาษาอังกฤษ) */
    depNameEn?: string;
    /** ชื่อคณะ (ภาษาไทย) */
    depNameTh?: string;
    first?: number;
    mode?: string;
    rowNum?: number;
    /** PK auto run (share_percent_id)  */
    sharePercentId?: number;
    size?: number;
    /** วันที่อัพเดตล่าสุด */
    updateDate?: string;


}
