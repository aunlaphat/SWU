/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */

export interface MemberCourseData {
    /** coursepublic_id */
    coursepublicId?: number;
    first?: number;
    memberCount?: number;
    /** member_course_id(PK) */
    memberCourseId?: number;
    /** member_firstname_en */
    memberFirstnameEn?: string;
    /** member_firstname_th */
    memberFirstnameTh?: string;
    /** member_id */
    memberId?: number;
    /** member_lastname_en */
    memberLastnameEn?: string;
    /** member_lastname_th */
    memberLastnameTh?: string;
    /** member_photo_path */
    filename?: string;
    prefix?: string;
    module?: number;

    memberPhoneno?: string;
    mode?: string;
    nameOrSurnameEn?: string;
    nameOrSurnameTh?: string;
    /** pass_status */
    passStatus?: boolean;
    /** paymentNameEn */
    paymentNameEn?: string;
    /** paymentNameLo */
    paymentNameLo?: string;
    /** payment_status */
    paymentStatus?: number;
    /** prefixname_code */
    prefixnameCode?: string;
    /** prefixname_name_en */
    prefixnameNameEn?: string;
    /** prefixname_name_th */
    prefixnameNameTh?: string;
    /** register_date */
    registerDate?: string;
    /** result_grade */
    resultGrade?: string;
    /** result_score */
    resultScore?: number;
    rowNum?: number;
    size?: number;
    /** study_status */
    studyStatus?: number;
    /** studyStatusNameEn */
    studyStatusNameEn?: string;
    /** studyStatusNameLo */
    studyStatusNameLo?: string;
}
