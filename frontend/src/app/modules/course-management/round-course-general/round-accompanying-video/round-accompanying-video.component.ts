import { MessageService } from 'primeng/api';
import { CourseManagementService } from 'src/app/services/course-management.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CoursepublicMainData, CoursepublicMediaData } from 'src/app/models/course-management';
import { MODE_PAGE } from 'src/app/models/common';
import { TranslateService } from '@ngx-translate/core';
import { TablePageEvent } from 'primeng/table';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { UploadFileService } from 'src/app/services/upload-file.service';
import { environment } from 'src/environments/environment';
import { ToastItemCloseEvent } from 'primeng/toast';

@Component({
    selector: 'app-round-accompanying-video',
    templateUrl: './round-accompanying-video.component.html',
    styleUrls: ['./round-accompanying-video.component.scss']
})
export class RoundAccompanyingVideoComponent {
    initForm: boolean = false;
    showError: boolean = false;

    @Input() coursepublicMain!: CoursepublicMainData;
    @Input() lang: string;

    @Output() afterSaveCourseMain = new EventEmitter();
    @Output() backToList = new EventEmitter();

    mode: MODE_PAGE = 'CREATE';

    items: CoursepublicMainData[] = [];
    totalRecords: number = 0;

    processing: boolean = false;
    showUpload: boolean = true;

    coursepublicMedia: CoursepublicMediaData;

    videoUrl: string = '';
    videoType: string = '';

    constructor(
        public translate: TranslateService,
        private loaderService: NgxUiLoaderService,
        private courseManagementService: CourseManagementService,
        private messageService: MessageService,
        private uploadFileService: UploadFileService
    ) {
        this.initForm = true;
    }
    ngOnInit(): void {
        this.initialForm();
    }
    initialForm() {
        this.coursepublicMedia = {
            activeFlag: true,
            coursepublicId: this.coursepublicMain.coursepublicId,
            /** คลิปวิดีโอ */
            mediaType: 30012003,
            coursepublicMediaId: null,
            filename: null,
            prefix: null,
            module: null,
            mediaNameEn: null,
            mediaNameTh: null
        };

        this.loaderService.start();
        this.courseManagementService
            .findCoursepublicMedia({
                coursepublicId: this.coursepublicMain.coursepublicId,
                /** คลิปวิดีโอ */
                mediaType: 30012003,
                filename: null,
                activeFlag: true
            })
            .subscribe(({ status, message, entries }) => {
                this.loaderService.stop();
                if (status === 200) {
                    if (entries.length > 0) {
                        this.coursepublicMedia = entries[0];
                        if (this.coursepublicMedia.filename) {
                            this.showUpload = false;
                        }

                        const { prefix, module, filename } = this.coursepublicMedia;
                        if ( prefix && module && filename ) {
                            this.videoUrl = `${environment.apiUrl}/publicfile/${prefix}/${module}/${filename}`;
                            this.videoType = `video/${filename.split('.')[1]}`;
                        }

                        this.mode = 'EDIT';
                    }
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

    onSave() {
        this.processing = true;
        this.loaderService.start();
        if (
            // !!!this.coursepublicMedia.mediaLink ||
            !!!this.coursepublicMedia.mediaNameEn ||
            !!!this.coursepublicMedia.mediaNameTh
        ) {
            this.showError = true;
            this.messageService.add({
                severity: 'warn',
                summary: this.translate.instant('common.alert.fail'),
                detail: this.translate.instant('common.pleaseEnter'),
                life: 2000
            });
            this.loaderService.stop();
            return;
        }

        if (this.mode === 'CREATE') {
            this.courseManagementService.postCoursepublicMedia(this.coursepublicMedia).subscribe((result) => {
                this.loaderService.stop();
                if (result.status === 200) {
                    this.messageService.add({
                        severity: 'success',
                        summary: this.translate.instant('common.alert.success'),
                        detail: this.translate.instant('common.alert.textSuccess'),
                        life: 2000
                    });
                } else if (result.status === 204) {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(result.message),
                        life: 2000
                    });
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: this.translate.instant(result.message),
                        life: 2000
                    });
                }
            });
        } else if (this.mode === 'EDIT') {
            this.courseManagementService
                .putCoursepublicMedia(this.coursepublicMedia.coursepublicMediaId, this.coursepublicMedia)
                .subscribe((result) => {
                    this.loaderService.stop();
                    if (result.status === 200) {
                        this.messageService.add({
                            severity: 'success',
                            summary: this.translate.instant('common.alert.success'),
                            detail: this.translate.instant('common.alert.textSuccess'),
                            life: 2000
                        });
                    } else if (result.status === 204) {
                        this.messageService.add({
                            severity: 'error',
                            summary: this.translate.instant('common.alert.fail'),
                            detail: this.translate.instant(result.message),
                            life: 2000
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: this.translate.instant('common.alert.fail'),
                            detail: result.message,
                            life: 2000
                        });
                    }
                });
        } else {
            console.log('else');
        }
    }

    onAdvancedUpload(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        this.uploadFileService.postCoursepublic(file).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                const { prefix, module, filename } = entries;
                this.coursepublicMedia.filename = filename;
                this.coursepublicMedia.prefix = prefix;
                this.coursepublicMedia.module = module;
                this.videoUrl = `${environment.apiUrl}/publicfile/${prefix}/${module}/${filename}`;
                this.videoType = `video/${filename.split('.')[1]}`;
                this.showUpload = false;
            }
        });
    }
    onRemoveUpload(event: FileRemoveEvent, form: any) {
        this.coursepublicMedia.filename = null;
        form.clear();
        form.uploadedFileCount = 0;
    }
    onPlayerReady(event?: any) {
        console.log('event :>> ', event);
    }

    onEditVideo() {
        this.showUpload = true;
    }

    onBack() {
        this.backToList.emit();
    }

    onClose(event: ToastItemCloseEvent) {
        this.processing = false;
    }
}
