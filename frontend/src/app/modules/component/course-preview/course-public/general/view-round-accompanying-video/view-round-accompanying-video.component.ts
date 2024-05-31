import { Component, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MessageService } from 'primeng/api';
import { TablePageEvent } from 'primeng/table';
import { CoursepublicMainData, CoursepublicMediaData } from 'src/app/models/course-management';
import { CourseManagementService } from 'src/app/services/course-management.service';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';

@Component({
    selector: 'app-view-round-accompanying-video',
    templateUrl: './view-round-accompanying-video.component.html',
    styleUrls: ['./view-round-accompanying-video.component.scss']
})
export class ViewRoundAccompanyingVideoComponent implements OnInit {
    @Input() lang: string;
    @Input() coursepublicMain!: CoursepublicMainData;

    initForm: boolean = false;

    coursepublicMedia: CoursepublicMediaData;

    items: CoursepublicMediaData[] = [];
    totalRecords: number = 0;

    constructor(
        public translate: TranslateService,
        private courseManagementService: CourseManagementService,
        private messageService: MessageService,
        private previewFileSerivce: PreviewFileService,
        private loaderService: NgxUiLoaderService,
    ) {}

    ngOnInit(): void {
        this.fetchData();
    }

    fetchData(event?: TablePageEvent) {
        this.loaderService.start();
        const criteria: CoursepublicMediaData = {
            coursepublicId: this.coursepublicMain.coursepublicId,
            mediaType: 30012003,
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
                this.loaderService.stop();
                if (status === 200) {
                    this.initForm = true;
                    this.items = entries;
                    this.totalRecords = totalRecords;
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

    previewVideo(event: CoursepublicMediaData): void {
        console.log('event :>> ', event);
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

                    const blobType = (event.filename.toLowerCase().endsWith('.avi') ||
                                      event.filename.toLowerCase().endsWith('.mp4') ||
                                      event.filename.toLowerCase().endsWith('.m4v') ||
                                      event.filename.toLowerCase().endsWith('.mov') ||
                                      event.filename.toLowerCase().endsWith('.mpg') ||
                                      event.filename.toLowerCase().endsWith('.mpeg') ||
                                      event.filename.toLowerCase().endsWith('.wmv')) ? 'video/mp4' : '';

                    if (blobType) {
                        const bufferArray = base64ToArrayBuffer(entries.base64);
                        const blobStore = new Blob([bufferArray], { type: blobType });
                        const data = window.URL.createObjectURL(blobStore);
                        window.open(data, '_blank');
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: this.translate.instant('common.alert.fail'),
                            detail: this.translate.instant('Invalid video format'),
                            life: 2000
                        });
                    }

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
