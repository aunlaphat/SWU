/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface CourseSkillData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** FK (course_main) */
    courseId?: number;
    /** PK auto run  */
    courseSkillId?: number;
    /** ชื่อทักษะอื่น (อังกฤษ) */
    courseSkillOtherNameEn?: string;
    /** ชื่อทักษะอื่น (ไทย) */
    courseSkillOtherNameTh?: string;
    /** อื่นๆ */
    courseSkillOtherStatus?: boolean;
    first?: number;
    /** หมวดหมู่ทักษะ */
    generalSkillId?: number;
    /** custom ชื่อทักษะ (อังกฤษ) */
    generalSkillNameEn?: string;
    /** custom ชื่อทักษะ (ไทย) */
    generalSkillNameTh?: string;
    /** custom ระดับ (อังกฤษ) */
    levelNameEn?: string;
    /** custom ระดับ (ไทย) */
    levelNameTh?: string;
    mode?: string;
    rowNum?: number;
    size?: number;
    /** ระดับ */
    skillLevel?: number;
    /** น้ำหนัก */
    skillWeight?: number;
}
