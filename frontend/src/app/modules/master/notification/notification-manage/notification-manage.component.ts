import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ConfirmationService, MessageService } from 'primeng/api';
import { MODE_PAGE,LOOKUP_CATALOG } from 'src/app/models/common';
import { DropdownService } from 'src/app/services/dropdown.service';
import { MasterService } from 'src/app/services/master.service';
import { TablePageEvent } from 'primeng/table';
@Component({
    selector: 'app-notification-manage',
    templateUrl: './notification-manage.component.html',
    styleUrls: ['./notification-manage.component.scss']
})
export class NotificationManageComponent {
    @Input() lang: string;
    @Input() mode: MODE_PAGE = 'EDIT'
    @Output() goBack = new EventEmitter();
    notiInfo:any;
    receiverList: any[] = [];
    criteria: any = {
        topic: null,
        receiver: null,
        detail: null,
        status: null,
        first: 0,
        size: 5
    }
    constructor(
        public translate: TranslateService,
        private messageService: MessageService,
        private loaderService: NgxUiLoaderService,
        private dropdownService: DropdownService,
        private confirmationService: ConfirmationService,
        private masterService:MasterService
    ) {
    }
    ngOnInit(): void {
        console.log("localStorage.getItem('notiInfo')::", JSON.parse(localStorage.getItem('notiInfo')));
        this.notiInfo =  JSON.parse(localStorage.getItem('notiInfo'));
        this.loadDropDownReceiverList();
        // this.onSearch()
      }
      loadDropDownReceiverList() {
        this.dropdownService
          .getLookup({
            displayCode: false,
            id: LOOKUP_CATALOG.NOTI_RECEIVER_TYPE
          }).subscribe(({ status, message, entries }) => {
            if (status === 200) {
              console.log("entries:", entries);
              this.receiverList = entries;
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
    onSave(event: Event,item?:any) {
        this.confirmationService.confirm({
            key: 'confirm1',
            target: event.target || new EventTarget,
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              console.log(">>>>yes<<<<");
              console.log(">>>item:",item);
              this.masterService.updateNotiDetail(item).subscribe((result) => {
                if (result.status === 200) {
                  console.log("entries:", result.entries);
                  this.messageService.add({
                    severity: 'success',
                    summary: this.translate.instant('common.alert.success'),
                    detail: result.message,
                    life: 2000 });
                  this.onBack();
                } else {
                  this.messageService.add({
                    severity: 'error',
                    summary: this.translate.instant('common.alert.fail'),
                    detail: result.message,
                    life: 2000
                  });
                }
              });
            },
            reject: () => {
              console.log(">>>>no<<<<");
              this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            }
          });

     }
    onBack() {
        this.goBack.emit('LIST');
    }

}
