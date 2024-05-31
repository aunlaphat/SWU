import { UploadFileService } from 'src/app/services/upload-file.service';
import { tap } from 'rxjs';
import { PreviewFileService } from 'src/app/services/preview-file.service';
import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MessageService, MenuItem } from 'primeng/api';
import { DropdownChangeEvent, DropdownFilterEvent } from 'primeng/dropdown';
import { FileRemoveEvent, FileUpload, FileUploadEvent, FileUploadHandlerEvent } from 'primeng/fileupload';
import { ToastItemCloseEvent } from 'primeng/toast';
import { DropdownCriteriaData, DropdownData, DropdownTambonData, LOOKUP_CATALOG, MODE_PAGE } from 'src/app/models/common';
import { MemberInfoData } from 'src/app/models/user-management';
import { DropdownService } from 'src/app/services/dropdown.service';
import { UserManagementService } from 'src/app/services/user-management.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
    selector: 'app-member-manage',
    templateUrl: './member-manage.component.html',
    styleUrls: ['./member-manage.component.scss']
})
export class MemberManageComponent implements OnInit, AfterViewInit {
    @Input() item!: MemberInfoData;

    @Input() mode: MODE_PAGE = 'EDIT';

    @Input() lang: string = 'th';
    accessLevelList: DropdownData[] = [];
    roleList: DropdownData[] = [];
    @Output() backToListPage = new EventEmitter();

    initForm: boolean = false;
    processing: boolean = false;

    genderList: DropdownData[] = [];
    memberChannelList: DropdownData[] = [];
    memberCountryTypeList: DropdownData[] = [];
    highestEducationalList: DropdownData[] = [];
    currentJobList: DropdownData[] = [];
    // addressTypeList: DropdownData[] = [];

    countryList: DropdownData[] = [];
    prefixnameList: DropdownData[] = [];

    provinceList: DropdownData[] = [];
    amphurList: DropdownData[] = [];
    tambonList: DropdownTambonData[] = [];

    user: MenuItem;
    member: MenuItem;

    @ViewChild('fileUpload') fileUpload: any;

    constructor(
        public translate: TranslateService,
        private dropdownService: DropdownService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private userManagementService: UserManagementService,
        private previewFileSerivce: PreviewFileService,
        private uploadFileService: UploadFileService
    ) {
        this.user = {
            label: this.translate.instant('common.module.user'),
            command: () => this.openPage('LIST')
        }

        this.member = {
            label: this.translate.instant('userManagement.member.name'),
            command: () => this.openPage('LIST')
        }
    }

    openPage(page: MODE_PAGE) {
        this.backToListPage.emit('LIST');
    }
    
    ngAfterViewInit(): void {
        this.loadDropDown();
    }

    ngOnInit(): void {
        setTimeout(() => {
            this.initForm = true;
        }, 200);
    }

    userImg: string = 'data:image/png;base64,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';

      onAdvancedUpload(event: FileUploadHandlerEvent) {
        const file = event.files[0];
        this.uploadFileService.postMember(file)
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

    loadDropDown() {
        this.loadImage();
        const { countryId, prefixnameId, memberAdderss } = structuredClone(this.item);
        this.lazyLoadCountry(null, countryId);
        this.lazyLoadPrefixname(null, prefixnameId);
        this.lazyLoadProvince(null, memberAdderss.addressProvince);
        this.lazyLoadAmphur(null, memberAdderss.addressAmphur);
        this.lazyLoadTambon(null, memberAdderss.addressTambon);


        /*
        this.dropdownService
            .getLookup({
                displayCode: false,
                id: LOOKUP_CATALOG.ADDRESS_TYPE
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.addressTypeList = entries;
                } else {
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: message,
                        life: 2000
                    });
                }
            });
        */

        this.dropdownService
            .getLookup({
                displayCode: false,
                id: LOOKUP_CATALOG.HIGHEST_EDUCATIONAL_QUALIFICATION
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.highestEducationalList = entries;
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
                displayCode: false,
                id: LOOKUP_CATALOG.NATIONAL_TYPE
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.memberCountryTypeList = entries;
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
                displayCode: false,
                id: LOOKUP_CATALOG.ACCOUNT_FORMAT
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.memberChannelList = entries;
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
                displayCode: false,
                id: LOOKUP_CATALOG.GENDER
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.genderList = entries;
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
                displayCode: false,
                id: LOOKUP_CATALOG.CURRENT_JOB_STATUS
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.currentJobList = entries;
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

    lazyLoadCountry(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            displayCode: true
        };

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        this.dropdownService.getCountryDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.countryList = entries;
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

    lazyLoadPrefixname(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            displayCode: true
        };

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        this.dropdownService.getPrefixnameDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.prefixnameList = entries;
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

    lazyLoadProvince(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            displayCode: true
        };

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        this.dropdownService.getProvinceDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.provinceList = entries;
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

    lazyLoadAmphur(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            id: this.item?.memberAdderss?.addressProvince,
            displayCode: true
        };

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        this.dropdownService.getAmphurDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.amphurList = entries;
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

    lazyLoadTambon(event?: DropdownFilterEvent, pkId?: number) {
        let dropdownCriteriaData: DropdownCriteriaData = {
            id: this.item?.memberAdderss?.addressAmphur,
            displayCode: true
        };

        if (pkId) {
            dropdownCriteriaData.pkId = pkId;
        }

        if (event && event.filter) {
            dropdownCriteriaData.searchValue = event.filter;
        }

        this.dropdownService.getTambonDropdown(dropdownCriteriaData).subscribe(({ status, message, entries }) => {
            if (status === 200) {
                this.tambonList = entries;
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
        // validate
        if (this.mode === 'EDIT') {
            this.userManagementService
                .putMemberInfo(this.item.memberId, this.item)
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
        }
    }

    onBack() {
        this.backToListPage.emit('LIST');
    }

    changeMemberCountryType(memberCountryType: number) {
        if (memberCountryType === 30028001) {
            /** TH = 221 */
            this.item.countryId = 221;
            this.lazyLoadCountry(null, 221);
        } else {
            this.lazyLoadCountry();
        }
    }

    changeTambon(event: DropdownChangeEvent) {
        if (event.value && this.item.memberAdderss) {
            this.item.memberAdderss.addressZipcode = this.tambonList.filter(({ value }) => value == event.value)[0]?.zipCode
        }
    }

    onClose(event: ToastItemCloseEvent) {
        if (event.message.severity === 'success') {
            this.onBack();
        }
        this.processing = false;
    }
}
