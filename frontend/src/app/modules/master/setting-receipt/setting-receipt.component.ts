import { Component, DoCheck, OnInit, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService } from 'primeng/api';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { ToastItemCloseEvent } from 'primeng/toast';
import { MODE_PAGE } from 'src/app/models/common';
import { FinanceReceiptConfigData } from 'src/app/models/financial-management';
import { FinancialManagementService } from 'src/app/services/financial-management.service';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
    selector: 'app-setting-receipt',
    templateUrl: './setting-receipt.component.html',
    styleUrls: ['./setting-receipt.component.scss']
})
export class SettingReceiptComponent implements OnInit, DoCheck {
    lang: string = 'th';
    showError: boolean = false;
    processing: boolean = false;

    mode: MODE_PAGE = 'EDIT';

    @ViewChild('fileUpload') fileUpload: any;

    item: FinanceReceiptConfigData = {
        receiptConfigId: null,
        logoPath: null,
        depTaxId: null,
        depNameTh: null,
        depNameEn: null,
        depAddressTh: null,
        depAddressEn: null,
        receiptPrefix: null,
        receiptNoteTh: null,
        receiptNoteEn: null,
        receiptRemark: null,
        staffName: null,
        staffPosition: null,
        staffSignaturePath: null,
        activeFlag: true
    };

    constructor(
        private messageService: MessageService,
        private translate: TranslateService,
        private loaderService: NgxUiLoaderService,
        private financialManagementService: FinancialManagementService,
        private uploadFileService: UploadFileService,
        private previewFileSerivce: PreviewFileService
    ) {}

    ngDoCheck(): void {
        if (this.lang != localStorage.getItem('lang')) {
            this.lang = localStorage.getItem('lang');
        }
    }

    ngOnInit(): void {
        this.loadModel();
        this.processing = false;
    }

    loadModel() {
        this.loaderService.start();
        this.financialManagementService.getFinanceReceiptConfig().subscribe(({ status, message, entries }) => {
            this.loaderService.stop();
            if (status === 200) {
                this.item = entries;
                this.mode = 'EDIT';
            } else {
                this.mode = 'CREATE';
            }
        });
    }

    onSave() {
        this.processing = true;
        console.log('save');
        this.loaderService.start();

        if (
            !!!this.item.logoPath ||
            !!!this.item.staffSignaturePath ||
            !!!this.item.depTaxId ||
            !!!this.item.depNameTh ||
            !!!this.item.depNameEn ||
            !!!this.item.depAddressTh ||
            !!!this.item.depAddressEn ||
            !!!this.item.staffName ||
            !!!this.item.staffPosition
        ) {
            this.showError = true;
            this.messageService.add({
                severity: 'warn',
                summary: this.translate.instant('common.alert.fail'),
                detail: this.translate.instant('common.pleaseEnter'),
                life: 2000
            });
            // this.processing = false;
            this.loaderService.stop();
            return;
        }

        if (this.mode == 'EDIT') {
            this.financialManagementService
                .putFinanceReceiptConfig(this.item.receiptConfigId, this.item)
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
        } else {
            this.financialManagementService
                .postFinanceReceiptConfig(this.item)
                .subscribe(({ status, message, entries }) => {
                    this.loaderService.stop();
                    if (status === 200) {
                        this.item = entries;
                        this.messageService.add({
                            severity: 'success',
                            summary: this.translate.instant('common.alert.success'),
                            detail: message,
                            life: 2000
                        });
                    }
                });
        }
    }

    onClose(event: ToastItemCloseEvent) {
        if (    event.message.severity === 'success'
             || event.message.severity === 'warn'
             || event.message.severity === 'error'
           ) {
            this.processing = false;
        }
    }

    onAdvancedUploadLogo(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        this.uploadFileService.postCourse(file)
        .subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.item.logoPath = entries.fullpath;
            }
        })
    }

    userImg: string = 'assets/layout/images/dummy/dummy.svg';

    onAdvancedUploadSign(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        this.uploadFileService.postCourse(file)
        .subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.item.staffSignaturePath = entries.fullpath

                this.userImg = `data:image/${entries.extension};base64,${entries.base64}`
            }
        })
    }
    onRemoveUploadLogo(event: FileRemoveEvent, form: any) {
        this.item.logoPath = null;
        form.clear();
        form.uploadedFileCount = 0;
    }
    onRemoveUploadSign(event: FileRemoveEvent, form: any) {
        this.item.staffSignaturePath = null;
        form.clear();
        form.uploadedFileCount = 0;
    }

    loadImage() {
        if (this.item && this.item.logoPath) {
            this.previewFileSerivce
                .postFile({
                    filename: this.item.logoPath,
                    // prefix: this.item.prefix,
                    // module: this.item.module
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
}
