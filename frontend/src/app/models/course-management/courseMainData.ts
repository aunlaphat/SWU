/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface CourseMainData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** custom count coursepublic_id  */
    countCoursepublicId?: number;
    /** จำนวนชั่วโมงการเรียนปฏิบัติ */
    courseActionH: number;
    /** รหัสหลักสูตร */
    courseCode: string;
    /** คำอธิบายรายวิชาของหลักสูตร (ภาษาอังกฤษ) */
    courseDescEn: string;
    /** คำอธิบายรายวิชาของหลักสูตร (ภาษาไทย) */
    courseDescTh: string;
    /** ระยะเวลาเรียนตลอดหลักสูตรอบรมระยะสั้น */
    courseDurationTime: number;
    /** รูปแบบการเรียนการสอน LOOKUP (30003000) */
    courseFormat: number;
    /** รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาอังกฤษ) */
    courseFormatDescEn: string;
    /** รายละเอียดรูปแบบการสอนเพิ่มเติม (ภาษาไทย) */
    courseFormatDescTh: string;
    /** custom รูปแบบการเรียนการสอน (ภาษาอังกฤษ) */
    courseFormatEn?: string;
    /** custom รูปแบบการเรียนการสอน (ภาษาไทย) */
    courseFormatTh?: string;
    /** ชื่อ Tag */
    courseHashtag?: string[];
    /** PK */
    courseId?: number;
    /** สถานะคำขอเปิดหลักสูตร LOOKUP (30010000) */
    courseMainStatus: number;
    /** custom สถานะคำขอเปิดหลักสูตร (ภาษาอังกฤษ) */
    courseMainStatusEn?: string;
    /** custom สถานะคำขอเปิดหลักสูตร (ภาษาไทย) */
    courseMainStatusTh?: string;
    /** custom true = DEGREE, show tab */
    courseMappingStatus?: boolean;
    /** ชื่อหลักสูตร (ภาษาอังกฤษ) */
    courseNameEn: string;
    /** ชื่อหลักสูตร (ภาษาไทย) */
    courseNameTh: string;
    /** สถานภาพของหลักสูตรอบรมระยะสั้น LOOKUP (30022000) */
    courseNewStatus?: number;
    /** custom สถานภาพของหลักสูตรอบรมระยะสั้น (ภาษาอังกฤษ) */
    courseNewStatusEn?: string;
    /** custom สถานภาพของหลักสูตรอบรมระยะสั้น (ภาษาไทย) */
    courseNewStatusTh?: string;
    /** Course Reference ID */
    courseRefId?: number;
    /** ความรู้พื้นฐาน/คุณสมบัติของผู้เข้าอบรม  (ภาษาอังกฤษ) */
    courseSpecificRequirementEn: string;
    /** ความรู้พื้นฐาน/คุณสมบัติของผู้เข้าอบรม  (ภาษาไทย) */
    courseSpecificRequirementTh: string;
    /** จำนวนชั่วโมงการเรียนทฤษฏี */
    courseTheoryH: number;
    /** รวมจำนวนชั่วโมงเรียน */
    courseTotalH: number;
    /** custom รหัสประเภทหลักสูตร  */
    courseTypeCode?: string;
    /** FK (mas_course_type) */
    courseTypeId?: number;
    /** custom ชื่อประเภทหลักสูตร (อังกฤษ) */
    courseTypeNameEn?: string;
    /** custom ชื่อประเภทหลักสูตร (ภาษาไทย) */
    courseTypeNameTh?: string;
    /** Course Version */
    courseVersion?: number;
    /** custom เวลาที่บันทึก */
    createDate?: string;
    /** จำนวนหน่วยกิต */
    creditAmount: number;
    /** custom รหัสคณะ/หน่วยงาน Level1 */
    depCodeLevel1?: string;
    /** custom รหัสคณะ/หน่วยงาน Level2 */
    depCodeLevel2?: string;
    /** FK (mas_department) */
    depIdLevel1?: number;
    /** FK (mas_department) */
    depIdLevel2?: number;
    /** custom ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1 */
    depNameAbbrEnLevel1?: string;
    /** custom ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2 */
    depNameAbbrEnLevel2?: string;
    /** custom ตัวย่อคณะ/หน่วยงาน (ภาษาไทย) Level1 */
    depNameAbbrThLevel1?: string;
    /** custom ตัวย่อคณะ/หน่วยงาน (ภาษาไทย) Level2 */
    depNameAbbrThLevel2?: string;
    /** custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1 */
    depNameEnLevel1?: string;
    /** custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2 */
    depNameEnLevel2?: string;
    /** custom ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level1 */
    depNameShortEnLevel1?: string;
    /** custom ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ) Level2 */
    depNameShortEnLevel2?: string;
    /** custom ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย) Level1 */
    depNameShortThLevel1?: string;
    /** custom ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย) Level2 */
    depNameShortThLevel2?: string;
    /** custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย) Level1 */
    depNameThLevel1?: string;
    /** custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย) Level2 */
    depNameThLevel2?: string;
    /** หน่วยเวลา LOOKUP (30004000) */
    durationTimeUnit: number;
    durationTimeUnitEn?: string;
    /** custom หน่วยเวลา (ภาษาไทย) */
    durationTimeUnitTh?: string;
    first?: number;
    /** สถานะการเพิ่มแบบ Break Rule (ไม่ต้องอนุมัติ) */
    forceStatus: boolean;
    /** รูปแบบผลการเรียน LOOKUP (30005000) */
    gradeFormat: number;
    gradeFormatEn?: string;
    /** custom รูปแบบผลการเรียน (ภาษาไทย) */
    gradeFormatTh?: string;
    /** custom กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน (ภาษาอังกฤษ) */
    industryGroupEn?: string;
    /** กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน LOOKUP (30001000) */
    industryGroupId: number;
    /** custom กลุ่มอุตสาหกรรม/กลุ่มพัฒนาคน (ภาษาไทย) */
    industryGroupTh?: string;
    /** custom การเทียบเคียงหลักสูตร (ภาษาอังกฤษ) */
    mappingStatusEn?: string;
    /** custom การเทียบเคียงหลักสูตร (ภาษาไทย) */
    mappingStatusTh?: string;
    /** custom max create_date  */
    maxCreateDate?: string;
    mode?: string;
    /** custom อาชีพ (ภาษาอังกฤษ) */
    occupationEn?: string[];
    /** custom อาชีพ (ภาษาไทย) */
    occupationTh?: string[];
    /** วันที่ขออนุมัติ */
    requestDate?: string;
    rowNum?: number;
    /** custom ผู้บันทึก (ภาษาอังกฤษ) */
    saveByFullNameEn?: string;
    /** custom ผู้บันทึก (ภาษาไทย) */
    saveByFullNameTh?: string;
    size?: number;
    /** custom เป้าหมาย (ภาษาอังกฤษ) */
    targetEn?: string[];
    /** ชื่อกลุ่มเป้าหมายอื่น */
    targetGroupOtherName: string;
    /** กลุ่มเป้าหมายอื่นๆ */
    targetGroupOtherStatus: boolean;
    /** custom เป้าหมาย (ภาษาไทย) */
    targetTh?: string[];
    /** ผู้ที่บันทึก */
    createById?: number;

}
