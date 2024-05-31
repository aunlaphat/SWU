import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges, DoCheck } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService, MenuItem } from 'primeng/api';
import { FileRemoveEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { ToastItemCloseEvent } from 'primeng/toast';
import { MODE_PAGE } from 'src/app/models/common';
import { MasWebsiteBannerData } from 'src/app/models/master/masWebsiteBannerData';
import { MasterService } from 'src/app/services/master.service';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { UploadFileService } from 'src/app/services/upload-file.service';

@Component({
    selector: 'app-website-banner-manage',
    templateUrl: './website-banner-manage.component.html',
    styleUrls: ['./website-banner-manage.component.scss']
})
export class WebsiteBannerManageComponent implements OnInit{
    showError: boolean = false;

    isImageSizeValid: boolean = false;

    @Input() item!: MasWebsiteBannerData;
    @Input() mode: MODE_PAGE;
    @Input() lang: string;

    @Output() backToListPage = new EventEmitter();

    processing: boolean = false;
    imgSrcRef: string = 'assets/layout/images/dummy/1680x600.svg';
    imgSrc: string;

    showUpload: boolean = true;

    information: MenuItem;
    banner: MenuItem;

    constructor(
        public translate: TranslateService,
        private masterService: MasterService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private uploadFileService: UploadFileService,
        private previewFileSerivce: PreviewFileService
    ) {
        this.setItems();
    }

    setItems() {
        this.information = {
        label: this.translate.instant('common.module.master'),
        command: () => this.openPage('LIST')
        };

        this.banner = {
        label: this.translate.instant('menu.master.news.websiteBanner'),
        command: () => this.openPage('LIST')
        };
    }

    ngDoCheck() {
        if (this.lang != localStorage.getItem('lang')) {
            this.lang = localStorage.getItem('lang');
            this.setItems();
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['lang']) {
            this.lang = changes['lang'].currentValue;
            this.setItems();
        }
    }

    openPage(page: MODE_PAGE) {
        this.backToListPage.emit('LIST');
    }

    ngOnInit(): void {
        window.scrollTo(0, 0);
        this.loadImage();
    }

    onAdvancedUpload(event: FileUploadHandlerEvent) {
        this.loaderService.start();
        const file = event.files[0];
        this.uploadFileService.postBanner(file)
        .subscribe(({status, message, entries}) => {
            if (status === 200) {
                this.item.filename = entries.filename;
                this.item.prefix = entries.prefix;
                this.item.module = entries.module;
                this.item.bannerImageTh = entries.originalFilename;
                this.item.bannerImageEn = entries.originalFilename;
                const extension = entries.filename.split('.')[1]
                this.imgSrc = `data:image/${extension};base64,${entries.base64}`
                this.showUpload = false;
            }
            console.log(this.imgSrc)
        })
        this.loaderService.stop();
      }

      onRemoveUpload(event: FileRemoveEvent, form: any) {
        this.item.filename = null;
        form.clear();
        form.uploadedFileCount = 0;
    }

    onEditImage(event: FileUploadHandlerEvent) {
        this.loaderService.start();
        if (this.item.filename) {
            setTimeout(() => {
                this.loaderService.stop();
                this.item.filename = null;
                this.showUpload = true;
            }, 500);


        }
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
                        this.imgSrc = `data:image/;base64,${entries.base64}`
                        this.showUpload = false;
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
        this.processing = true;
        this.loaderService.start();
        if (
            !!!this.item.bannerName ||
            !!!this.item.bannerImageEn ||
            !!!this.item.bannerImageTh ||
            !!!this.item.bannerImageEn ||
            !!!this.item.bannerImageTh ||
            !!!this.item.orderBy
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
            this.masterService.postWebsiteBanner(this.item).subscribe((result) => {
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
            this.masterService.putWebsiteBanner(this.item.bannerId, this.item).subscribe((result) => {
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


}
