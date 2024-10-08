/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface CoursepublicInstructorData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** base64 */
    base64?: string;
    /** FK (coursepublic_main) */
    coursepublicId?: number;
    /** PK */
    coursepublicInstructorId?: number;
    /** custom อีเมล */
    email?: string;
    /** อีเมล */
    externalEmail?: string;
    /** ชื่ออาจารย์/ผู้รับผิดชอบ  (ภาษาอังกฤษ) */
    externalNameEn?: string;
    /** ชื่ออาจารย์/ผู้รับผิดชอบ (ภาษาไทย) */
    externalNameTh?: string;
    /** FileName */
    filename?: string;
    first?: number;
    /** custom ชื่ออาจารย์  (ภาษาอังกฤษ) */
    fullnameEn?: string;
    /** custom ชื่ออาจารย์ (ภาษาไทย) */
    fullnameTh?: string;
    /** FK (mas_personal) */
    instructorId?: number;
    /** อาจารย์/ผู้รับผิดชอบหลักสูตร */
    instructorMain?: boolean;
    /** บุคลากรภายนอก */
    instructorType?: boolean;
    mode?: string;
    /** Module */
    module?: number;
    /** Prefix */
    prefix?: string;
    /** ชื่อย่อคำนำหน้า (อังกฤษ) */
    prefixShortEn?: string;
    /** ชื่อย่อคำนำหน้า (ไทย) */
    prefixShortTh?: string;
    rowNum?: number;
    size?: number;
}
