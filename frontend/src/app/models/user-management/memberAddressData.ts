/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface MemberAddressData {
    /** อำเภอ (sys_amphur) */
    addressAmphur?: number;
    /** ที่อยู่ */
    addressDetail?: string;
    /** จังหวัด (sys_province) */
    addressProvince?: number;
    /** ตำบล (sys_tambon) */
    addressTambon?: number;
    /** ประเภทที่อยู่ 30031000 */
    addressType?: number;
    /** รหัสไปรษณีย์ */
    addressZipcode?: number;
    first?: number;
    /** PK */
    memberAddressId?: number;
    /** Fk (member_info) */
    memberId?: number;
    mode?: string;
    rowNum?: number;
    size?: number;
}
