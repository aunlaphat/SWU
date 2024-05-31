import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService } from 'primeng/api';
import { CheckboxChangeEvent } from 'primeng/checkbox';
import { DropdownFilterEvent } from 'primeng/dropdown';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { InputSwitchOnChangeEvent } from 'primeng/inputswitch';
import { ToastItemCloseEvent } from 'primeng/toast';
import { DropdownCriteriaData, DropdownData, MODE_PAGE } from 'src/app/models/common';
import { CoursepublicInstructorData } from 'src/app/models/course-management';
import { CourseManagementService } from 'src/app/services/course-management.service';
import { DropdownService } from 'src/app/services/dropdown.service';
import { MasterService } from 'src/app/services/master.service';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { UploadFileService } from 'src/app/services/upload-file.service';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-add-round-instructor',
    templateUrl: './add-round-instructor.component.html',
    styleUrls: ['./add-round-instructor.component.scss']
})
export class AddRoundInstructorComponent implements OnInit{
    @Input() item!: CoursepublicInstructorData;

    @Input() mode: MODE_PAGE;

    @Input() lang: string;

    @Output() backToListPage = new EventEmitter();

    processing: boolean = false;
    personalList: DropdownData[] = [];
    showError: boolean = false;

    constructor(
        public translate: TranslateService,
        private masterService: MasterService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private dropdownService: DropdownService,
        private courseManagementService: CourseManagementService,
        private previewFileSerivce: PreviewFileService,
        private uploadFileService: UploadFileService
    ) {}

    ngOnInit(): void {
        setTimeout(() => {
            this.lazyLoadPersonal(null, this.item.instructorId);
            this.loadImage();
        }, 100);
    }

    userImg: string = 'assets/layout/images/dummy/dummy.svg';

    onAdvancedUpload(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        this.uploadFileService.postPersonal(file)
        .subscribe(({status, message, entries}) => {
            if (status === 200) {
                this.item.filename = entries.filename;
                this.item.prefix = entries.prefix;
                this.item.module = entries.module;
                const extension = entries.filename.split('.')[1]
                this.userImg = `data:image/${extension};base64,${entries.base64}`
            }
            console.log(this.userImg)
        })
      }
      onRemoveUpload(event: FileRemoveEvent, form: any) {
        this.item.filename = null;
        form.clear();
        form.uploadedFileCount = 0;
    }

    loadImage() {
        if (this.item && this.item.filename) {
            this.previewFileSerivce
                .postFile({
                    filename: this.item.filename,
                    prefix: this.item.prefix,
                    module: this.item.module
                })
                .subscribe(({ status, message, entries }) => {
                    if (status === 200) {
                        this.userImg = `data:image/;base64,${entries.base64}`

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
    onSave() {
        this.loaderService.start();
        console.log(this.item);
        this.processing = true;

        this.loaderService.start();
        if (this.item.instructorType === true) {
            if (!!!this.item.externalNameTh || !!!this.item.externalNameEn || !!!this.item.externalEmail) {
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
        } else {
            if (!!!this.item.instructorId) {
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
        }

        if (this.mode === 'EDIT') {
            this.courseManagementService
                .putCoursepublicInstructor(this.item.coursepublicInstructorId, this.item)
                .subscribe(({ status, message, entries }) => {
                    this.loaderService.stop();
                    if (status === 200) {
                        if (this.item.instructorType === null) {
                            this.item.instructorType = false;
                        }
                        this.messageService.add({
                            severity: 'success',
                            summary: this.translate.instant('common.alert.success'),
                            detail: message,
                            life: 2000
                        });
                    } else {
                        this.messageService.add({
                            severity: 'error',
                            summary: this.translate.instant('common.alert.fail'),
                            detail: message,
                            life: 2000
                        });
                    }
                });
        } else if (this.mode === 'CREATE') {
            this.item.instructorType = this.item.instructorType ? this.item.instructorType : false;
            this.item.instructorMain = this.item.instructorMain ? this.item.instructorMain : false;
            if (this.item.instructorType) {
                this.item.instructorMain = false;
            }
            this.courseManagementService
                .postCoursepublicInstructor(this.item)
                .subscribe(({ status, message, entries }) => {
                    this.loaderService.stop();
                    if (status === 200) {
                        this.messageService.add({
                            severity: 'success',
                            summary: this.translate.instant('common.alert.success'),
                            detail: message,
                            life: 2000
                        });
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
    }

    lazyLoadPersonal(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            displayCode: false
        };

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        this.dropdownService.getPersonalDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {

                this.personalList = this.getUniqueListBy([
                    ...this.personalList,
                    ...entries
                ], 'value');

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

    getUniqueListBy(arr: any[], key) {
        return [...new Map(arr.map(item => [item[key], item])).values()]
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

    changeInstructorMain(event: InputSwitchOnChangeEvent) {
        console.log('event :>> ', event);
        if (event.checked) {
            this.item.activeFlag = true;
            this.item.instructorType = false;
            this.item.externalNameTh = null;
            this.item.externalNameEn = null;
            this.item.externalEmail = null;
        } else {
            this.item.activeFlag = true;
            this.item.externalNameTh = null;
            this.item.externalNameEn = null;
            this.item.externalEmail = null;
        }
    }

    onChangeInstructorType(event: CheckboxChangeEvent) {
        if (event.checked) {
            this.item.instructorMain = false;
            this.item.instructorId = null;
        } else {
            this.item.externalNameTh = null;
            this.item.externalNameEn = null;
            this.item.externalEmail = null;
        }
    }

    changePersonal() {
        this.masterService.getPersonal(this.item.instructorId).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                const { prefix, module, personalPhotoPath } = entries;
                if (prefix && module && personalPhotoPath) {
                    this.item.prefix = prefix;
                    this.item.module = module;
                    this.item.filename = personalPhotoPath;
                    this.userImg = `${environment.apiUrl}/publicfile/${prefix}/${module}/${personalPhotoPath}`;
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
