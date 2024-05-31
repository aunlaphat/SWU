import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService } from 'primeng/api';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { ToastItemCloseEvent } from 'primeng/toast';
import { MODE_PAGE } from 'src/app/models/common';
import { CourseAttachData } from 'src/app/models/course-management';
import { CourseManagementService } from 'src/app/services/course-management.service';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
  selector: 'app-documents-and-more-manage',
  templateUrl: './documents-and-more-manage.component.html',
  styleUrls: ['./documents-and-more-manage.component.scss']
})
export class DocumentsAndMoreManageComponent {
    showError: boolean = false;
    showErrorFile: boolean = false;
    fileSizeExceeded: boolean = false;
    
    @Input() item!: CourseAttachData;

    @Input() mode: MODE_PAGE;

    @Input() lang: string;

    @Output() backToListPage = new EventEmitter();

    processing: boolean = false;
    imgSrc: string;

    @ViewChild('fileUpload') fileUpload: any;

    constructor(
        public translate: TranslateService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private courseManagementService: CourseManagementService,
        private uploadFileService: UploadFileService
    ) {}

    onSave() {
        this.processing = true;
        this.loaderService.start();
        if (!!!this.item.fileNameTh || !!!this.item.fileNameEn || !!!this.item.filename) {
            this.showError = true;
               this.messageService.add({ 
                    severity: 'warn', 
                    summary: this.translate.instant('common.alert.fail'), 
                    detail: this.translate.instant('common.pleaseEnter') ,
                    life: 2000});
            this.loaderService.stop();
            return;
        }
        
        const fileSize = this.fileUpload.files[0].size;
        if(fileSize > 20000000) {
            this.fileSizeExceeded = true;
          //  this.showErrorFile = true;
            this.messageService.add({ 
                severity: 'warn', 
                summary: this.translate.instant('common.alert.fail'), 
                detail: this.translate.instant('ไฟล์มีขนาดเกิน 20MB'), 
                life: 2000 
            });
            this.loaderService.stop();
            return;
        }
    
        if (this.mode === 'CREATE') {
            this.courseManagementService.postCourseAttach(this.item).subscribe((result) => {
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
            this.courseManagementService.putCourseAttach(this.item.courseAttachId, this.item).subscribe((result) => {
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

    onBack() {
        this.backToListPage.emit('LIST');
    }

    onClose(event: ToastItemCloseEvent) {
        if (event.message.severity === 'success') {
            this.backToListPage.emit('LIST');
        }
        this.processing = false;
    }

    onAdvancedUpload(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        const fileSize = event.files[0].size;
    
        if (fileSize > 20000000) {
            this.fileSizeExceeded = true;
            this.showErrorFile = true; // แสดงข้อความเตือน
           
            this.messageService.add({
                severity: 'warn',
                summary: this.translate.instant('common.alert.fail'),
                detail: this.translate.instant('ไฟล์มีขนาดเกิน 20MB'),
                life: 2000
            });
        } else {
            this.fileSizeExceeded = false;
        }

        this.uploadFileService.postCourse(file)
        .subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.item.filename = entries.filename;
                this.item.prefix = entries.prefix;
                this.item.module = entries.module;
            }
        })
    }

    onRemoveUpload(event: FileRemoveEvent, form: any) {
        this.item.filename = null;
        form.clear();
        form.uploadedFileCount = 0;
        this.showError = true;
        this.showErrorFile = false;
    }

}
