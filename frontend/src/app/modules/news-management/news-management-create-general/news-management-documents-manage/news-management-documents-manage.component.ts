import { Component, DoCheck, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService } from 'primeng/api';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { ToastItemCloseEvent } from 'primeng/toast';
import { MODE_PAGE, MODE_PAGE_CHILDE } from 'src/app/models/common';
import { NewsInfoAttachData } from 'src/app/models/news-management/NewsInfoAttachData';
import { NewsManagementService } from 'src/app/services/news-management.service';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
  selector: 'app-news-management-documents-manage',
  templateUrl: './news-management-documents-manage.component.html',
  styleUrls: ['./news-management-documents-manage.component.scss']
})
export class NewsManagementDocumentsManageComponent implements DoCheck{
  showError: boolean = false;

  @Input() item!: NewsInfoAttachData;

  lang: string;

  @Output() backToMain = new EventEmitter();

  @Input() modeChilde: MODE_PAGE_CHILDE = 'MAIN';

  processing: boolean = false;
  @ViewChild('fileUpload') fileUpload: any;
  initForm: boolean = false;

  constructor(
      public translate: TranslateService,
      private messageService: MessageService,
      private loaderService: NgxUiLoaderService,
      private newsManagementService: NewsManagementService,
      private uploadFileService: UploadFileService
  ) {}
    ngDoCheck(): void {
        if (!this.initForm) {
            this.initForm = !this.initForm;
            this.item
        }
        if (this.lang != localStorage.getItem('lang')) {
            this.lang = localStorage.getItem('lang');
        }
    }

  onSave() {
      this.processing = true;
      this.loaderService.start();

      if (!!!this.item.filename || !!!this.item.fileNameTh || !!!this.item.fileNameEn) {
          this.showError = true;
          this.messageService.add({
              severity: 'warn',
              summary: this.translate.instant('common.alert.fail'),
              detail: this.translate.instant('common.pleaseEnter'),
              life: 2000
          });
          this.loaderService.stop();
          this.processing = false;
          return;
      }

      this.loaderService.start();
    if (this.item.newsAttachId) {
        this.newsManagementService.putNewsInfoAttach(this.item.newsAttachId, this.item).subscribe((result) => {
            this.loaderService.stop();
            if (result.status === 200) {
                this.item = result.entries;

                this.backToMain.emit(this.item);

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
    }else if (this.item.newsId) {
        this.newsManagementService.postNewsInfoAttach(this.item).subscribe((result) => {
            this.loaderService.stop();
            if (result.status === 200) {
                this.item = result.entries;
                this.backToMain.emit(this.item);
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
    } else {
        console.log('else');
        const filename =  this.item.filename;
        const prefix =  this.item.prefix;
        const module =  this.item.module;
        const fileNameEn =  this.item.fileNameEn;
        const fileNameTh =  this.item.fileNameTh;
        this.item = {
            ...this.item,
            filename,
            prefix,
            module,
            fileNameEn,
            fileNameTh
        };
        this.loaderService.stop();
        this.backToMain.emit(this.item);
    }

}
  onBack() {
    this.backToMain.emit();
  }

  onCloseLevel2(event: ToastItemCloseEvent) {
    if (event.message.severity === 'success') {
        this.backToMain.emit();
    }
    this.processing = false;
}
onAdvancedUpload(event: FileUploadHandlerEvent) {
    const file = event.files[0];
    this.uploadFileService.postNews(file)
    .subscribe(({ status, message, entries }) => {
        if (status === 200) {
            console.log('RR')
            this.initForm = true;
            this.item.filename = entries.filename;
            this.item.prefix = entries.prefix;
            this.item.module = entries.module;
            this.item.originalFilename = entries.originalFilename
            this.item.fileNameEn =entries.originalFilename
            this.item.fileNameTh =entries.originalFilename
        }
    })

}
onRemoveUpload(event: FileRemoveEvent, form: any) {
    this.item.filename = null;
    form.clear();
    form.uploadedFileCount = 0;
}



}

