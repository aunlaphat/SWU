/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

import { CourseActivityMethodData } from './courseActivityMethodData';

export interface CourseActivityData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** การวัดและประเมินผล (อังกฤษ) */
    courseActivityAssessEn?: string;
    /** การวัดและประเมินผล (ไทย) */
    courseActivityAssessTh?: string;
    /** PK */
    courseActivityId?: number;
    /** รายละเอียดกิจกรรมการเรียนการสอนเพิ่มเติม */
    courseActivityMethodMore?: string;
    /** custom วิธีการสอน (ภาษาไทย) */
    coruseActivityMethodTh?: string;
    /** custom วิธีการสอน (ภาษาอังกฤษ) */
    coruseActivityMethodEn?: string;
    /** ระยะเวลา (ชั่วโมง) */
    courseActivityPeriod?: number;
    /** เนื้อหา/กระบวนการ (อังกฤษ) */
    courseActivityTopicEn?: string;
    /** เนื้อหา/กระบวนการ (ไทย) */
    courseActivityTopicTh?: string;
    /** FK (course_main) */
    courseId?: number;
    /** custom วิธีการสอน Fk(course_activity_method) */
    courseActiviryMethodList?: CourseActivityMethodData[];
    first?: number;
    mode?: string;
    rowNum?: number;
    size?: number;
}
