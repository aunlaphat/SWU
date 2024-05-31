import { DropdownService } from './../../../services/dropdown.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService } from 'primeng/api';
import { TablePageEvent } from 'primeng/table';
import { ToastItemCloseEvent } from 'primeng/toast';
import { DropdownData, LOOKUP_CATALOG } from 'src/app/models/common';
import {
    CoursepublicAttachData,
    CoursepublicInstructorData,
    CoursepublicMainData,
    CoursepublicMediaData
} from 'src/app/models/course-management';
import { CourseManagementService } from 'src/app/services/course-management.service';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { environment } from 'src/environments/environment';

type SAVEMODE = 'REQUEST' | 'APPROVE' | 'SENDBACK';

@Component({
    selector: 'app-round-approval',
    templateUrl: './round-approval.component.html',
    styleUrls: ['./round-approval.component.scss']
})
export class RoundApprovalComponent implements OnInit {
    initForm: boolean = false;
    processing: boolean = false;
    @Input() lang: string;
    @Input() mode: SAVEMODE;

    coursepublicMain: CoursepublicMainData;
    coursepublicInstructorItems: CoursepublicInstructorData[] = [];
    coursepublicInstructorTotalRecords: number = 0;
    coursepublicAttachItems: CoursepublicAttachData[] = [];
    coursepublicAttachTotalRecords: number = 0;
    coursepublicMediaVideoItems: CoursepublicMediaData[] = [];
    coursepublicMediaVideoTotalRecords: number = 0;
    coursepublicMediaThumbnailItems: CoursepublicMediaData[] = [];
    coursepublicMediaThumbnailTotalRecords: number = 0;
    coursepublicMediaIllustrationItems: CoursepublicMediaData[] = [];
    coursepublicMediaIllustrationTotalRecords: number = 0;

    @Output() goBack = new EventEmitter();

    certificateList: DropdownData[] = [];
    enrollmentFormatList: DropdownData[] = [];
    teachingConceptList: DropdownData[] = [];
    registrationCostList: DropdownData[] = [];

    lookupCertificateList: DropdownData[] = [];
    lookupEnrollmentFormatList: DropdownData[] = [];
    lookupTeachingConceptList: DropdownData[] = [];
    lookupRegistrationCostList: DropdownData[] = [];
    lookupDepartmentLevel1List: DropdownData[] = [];
    lookupDepartmentLevel2List: DropdownData[] = [];

    departmentLevel1List: DropdownData[] = [];
    departmentLevel2List: DropdownData[] = [];
    certificateNameTh: string[] = [];
    certificateNameEn: string[] = [];

    constructor(
        private translate: TranslateService,
        private courseManagementService: CourseManagementService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private dropdownService: DropdownService,
        private previewFileSerivce: PreviewFileService
    ) {}

    ngOnInit(): void {
        setTimeout(() => {
            window.scrollTo(0, 0);
            this.loadCoursepublicMain();
            this.loadDropDown();
            this.fetchCoursepublicInstructorData();
            this.fetchCoursepublicAttachData();
            this.fetchCoursepublicMediaVideoData();
            this.fetchCoursepublicMediaThumbnailData();
            this.fetchCoursepublicMediaIllustrationData();
        }, 100);

        setTimeout(() => {
            this.initForm = true;
        }, 200);
    }

    loadCoursepublicMain() {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        this.courseManagementService.getCoursepublicMain(coursepublicId).subscribe(({ status, message, entries }) => {
            this.initForm = true;
            if (status === 200) {

                this.coursepublicMain = entries;
                this.loadAccessLevel();

                this.lookupEnrollmentFormatList = this.enrollmentFormatList.filter(
                    ({ value }) => value === this.coursepublicMain.publicFormat
                );
                this.lookupTeachingConceptList = this.teachingConceptList.filter(
                    ({ value }) => value === this.coursepublicMain.courseFormat
                );
                this.lookupRegistrationCostList = this.registrationCostList.filter(
                    ({ value }) => value === this.coursepublicMain.feeStatus
                );

                if (this.coursepublicMain.certificateFormat && this.coursepublicMain.certificateFormat.length > 0) {
                    this.lookupCertificateList = this.certificateList.filter(({ value }) =>
                        this.coursepublicMain.certificateFormat.includes(value)
                    );
                }

                this.lookupCertificateList.forEach((value) => this.certificateNameTh.push(value.nameTh));
                this.lookupCertificateList.forEach((value) => this.certificateNameEn.push(value.nameEn));
            }
        });
    }

