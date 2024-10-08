/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ResponseListIf, ResponseOneIf } from 'src/app/models/common';
import type { AutRoleData, AutUserData, MemberAddressData, MemberInfoData } from 'src/app/models/user-management';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class UserManagementService {
    constructor(private http: HttpClient) {}

    getUser(userId: number): Observable<ResponseOneIf<AutUserData>> {
        return this.http.get<ResponseOneIf<AutUserData>>(`${environment.apiUrl}/user-management/user/${userId}`);
    }
    putUser(userId: number, autUserData: AutUserData): Observable<ResponseOneIf<AutUserData>> {
        return this.http.put<ResponseOneIf<AutUserData>>(
            `${environment.apiUrl}/user-management/user/${userId}`,
            autUserData
        );
    }
    getRole(roleId: number | null): Observable<ResponseOneIf<AutRoleData>> {
        if (roleId) {
            return this.http.get<ResponseOneIf<AutRoleData>>(`${environment.apiUrl}/user-management/role/${roleId}`);
        } else {
            return this.http.get<ResponseOneIf<AutRoleData>>(`${environment.apiUrl}/user-management/role`);
        }
    }
    putRole(roleId: number, autRoleData: AutRoleData): Observable<ResponseOneIf<AutRoleData>> {
        return this.http.put<ResponseOneIf<AutRoleData>>(
            `${environment.apiUrl}/user-management/role/${roleId}`,
            autRoleData
        );
    }
    getMemberInfo(memberId: number): Observable<ResponseOneIf<MemberInfoData>> {
        return this.http.get<ResponseOneIf<MemberInfoData>>(
            `${environment.apiUrl}/user-management/member-info/${memberId}`
        );
    }
    putMemberInfo(memberId: number, memberInfoData: MemberInfoData): Observable<ResponseOneIf<MemberInfoData>> {
        return this.http.put<ResponseOneIf<MemberInfoData>>(
            `${environment.apiUrl}/user-management/member-info/${memberId}`,
            memberInfoData
        );
    }
    getMemberAddress(memberAddressId: number): Observable<ResponseOneIf<MemberAddressData>> {
        return this.http.get<ResponseOneIf<MemberAddressData>>(
            `${environment.apiUrl}/user-management/member-address/${memberAddressId}`
        );
    }
    putMemberAddress(
        memberAddressId: number,
        memberAddressData: MemberAddressData
    ): Observable<ResponseOneIf<MemberAddressData>> {
        return this.http.put<ResponseOneIf<MemberAddressData>>(
            `${environment.apiUrl}/user-management/member-address/${memberAddressId}`,
            memberAddressData
        );
    }
    postUser(autUserData: AutUserData): Observable<ResponseOneIf<AutUserData>> {
        return this.http.post<ResponseOneIf<AutUserData>>(`${environment.apiUrl}/user-management/user`, autUserData);
    }
    postRole(autRoleData: AutRoleData): Observable<ResponseOneIf<AutRoleData>> {
        return this.http.post<ResponseOneIf<AutRoleData>>(`${environment.apiUrl}/user-management/role`, autRoleData);
    }
    postMemberInfo(memberInfoData: MemberInfoData): Observable<ResponseOneIf<MemberInfoData>> {
        return this.http.post<ResponseOneIf<MemberInfoData>>(
            `${environment.apiUrl}/user-management/member-info`,
            memberInfoData
        );
    }
    postMemberAddress(memberAddressData: MemberAddressData): Observable<ResponseOneIf<MemberAddressData>> {
        return this.http.post<ResponseOneIf<MemberAddressData>>(
            `${environment.apiUrl}/user-management/member-address`,
            memberAddressData
        );
    }
    findUser(autUserData: AutUserData): Observable<ResponseListIf<AutUserData>> {
        return this.http.post<ResponseListIf<AutUserData>>(
            `${environment.apiUrl}/user-management/find-user`,
            autUserData
        );
    }
    findRole(autRoleData: AutRoleData): Observable<ResponseListIf<AutRoleData>> {
        return this.http.post<ResponseListIf<AutRoleData>>(
            `${environment.apiUrl}/user-management/find-role`,
            autRoleData
        );
    }
    findMemberInfo(memberInfoData: MemberInfoData): Observable<ResponseListIf<MemberInfoData>> {
        return this.http.post<ResponseListIf<MemberInfoData>>(
            `${environment.apiUrl}/user-management/find-member-info`,
            memberInfoData
        );
    }
    findMemberAddress(memberAddressData: MemberAddressData): Observable<ResponseListIf<MemberAddressData>> {
        return this.http.post<ResponseListIf<MemberAddressData>>(
            `${environment.apiUrl}/user-management/find-member-address`,
            memberAddressData
        );
    }
}
