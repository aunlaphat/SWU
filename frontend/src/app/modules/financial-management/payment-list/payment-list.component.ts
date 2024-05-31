import { HttpHeaders } from '@angular/common/http';
import { FinancialManagementService } from './../../../services/financial-management.service';
import { Component, DoCheck, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TablePageEvent } from 'primeng/table';
import { DropdownData, LOOKUP_CATALOG, MODE_PAGE } from 'src/app/models/common';
import { FinancePaymentData } from 'src/app/models/financial-management';
import { DropdownService } from 'src/app/services/dropdown.service';
import { MasterService } from 'src/app/services/master.service';
import { ReportService } from 'src/app/services/report.service';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-payment-list',
    templateUrl: './payment-list.component.html',
    styleUrls: ['./payment-list.component.scss']
})
export class PaymentListComponent implements OnInit, DoCheck {
    initForm: boolean = false;
    lang: string;

    mode: MODE_PAGE = 'LIST';

    items: FinancePaymentData[] = [];
    totalRecords: number = 0;
    rows: number = 5;

    paymentTypeList: DropdownData[] = [];
    cancelReceiptChoice: any;
    cancelReceiptSelected: FinancePaymentData = {
        paymentId: null,
        cardType: null,
        paymentType: null,
        paymentStatus: null,
        studyStatus: null
    };
    criteria: FinancePaymentData = {
        cardType: null,
        paymentType: null,
        receiptDateList: null,
        publicNameTh: null,
        memberFirstnameTh: null,
        first: 0,
        size: 5
    };
    visible: boolean = false;
    clickYear: boolean = false;

    constructor(
        public translate: TranslateService,
        private financialManagementService: FinancialManagementService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private dropdownService: DropdownService,
        private confirmationService: ConfirmationService,
        private reportService: ReportService
    ) {}

    ngDoCheck(): void {
        if (!this.initForm) {
            this.initForm = !this.initForm;
            this.onSearch();
        }
        if (this.lang != localStorage.getItem('lang')) {
            this.lang = localStorage.getItem('lang');
        }
    }

    ngOnInit(): void {
        this.loadDropDown();
    }

    loadDropDown() {
        this.dropdownService
            .getLookup({
                displayCode: true,
                id: LOOKUP_CATALOG.PAYMENT_TYPE
            })
            .subscribe(({ status, message, entries }) => {
                if (status === 200) {
                    this.paymentTypeList = entries;
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

    onClear() {
        this.criteria = {
            cardType: null,
            paymentType: null,
            receiptDateList: null,
            publicNameTh: null,
            memberFirstnameTh: null,
            first: 0,
            size: 5
        };
        this.onSearch();
    }

    backToFirstPage() {
        let pageFirst = document.getElementsByClassName('p-paginator-first')[0] as HTMLElement;
        pageFirst?.click();
    }

    onSearch(event?: TablePageEvent) {
        this.loaderService.start();

        if (event) {
            this.criteria.size = event.rows;
            this.criteria.first = event.first;
            if (event.rows !== this.rows) {
                this.backToFirstPage();
            }
        } else {
            this.backToFirstPage();
        }

        this.financialManagementService.findFinancePayment(this.criteria).subscribe((result) => {
            this.loaderService.stop();
            if (result.status === 200) {
                this.items = result.entries;
                this.totalRecords = result.totalRecords;
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

    viewDocumnet(item: FinancePaymentData) {
        return `${environment.apiUrl}/publicfile/path?fullpathname=${item.receiptOriginalCaPath}`;
    }
    getDownloadName(headers: HttpHeaders): string {
        const contentDisposition = headers.get('content-disposition');
        let fileName = contentDisposition ? contentDisposition.split(';')[1].split('=')[1] : `document`;
        if (fileName.startsWith('UTF')) {
            fileName = decodeURIComponent(fileName.substring(7));
        }
        return fileName;
    }

    viewDocumnetAjax(item: FinancePaymentData) {

        const { receiptCopyCaPath, receiptOriginalCaPath, memberReceiptViewFlag, paymentId } = item;

        let path = receiptCopyCaPath;

        if (!memberReceiptViewFlag) {
            path = receiptOriginalCaPath;
            item.memberReceiptViewFlag = true;
            this.financialManagementService.putFinancePaymentMemberReceiptViewFlag(paymentId, item)
            .subscribe();
        }
        // return `${environment.apiUrl}/publicfile/path?fullpathname=${item.receiptOriginalCaPath}`;
        this.reportService.downloaod(path).subscribe({
            next: (result) => {
                if (result?.body && result?.headers) {
                    this.onSearch();
                    // saveAs(result.body, this.getDownloadName(result.headers));

                    const data = window.URL.createObjectURL(result.body);
                    window.open(data, '_blank');
                }
            },
            error: (err) => {
                const { status, messages } = err;
                if (messages) {
                    const messageLog = [, , messages];
                    // this.alertServices.onError(...messageLog);
                    this.messageService.add({
                        severity: 'error',
                        summary: this.translate.instant('common.alert.fail'),
                        detail: `${messages}`,
                        life: 2000
                    });
                }
            }
        });
    }
    onExportExcel(data: FinancePaymentData) {
        this.loaderService.start();
        this.criteria.mode = 'excelbase64';

        this.reportService.findPaymentDataExportList(this.criteria).subscribe(({ status, message, entries }) => {
            this.loaderService.stop();
            if (status === 200) {
                console.log('export');

                var link = document.createElement('a');
                document.body.appendChild(link);
                link.setAttribute('type', 'hidden');
                link.href = 'data:application/octet-stream;charset=utf-8;base64,' + entries;
                //console.log('entries :>> ', entries);
                link.download = `ส่งออกข้อมูลการชำระเงิน-${new Date().toJSON().slice(0, 10).replace(/-/g, '')}.xlsx`;
                link.click();
                document.body.removeChild(link);
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
