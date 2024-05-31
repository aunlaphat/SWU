/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface CourseLogData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** FK (course_main) */
    courseId?: number;
    /** PK */
    courseLogId?: number;
    /** สถานะคำขอเปิดหลักสูตร LOOKUP (30010000)  */
    courseMainStatus?: number;
    first?: number;
    mode?: string;
    rowNum?: number;
    size?: number;
    createDate?: Date;

    statusnameTh?: string;
    statusnameEn?: string;
    fullnameTh?: string;
    fullnameEn?: string;
}
