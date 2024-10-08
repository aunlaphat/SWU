/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import type {
    MasBankAccountData,
    MasBankBranchData,
    MasBankChargeAttachData,
    MasBankChargeData,
    MasBankData,
    MasCourseTypeData,
    MasDepartmentData,
    MasGeneralSkillData,
    MasOccupationData,
    MasOccupationGroupData,
    MasOccupationSkillData,
    MasPersonalData,
    MasSharePercentAttachData,
    MasSharePercentData,
    MasSharePercentHistoryData,
    NotiInfoData,
    SysProgressData
} from '../models/master';
import { ResponseListIf, ResponseOneIf } from 'src/app/models/common';
import { environment } from 'src/environments/environment';
import { AutUserData } from '../models/user-management';
import { MasWebsiteBannerData } from '../models/master/masWebsiteBannerData';

@Injectable({ providedIn: 'root' })
export class MasterService {
    constructor(private http: HttpClient) {}

    getSharePercent(sharePercentId: number): Observable<ResponseOneIf<MasSharePercentData>> {
        return this.http.get<ResponseOneIf<MasSharePercentData>>(
            `${environment.apiUrl}/master/share-percent/${sharePercentId}`
        );
    }
    putSharePercent(
        sharePercentId: number,
        masSharePercentData: MasSharePercentData
    ): Observable<ResponseOneIf<MasSharePercentData>> {
        return this.http.put<ResponseOneIf<MasSharePercentData>>(
            `${environment.apiUrl}/master/share-percent/${sharePercentId}`,
            masSharePercentData
        );
    }
    getSharePercentHistory(sharePercentHistoryId: number): Observable<ResponseOneIf<MasSharePercentHistoryData>> {
        return this.http.get<ResponseOneIf<MasSharePercentHistoryData>>(
            `${environment.apiUrl}/master/share-percent-history/${sharePercentHistoryId}`
        );
    }
    putSharePercentHistory(
        sharePercentHistoryId: number,
        masSharePercentHistoryData: MasSharePercentHistoryData
    ): Observable<ResponseOneIf<MasSharePercentHistoryData>> {
        return this.http.put<ResponseOneIf<MasSharePercentHistoryData>>(
            `${environment.apiUrl}/master/share-percent-history/${sharePercentHistoryId}`,
            masSharePercentHistoryData
        );
    }
    getSharePercentAttach(sharePercentAttachId: number): Observable<ResponseOneIf<MasSharePercentAttachData>> {
        return this.http.get<ResponseOneIf<MasSharePercentAttachData>>(
            `${environment.apiUrl}/master/share-percent-attach/${sharePercentAttachId}`
        );
    }
    putSharePercentAttach(
        sharePercentAttachId: number,
        masSharePercentAttachData: MasSharePercentAttachData
    ): Observable<ResponseOneIf<MasSharePercentAttachData>> {
        return this.http.put<ResponseOneIf<MasSharePercentAttachData>>(
            `${environment.apiUrl}/master/share-percent-attach/${sharePercentAttachId}`,
            masSharePercentAttachData
        );
    }
    getPersonal(personalId: number): Observable<ResponseOneIf<MasPersonalData>> {
        return this.http.get<ResponseOneIf<MasPersonalData>>(`${environment.apiUrl}/master/personal/${personalId}`);
    }
    getPersonalPull(): Observable<ResponseOneIf<string>> {
        return this.http.get<ResponseOneIf<string>>(`${environment.apiUrl}/master/personal/pull`);
    }
    getPersonalCheck(): Observable<ResponseOneIf<SysProgressData>> {
        return this.http.get<ResponseOneIf<SysProgressData>>(`${environment.apiUrl}/master/personal/check`);
    }
    getPersonalUser(personalId: number): Observable<ResponseOneIf<AutUserData>> {
        return this.http.get<ResponseOneIf<AutUserData>>(`${environment.apiUrl}/master/personal/user/${personalId}`);
    }
    putPersonal(personalId: number, masPersonalData: MasPersonalData): Observable<ResponseOneIf<MasPersonalData>> {
        return this.http.put<ResponseOneIf<MasPersonalData>>(
            `${environment.apiUrl}/master/personal/${personalId}`,
            masPersonalData
        );
    }
    getOccupation(occupationId: number): Observable<ResponseOneIf<MasOccupationData>> {
        return this.http.get<ResponseOneIf<MasOccupationData>>(
            `${environment.apiUrl}/master/occupation/${occupationId}`
        );
    }
    putOccupation(
        occupationId: number,
        masOccupationData: MasOccupationData
    ): Observable<ResponseOneIf<MasOccupationData>> {
        return this.http.put<ResponseOneIf<MasOccupationData>>(
            `${environment.apiUrl}/master/occupation/${occupationId}`,
            masOccupationData
        );
    }
    getOccupationSkill(occSkillId: number): Observable<ResponseOneIf<MasOccupationSkillData>> {
        return this.http.get<ResponseOneIf<MasOccupationSkillData>>(
            `${environment.apiUrl}/master/occupation-skill/${occSkillId}`
        );
    }
    putOccupationSkill(
        occSkillId: number,
        masOccupationSkillData: MasOccupationSkillData
    ): Observable<ResponseOneIf<MasOccupationSkillData>> {
        return this.http.put<ResponseOneIf<MasOccupationSkillData>>(
            `${environment.apiUrl}/master/occupation-skill/${occSkillId}`,
            masOccupationSkillData
        );
    }
    getOccupationGroup(occupationGroupId: number): Observable<ResponseOneIf<MasOccupationGroupData>> {
        return this.http.get<ResponseOneIf<MasOccupationGroupData>>(
            `${environment.apiUrl}/master/occupation-group/${occupationGroupId}`
        );
    }
    putOccupationGroup(
        occupationGroupId: number,
        masOccupationGroupData: MasOccupationGroupData
    ): Observable<ResponseOneIf<MasOccupationGroupData>> {
        return this.http.put<ResponseOneIf<MasOccupationGroupData>>(
            `${environment.apiUrl}/master/occupation-group/${occupationGroupId}`,
            masOccupationGroupData
        );
    }
    getGeneralSkill(generalSkillId: number): Observable<ResponseOneIf<MasGeneralSkillData>> {
        return this.http.get<ResponseOneIf<MasGeneralSkillData>>(
            `${environment.apiUrl}/master/general-skill/${generalSkillId}`
        );
    }
    putGeneralSkill(
        generalSkillId: number,
        masGeneralSkillData: MasGeneralSkillData
    ): Observable<ResponseOneIf<MasGeneralSkillData>> {
        return this.http.put<ResponseOneIf<MasGeneralSkillData>>(
            `${environment.apiUrl}/master/general-skill/${generalSkillId}`,
            masGeneralSkillData
        );
    }
    getDepartment(depId: number): Observable<ResponseOneIf<MasDepartmentData>> {
        return this.http.get<ResponseOneIf<MasDepartmentData>>(`${environment.apiUrl}/master/department/${depId}`);
    }
    getDepartmentPull(): Observable<ResponseOneIf<string>> {
        return this.http.get<ResponseOneIf<string>>(`${environment.apiUrl}/master/department/pull`);
    }
    getDepartmentCheck(): Observable<ResponseOneIf<SysProgressData>> {
        return this.http.get<ResponseOneIf<SysProgressData>>(`${environment.apiUrl}/master/department/check`);
    }
    putDepartment(depId: number, masDepartmentData: MasDepartmentData): Observable<ResponseOneIf<MasDepartmentData>> {
        return this.http.put<ResponseOneIf<MasDepartmentData>>(
            `${environment.apiUrl}/master/department/${depId}`,
            masDepartmentData
        );
    }
    getCourseType(courseTypeId: number): Observable<ResponseOneIf<MasCourseTypeData>> {
        return this.http.get<ResponseOneIf<MasCourseTypeData>>(
            `${environment.apiUrl}/master/course-type/${courseTypeId}`
        );
    }
    putCourseType(
        courseTypeId: number,
        masCourseTypeData: MasCourseTypeData
    ): Observable<ResponseOneIf<MasCourseTypeData>> {
        return this.http.put<ResponseOneIf<MasCourseTypeData>>(
            `${environment.apiUrl}/master/course-type/${courseTypeId}`,
            masCourseTypeData
        );
    }
    getBank(bankId: number): Observable<ResponseOneIf<MasBankData>> {
        return this.http.get<ResponseOneIf<MasBankData>>(`${environment.apiUrl}/master/bank/${bankId}`);
    }
    putBank(bankId: number, masBankData: MasBankData): Observable<ResponseOneIf<MasBankData>> {
        return this.http.put<ResponseOneIf<MasBankData>>(`${environment.apiUrl}/master/bank/${bankId}`, masBankData);
    }
    getBankCharge(chargeId: number): Observable<ResponseOneIf<MasBankChargeData>> {
        return this.http.get<ResponseOneIf<MasBankChargeData>>(`${environment.apiUrl}/master/bank-charge/${chargeId}`);
    }
    putBankCharge(
        chargeId: number,
        masBankChargeData: MasBankChargeData
    ): Observable<ResponseOneIf<MasBankChargeData>> {
        return this.http.put<ResponseOneIf<MasBankChargeData>>(
            `${environment.apiUrl}/master/bank-charge/${chargeId}`,
            masBankChargeData
        );
    }
    getBankChargeAttach(chargeId: number): Observable<ResponseOneIf<MasBankChargeData>> {
        return this.http.get<ResponseOneIf<MasBankChargeData>>(
            `${environment.apiUrl}/master/bank-charge-attach/${chargeId}`
        );
    }
    putBankChargeAttach(
        chargeId: number,
        masBankChargeAttachData: MasBankChargeAttachData
    ): Observable<ResponseOneIf<MasBankChargeData>> {
        return this.http.put<ResponseOneIf<MasBankChargeData>>(
            `${environment.apiUrl}/master/bank-charge-attach/${chargeId}`,
            masBankChargeAttachData
        );
    }
    getBankBranch(bankBranchId: number): Observable<ResponseOneIf<MasBankBranchData>> {
        return this.http.get<ResponseOneIf<MasBankBranchData>>(
            `${environment.apiUrl}/master/bank-branch/${bankBranchId}`
        );
    }
    putBankBranch(
        bankBranchId: number,
        masBankBranchData: MasBankBranchData
    ): Observable<ResponseOneIf<MasBankBranchData>> {
        return this.http.put<ResponseOneIf<MasBankBranchData>>(
            `${environment.apiUrl}/master/bank-branch/${bankBranchId}`,
            masBankBranchData
        );
    }
    getBankAccount(bankAccountId: number): Observable<ResponseOneIf<MasBankAccountData>> {
        return this.http.get<ResponseOneIf<MasBankAccountData>>(
            `${environment.apiUrl}/master/bank-account/${bankAccountId}`
        );
    }
    getBankAccountActive(): Observable<ResponseOneIf<MasBankAccountData>> {
        return this.http.get<ResponseOneIf<MasBankAccountData>>(`${environment.apiUrl}/master/bank-account/active`);
    }
    putBankAccount(
        bankAccountId: number,
        masBankAccountData: MasBankAccountData
    ): Observable<ResponseOneIf<MasBankAccountData>> {
        return this.http.put<ResponseOneIf<MasBankAccountData>>(
            `${environment.apiUrl}/master/bank-account/${bankAccountId}`,
            masBankAccountData
        );
    }
    postSharePercent(masSharePercentData: MasSharePercentData): Observable<ResponseOneIf<MasSharePercentData>> {
        return this.http.post<ResponseOneIf<MasSharePercentData>>(
            `${environment.apiUrl}/master/share-percent`,
            masSharePercentData
        );
    }
    postSharePercentHistory(
        masSharePercentHistoryData: MasSharePercentHistoryData
    ): Observable<ResponseOneIf<MasSharePercentHistoryData>> {
        return this.http.post<ResponseOneIf<MasSharePercentHistoryData>>(
            `${environment.apiUrl}/master/share-percent-history`,
            masSharePercentHistoryData
        );
    }
    postSharePercentAttach(
        masSharePercentAttachData: MasSharePercentAttachData
    ): Observable<ResponseOneIf<MasSharePercentAttachData>> {
        return this.http.post<ResponseOneIf<MasSharePercentAttachData>>(
            `${environment.apiUrl}/master/share-percent-attach`,
            masSharePercentAttachData
        );
    }
    postPersonal(masPersonalData: MasPersonalData): Observable<ResponseOneIf<MasPersonalData>> {
        return this.http.post<ResponseOneIf<MasPersonalData>>(`${environment.apiUrl}/master/personal`, masPersonalData);
    }
    postOccupation(masOccupationData: MasOccupationData): Observable<ResponseOneIf<MasOccupationData>> {
        return this.http.post<ResponseOneIf<MasOccupationData>>(
            `${environment.apiUrl}/master/occupation`,
            masOccupationData
        );
    }
    postOccupationSkill(
        masOccupationSkillData: MasOccupationSkillData
    ): Observable<ResponseOneIf<MasOccupationSkillData>> {
        return this.http.post<ResponseOneIf<MasOccupationSkillData>>(
            `${environment.apiUrl}/master/occupation-skill`,
            masOccupationSkillData
        );
    }
    postOccupationGroup(
        masOccupationGroupData: MasOccupationGroupData
    ): Observable<ResponseOneIf<MasOccupationGroupData>> {
        return this.http.post<ResponseOneIf<MasOccupationGroupData>>(
            `${environment.apiUrl}/master/occupation-group`,
            masOccupationGroupData
        );
    }
    postGeneralSkill(masGeneralSkillData: MasGeneralSkillData): Observable<ResponseOneIf<MasGeneralSkillData>> {
        return this.http.post<ResponseOneIf<MasGeneralSkillData>>(
            `${environment.apiUrl}/master/general-skill`,
            masGeneralSkillData
        );
    }
    findSharePercent(masSharePercentData: MasSharePercentData): Observable<ResponseListIf<MasSharePercentData>> {
        return this.http.post<ResponseListIf<MasSharePercentData>>(
            `${environment.apiUrl}/master/find-share-percent`,
            masSharePercentData
        );
    }
    findSharePercentHistory(
        masSharePercentHistoryData: MasSharePercentHistoryData
    ): Observable<ResponseListIf<MasSharePercentHistoryData>> {
        return this.http.post<ResponseListIf<MasSharePercentHistoryData>>(
            `${environment.apiUrl}/master/find-share-percent-history`,
            masSharePercentHistoryData
        );
    }
    findSharePercentAttach(
        masSharePercentAttachData: MasSharePercentAttachData
    ): Observable<ResponseListIf<MasSharePercentAttachData>> {
        return this.http.post<ResponseListIf<MasSharePercentAttachData>>(
            `${environment.apiUrl}/master/find-share-percent-attach`,
            masSharePercentAttachData
        );
    }
    findPersonal(masPersonalData: MasPersonalData): Observable<ResponseListIf<MasPersonalData>> {
        return this.http.post<ResponseListIf<MasPersonalData>>(
            `${environment.apiUrl}/master/find-personal`,
            masPersonalData
        );
    }
    findOccupation(masOccupationData: MasOccupationData): Observable<ResponseListIf<MasOccupationData>> {
        return this.http.post<ResponseListIf<MasOccupationData>>(
            `${environment.apiUrl}/master/find-occupation`,
            masOccupationData
        );
    }
    findOccupationSkill(
        masOccupationSkillData: MasOccupationSkillData
    ): Observable<ResponseListIf<MasOccupationSkillData>> {
        return this.http.post<ResponseListIf<MasOccupationSkillData>>(
            `${environment.apiUrl}/master/find-occupation-skill`,
            masOccupationSkillData
        );
    }
    findOccupationGroup(
        masOccupationGroupData: MasOccupationGroupData
    ): Observable<ResponseListIf<MasOccupationGroupData>> {
        return this.http.post<ResponseListIf<MasOccupationGroupData>>(
            `${environment.apiUrl}/master/find-occupation-group`,
            masOccupationGroupData
        );
    }
    findGeneralSkill(masGeneralSkillData: MasGeneralSkillData): Observable<ResponseListIf<MasGeneralSkillData>> {
        return this.http.post<ResponseListIf<MasGeneralSkillData>>(
            `${environment.apiUrl}/master/find-general-skill`,
            masGeneralSkillData
        );
    }
    findDepartment(masDepartmentData: MasDepartmentData): Observable<ResponseListIf<MasDepartmentData>> {
        return this.http.post<ResponseListIf<MasDepartmentData>>(
            `${environment.apiUrl}/master/find-department`,
            masDepartmentData
        );
    }
    findCourseType(masCourseTypeData: MasCourseTypeData): Observable<ResponseListIf<MasCourseTypeData>> {
        return this.http.post<ResponseListIf<MasCourseTypeData>>(
            `${environment.apiUrl}/master/find-course-type`,
            masCourseTypeData
        );
    }
    findBank(masBankData: MasBankData): Observable<ResponseListIf<MasBankData>> {
        return this.http.post<ResponseListIf<MasBankData>>(`${environment.apiUrl}/master/find-bank`, masBankData);
    }
    findBankCharge(masBankChargeData: MasBankChargeData): Observable<ResponseListIf<MasBankChargeData>> {
        return this.http.post<ResponseListIf<MasBankChargeData>>(
            `${environment.apiUrl}/master/find-bank-charge`,
            masBankChargeData
        );
    }
    findBankChargeAttach(
        masBankChargeAttachData: MasBankChargeAttachData
    ): Observable<ResponseListIf<MasBankChargeAttachData>> {
        return this.http.post<ResponseListIf<MasBankChargeAttachData>>(
            `${environment.apiUrl}/master/find-bank-charge-attach`,
            masBankChargeAttachData
        );
    }
    findBankBranch(masBankBranchData: MasBankBranchData): Observable<ResponseListIf<MasBankBranchData>> {
        return this.http.post<ResponseListIf<MasBankBranchData>>(
            `${environment.apiUrl}/master/find-bank-branch`,
            masBankBranchData
        );
    }
    findBankAccount(masBankAccountData: MasBankAccountData): Observable<ResponseListIf<MasBankAccountData>> {
        return this.http.post<ResponseListIf<MasBankAccountData>>(
            `${environment.apiUrl}/master/find-bank-account`,
            masBankAccountData
        );
    }
    postDepartment(masDepartmentData: MasDepartmentData): Observable<ResponseOneIf<MasDepartmentData>> {
        return this.http.post<ResponseOneIf<MasDepartmentData>>(
            `${environment.apiUrl}/master/department`,
            masDepartmentData
        );
    }
    postCourseType(masCourseTypeData: MasCourseTypeData): Observable<ResponseOneIf<MasCourseTypeData>> {
        return this.http.post<ResponseOneIf<MasCourseTypeData>>(
            `${environment.apiUrl}/master/course-type`,
            masCourseTypeData
        );
    }
    postBank(masBankData: MasBankData): Observable<ResponseOneIf<MasBankData>> {
        return this.http.post<ResponseOneIf<MasBankData>>(`${environment.apiUrl}/master/bank`, masBankData);
    }
    postBankCharge(masBankChargeData: MasBankChargeData): Observable<ResponseOneIf<MasBankChargeData>> {
        return this.http.post<ResponseOneIf<MasBankChargeData>>(
            `${environment.apiUrl}/master/bank-charge`,
            masBankChargeData
        );
    }
    postBankChargeAttach(
        masBankChargeAttachData: MasBankChargeAttachData
    ): Observable<ResponseOneIf<MasBankChargeAttachData>> {
        return this.http.post<ResponseOneIf<MasBankChargeAttachData>>(
            `${environment.apiUrl}/master/bank-charge-attach`,
            masBankChargeAttachData
        );
    }
    postBankBranch(masBankBranchData: MasBankBranchData): Observable<ResponseOneIf<MasBankBranchData>> {
        return this.http.post<ResponseOneIf<MasBankBranchData>>(
            `${environment.apiUrl}/master/bank-branch`,
            masBankBranchData
        );
    }
    postBankAccount(masBankAccountData: MasBankAccountData): Observable<ResponseOneIf<MasBankAccountData>> {
        return this.http.post<ResponseOneIf<MasBankAccountData>>(
            `${environment.apiUrl}/master/bank-account`,
            masBankAccountData
        );
    }
    checkLogin(): Observable<ResponseOneIf<string>> {
        return this.http.get<ResponseOneIf<string>>(`${environment.apiUrl}/master/check-login`);
    }
    findNotiInfo(criteria): Observable<ResponseListIf<NotiInfoData>> {
        return this.http.post<ResponseListIf<NotiInfoData>>(`${environment.apiUrl}/master/find-noti-info`, criteria);
    }
    updateNotiDetail(criteria): Observable<ResponseListIf<NotiInfoData>> {
        return this.http.put<ResponseListIf<NotiInfoData>>(`${environment.apiUrl}/master/update-noti-detail`, criteria);
    }
    getWebsiteBanner(bannerId: number): Observable<ResponseOneIf<MasWebsiteBannerData>> {
        return this.http.get<ResponseOneIf<MasWebsiteBannerData>>(
            `${environment.apiUrl}/master/website-banner/${bannerId}`
        );
    }
    putWebsiteBanner(
        bannerId: number,
        masWebsiteBannerData: MasWebsiteBannerData
    ): Observable<ResponseOneIf<MasWebsiteBannerData>> {
        return this.http.put<ResponseOneIf<MasWebsiteBannerData>>(
            `${environment.apiUrl}/master/website-banner/${bannerId}`,
            masWebsiteBannerData
        );
    }
    postWebsiteBanner(masWebsiteBannerData: MasWebsiteBannerData): Observable<ResponseOneIf<MasWebsiteBannerData>> {
        return this.http.post<ResponseOneIf<MasWebsiteBannerData>>(
            `${environment.apiUrl}/master/website-banner`,
            masWebsiteBannerData
        );
    }
    findWebsiteBanner(masWebsiteBannerData: MasWebsiteBannerData): Observable<ResponseListIf<MasWebsiteBannerData>> {
        return this.http.post<ResponseListIf<MasWebsiteBannerData>>(
            `${environment.apiUrl}/master/find-website-banner`,
            masWebsiteBannerData
        );
    }
    deleteWebsiteBanner(bannerId: number): Observable<ResponseOneIf<null>> {
        return this.http.delete<ResponseOneIf<null>>(
            `${environment.apiUrl}/master/website-banner/delete/${bannerId}`
        );
    }
}