    fetchCoursepublicInstructorData(event?: TablePageEvent) {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        const criteria: CoursepublicInstructorData = {
            coursepublicId: coursepublicId,
            first: 0,
            size: 5,
            activeFlag: true
        };

        if (event) {
            criteria.size = event.rows;
            criteria.first = event.first;
        }

        this.courseManagementService
            .findCoursepublicInstructor(criteria)
            .subscribe(({ status, message, entries, totalRecords }) => {
                if (status === 200) {
                    this.initForm = true;
                    this.coursepublicInstructorItems = entries;
                    this.coursepublicInstructorTotalRecords = totalRecords;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    fetchCoursepublicAttachData(event?: TablePageEvent) {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        const criteria: CoursepublicAttachData = {
            coursepublicId: coursepublicId,
            first: 0,
            size: 5,
            activeFlag: true
        };

        if (event) {
            criteria.size = event.rows;
            criteria.first = event.first;
        }

        this.courseManagementService
            .findCoursepublicAttach(criteria)
            .subscribe(({ status, message, entries, totalRecords }) => {
                if (status === 200) {
                    this.initForm = true;
                    this.coursepublicAttachItems = entries;
                    this.coursepublicAttachTotalRecords = totalRecords;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    fetchCoursepublicMediaVideoData(event?: TablePageEvent) {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        const criteria: CoursepublicMediaData = {
            coursepublicId: coursepublicId,
            mediaType: 30012003,
            filename: null,
            first: 0,
            size: 5,
            activeFlag: true
        };

        if (event) {
            criteria.size = event.rows;
            criteria.first = event.first;
        }

        this.courseManagementService
            .findCoursepublicMedia(criteria)
            .subscribe(({ status, message, entries, totalRecords }) => {
                if (status === 200) {
                    this.initForm = true;
                    this.coursepublicMediaVideoItems = entries.map(o => {
                        if (o.filename) {
                            const fullpath = `${environment.apiUrl}/publicfile/${o.prefix}/${o.module}/${o.filename}`;
                            const videoType = `video/${o.filename.split('.')[1]}`;
                            return {
                                ...o,
                                fullpath,
                                videoType
                            }
                        } else {
                            return o;
                        }
                    });
                    this.coursepublicMediaVideoTotalRecords = totalRecords;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    fetchCoursepublicMediaThumbnailData(event?: TablePageEvent) {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        const criteria: CoursepublicMediaData = {
            coursepublicId: coursepublicId,
            mediaType: 30012001,
            filename: null,
            first: 0,
            size: 5,
            activeFlag: true
        };

        if (event) {
            criteria.size = event.rows;
            criteria.first = event.first;
        }

        this.courseManagementService
            .findCoursepublicMedia(criteria)
            .subscribe(({ status, message, entries, totalRecords }) => {
                if (status === 200) {
                    this.initForm = true;
                    this.coursepublicMediaThumbnailItems = entries.map(o => {
                        if (o.filename) {
                            const fullpath = `${environment.apiUrl}/publicfile/${o.prefix}/${o.module}/${o.filename}`;
                            return {
                                ...o,
                                fullpath
                            }
                        } else {
                            return o;
                        }
                    });
                    this.coursepublicMediaThumbnailTotalRecords = totalRecords;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    fetchCoursepublicMediaIllustrationData(event?: TablePageEvent) {
        const coursepublicId = Number(localStorage.getItem('coursepublic'));
        const criteria: CoursepublicMediaData = {
            coursepublicId: coursepublicId,
            mediaType: 30012002,
            first: 0,
            size: 5,
            activeFlag: true
        };

        if (event) {
            criteria.size = event.rows;
            criteria.first = event.first;
        }

        this.courseManagementService
            .findCoursepublicMedia(criteria)
            .subscribe(({ status, message, entries, totalRecords }) => {
                if (status === 200) {
                    this.initForm = true;
                    this.coursepublicMediaIllustrationItems = entries.map(o => {
                        if (o.filename) {
                            const fullpath = `${environment.apiUrl}/publicfile/${o.prefix}/${o.module}/${o.filename}`;
                            return {
                                ...o,
                                fullpath
                            }
                        } else {
                            return o;
                        }
                    });
                    this.coursepublicMediaIllustrationTotalRecords = totalRecords;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    loadDropDown() {
        this.dropdownService
            .getLookup({
                displayCode: true,
                id: LOOKUP_CATALOG.CERTIFICATE_ISSUANCE_FORMAT
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.certificateList = entries;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });

        this.dropdownService
            .getLookup({
                displayCode: true,
                id: LOOKUP_CATALOG.COURSE_ENROLLMENT_FORMAT
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.enrollmentFormatList = entries;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });

        this.dropdownService
            .getLookup({
                displayCode: true,
                id: LOOKUP_CATALOG.TEACHING_CONCEPT
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.teachingConceptList = entries;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });

        this.dropdownService
            .getLookup({
                displayCode: true,
                id: LOOKUP_CATALOG.REGISTRATION_COST_08
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.registrationCostList = entries;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });
    }

    loadAccessLevel() {

        this.dropdownService
            .getDepartmentDropdown({
                depType: 30009001,
                pkId: this.coursepublicMain.depIdLevel1
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.departmentLevel1List = entries;

                    this.lookupDepartmentLevel1List = this.departmentLevel1List.filter(
                        ({ value }) => value === this.coursepublicMain.depIdLevel1
                    );
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });

        this.dropdownService
            .getDepartmentDropdown({
                depType: 30009002,
                pkId: this.coursepublicMain.depIdLevel2
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.departmentLevel2List = entries;

                    this.lookupDepartmentLevel2List = this.departmentLevel2List.filter(
                        ({ value }) => value === this.coursepublicMain.depIdLevel2
                    );

                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });
    }
    onClose(event: ToastItemCloseEvent) {
        if (    event.message.severity === 'success'
             || event.message.severity === 'warn'
             || event.message.severity === 'error'
           ) {
            this.processing = false;
        }
    }
    onBack() {
        this.goBack.emit('LIST');
    }
    onSave(saveMode: SAVEMODE) {
        this.processing = true;
        this.loaderService.start();
        if (saveMode === 'APPROVE') {
            /** เปิดรอบหลักสูตร */
            this.coursepublicMain.coursepublicStatus = 30014003;
        } else if (saveMode === 'REQUEST') {
            /** รอศูนย์บริการอนุมัติ */
            this.coursepublicMain.coursepublicStatus = 30014002;
        } else if (saveMode === 'SENDBACK') {
            /** รอเปิดหลักสูตร */
            this.coursepublicMain.coursepublicStatus = 30014008;
        }
        this.courseManagementService
            .putCoursepublicMainStatus(this.coursepublicMain.coursepublicId, this.coursepublicMain)
            .subscribe(({ status, message, entries }) => {
                this.loaderService.stop();
                if (status === 200) {
                    this.messageService.add({
                        severity: 'success',
                        summary: this.translate.instant('common.alert.success'),
                        detail: this.translate.instant('common.alert.textSuccess'),
                        life: 2000
                    });
                    setTimeout(() => {
                        this.onBack();
                    }, 1000);
                } else if (status === 204) {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

    openCourse() {
        const { courseId } = this.coursepublicMain;
        const origin = window.location.origin;
        const id = {
            coursepublicId: null,
            courseId: courseId
        };
        const enc = window.btoa(JSON.stringify(id));
        window.open(`${origin}/course-management/course-preview?data=${enc}`, '_blank');
    }

    previewPdf(event: any): void {
        this.loaderService.start();
        this.previewFileSerivce
            .postFile({
                filename: event.filename,
                prefix: event.prefix,
                module: event.module
            })
            .subscribe(({ status, message, entries }) => {
                this.loaderService.stop();
                if (status === 200) {
                    const base64ToArrayBuffer = (data) => {
                        const bString = window.atob(data);
                        const bLength = bString.length;
                        const bytes = new Uint8Array(bLength);
                        for (let i = 0; i < bLength; i++) {
                            bytes[i] = bString.charCodeAt(i);
                        }
                        return bytes;
                    };
                    // const filename = this.lang === 'th' ? event.fileNameTh : event.fileNameEn
                    const bufferArray = base64ToArrayBuffer(entries.base64);
                    const blobStore = new Blob([bufferArray], { type: 'application/pdf' });
                    const data = window.URL.createObjectURL(blobStore);
                    window.open(data, '_blank');
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(message),
                        life: 2000
                    });
                }
            });
    }

}
