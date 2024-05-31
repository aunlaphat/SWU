/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import type { CoursepublicMainData } from './../course-management/coursepublicMainData';

export interface TmpFinanceImportLogData {
    /** สถานะการใช้งาน */
    activeFlag?: boolean;
    /** Fk (coursepubic_main) */
    coursepublicId?: number;
    coursepublicMainData?: CoursepublicMainData;
    /** จำนวนที่ไม่ถุกต้อง */
    failRow?: number;
    /** Ref นำเข้า */
    fileReferenceCode?: string;
    first?: number;
    /** ลิงค์ไฟล์นำเข้า */
    impFileLink?: string;
    /** ยอดเงินรวม */
    impFileMoney?: number;
    /** ชื่อไฟล์ */
    impFileName?: string;
    /** จำนวนทั้งหมดตามไฟล์ */
    impFileRow?: number;
    /** ขนาดไฟล์นำเข้า */
    impFileSize?: number;
    /** ข้อความ Error */
    messageError?: string;
    /** จำนวนที่่ไม่พบข้อมูล */
    missRow?: number;
    mode?: string;
    /** Module */
    module?: number;
    /** จำนวนรายการ */
    passRow?: number;
    /** Prefix */
    prefix?: string;
    rowNum?: number;
    size?: number;
    /** PK */
    tmpImpId?: number;
    /** uuid */
    uuid?: string;
}
