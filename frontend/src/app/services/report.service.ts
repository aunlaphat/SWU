/**
 * Generated by orval v6.23.0 🍺
 * Do not edit manually.
 * OpenAPI definition
 * OpenAPI spec version: v0
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, concatMap } from 'rxjs';
import { ResponseListIf } from '../models/common';
import type {
    ReportCanceledCourseData,
    ReportCourseEnrollmentData,
    ReportCoursePaymentData,
    ReportDepartmentIncomeData,
    ReportEnrollmentAndPaymentData,
    ReportEnrollmentListData,
    ReportOfferedCourseData
} from '../models/report';
import { environment } from 'src/environments/environment';
import { CoursepublicMainData } from '../models/course-management';
import { FinancePaymentData } from '../models/financial-management';
@Injectable({ providedIn: 'root' })
export class ReportService {
    constructor(private http: HttpClient) {}

    findOfferedCourseReport(
        reportOfferedCourseData: ReportOfferedCourseData
    ): Observable<ResponseListIf<ReportOfferedCourseData>> {
        return this.http.post<ResponseListIf<ReportOfferedCourseData>>(`${environment.apiUrl}/report/find-offered-course-report`, reportOfferedCourseData);
    }
    findEnrollmentListReport(
        reportEnrollmentListData: ReportEnrollmentListData
    ): Observable<ResponseListIf<ReportEnrollmentListData>> {
        return this.http.post<ResponseListIf<ReportEnrollmentListData>>(`${environment.apiUrl}/report/find-enrollment-list-report`, reportEnrollmentListData);
    }
    findEnrollmentAndPaymentReport(
        reportEnrollmentAndPaymentData: ReportEnrollmentAndPaymentData
    ): Observable<ResponseListIf<ReportEnrollmentAndPaymentData>> {
        return this.http.post<ResponseListIf<ReportEnrollmentAndPaymentData>>(
            `${environment.apiUrl}/report/find-enrollment-and-payment-report`,
            reportEnrollmentAndPaymentData
        );
    }
    findDepartmentIncomeReport(
        reportDepartmentIncomeData: ReportDepartmentIncomeData
    ): Observable<ResponseListIf<ReportDepartmentIncomeData>> {
        return this.http.post<ResponseListIf<ReportDepartmentIncomeData>>(`${environment.apiUrl}/report/find-department-income-report`, reportDepartmentIncomeData);
    }
    findCoursePaymentReport(
        reportCoursePaymentData: ReportCoursePaymentData
    ): Observable<ResponseListIf<ReportCoursePaymentData>> {
        return this.http.post<ResponseListIf<ReportCoursePaymentData>>(`${environment.apiUrl}/report/find-course-payment-report`, reportCoursePaymentData);
    }
    findCourseEnrollmentReport(
        reportCourseEnrollmentData: ReportCourseEnrollmentData
    ): Observable<ResponseListIf<ReportCourseEnrollmentData>> {
        return this.http.post<ResponseListIf<ReportCourseEnrollmentData>>(`${environment.apiUrl}/report/find-course-enrollment-report`, reportCourseEnrollmentData);
    }
    findCanceledCourseReport(
        reportCanceledCourseData: ReportCanceledCourseData
    ): Observable<ResponseListIf<ReportCanceledCourseData>> {
        return this.http.post<ResponseListIf<ReportCanceledCourseData>>(`${environment.apiUrl}/report/find-canceled-course-report`, reportCanceledCourseData);
    }
    findGradeExportList(
        coursepublicMainData: CoursepublicMainData
    ): Observable<ResponseListIf<CoursepublicMainData>> {
        return this.http.post<ResponseListIf<CoursepublicMainData>>(`${environment.apiUrl}/report/find-course-grade-result-export`, coursepublicMainData);
    }
    findPaymentDataExportList(
        criteria: FinancePaymentData
    ): Observable<ResponseListIf<FinancePaymentData>> {
        return this.http.post<ResponseListIf<FinancePaymentData>>(`${environment.apiUrl}/report/find-payment-data-export-list`, criteria);
    }
    downloaod(fullpathname: string) {
        return this.http
          .get(`${environment.apiUrl}/preview/path`, {
            params: { fullpathname: fullpathname },
            observe: 'response',
            responseType: 'blob'
          })
          .pipe(
            concatMap(async (response) => {
                console.log(" Response file .... ");
              let errorFormatJson = false;
              try {
                const textRes = await response.body?.text();
                if (textRes && 'status' in JSON.parse(textRes)) {
                  errorFormatJson = true;
                  throw JSON.parse(textRes);
                }
              } catch (error) {
                if (errorFormatJson) {
                  throw error;
                }
              }

              
    
              return response;
            })
          );
      }
}
