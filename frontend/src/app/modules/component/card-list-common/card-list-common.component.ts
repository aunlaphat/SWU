import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

export interface CardListChip {
    text: string;
    icon?: string;
}
export interface CardListMenu {
    text: string;
    icon?: string;
    callBackData?: CallBackData;
    display: boolean;
    menuCode: string;
}

export interface CallBackData {
    eventName?: string;
    data?: any;
}

export interface CardTitleStatus {
    text: string;
    callBackData?: CallBackData;
}
@Component({
    selector: 'app-card-list-common',
    templateUrl: './card-list-common.component.html',
    styleUrls: ['./card-list-common.component.scss']
})
export class CardListCommonComponent {

    constructor(public translate: TranslateService) {}

    @Input() title: string;
    @Input() titleStatus: CardTitleStatus;
    @Input() titleStatusColor: string = 'coral';
    @Input() memberInfo:any;
    @Input() detail: string;
    @Input() chipList: CardListChip[];
    @Input() menuList: CardListMenu[];
    @Input() imgUrl: string;
    @Input() imgFlag: string;
    @Input() lang: string;

    @Output() callBack = new EventEmitter();

    clickCallBack<T>(event: T) {
        this.callBack.emit(event);
    }

    filterDisplay(menuList: CardListMenu[]): CardListMenu[] {
        if (menuList && menuList.length === 0) {
            return [];
        }
        return menuList.filter(({ display }) => display);
    }

    gotoMoodle() {
        console.log("memberInfo::",this.memberInfo);
        const moodleWeb = 'https://course.lifelong.swu.ac.th/login/lifelong_login.php?username=' + this.memberInfo.email + '&password=' +this.memberInfo.buasriId + 'Lif3l@n9';
        window.open(moodleWeb, '_blank');
    }

    //---Exam Data---
    // title:string="DLLS01 - Listening and Speaking Japanese.."
    // titleStatus:string="รอส่ง"
    // detail:string="ข้อมูลหลักสูตรศิลปศาสตรบัณฑิต สาขาวิชาภาษาตะวันออก · 1) มีทักษะการสื่อสารภาษาจีน ญ่ีปุ่น หรือเกาหลีอย่างมีประสิทธิภาพ และมีความเข้าใจบริบทสังคมและวัฒนธรรม · 2) มีความใฝ่รู้และนำความรู้ทาง"
    // chipList =[
    //   {text:"วันที่ขออนุมัติ",icon:"pi pi-home"},
    //   {text:"Offline",icon:""},
    //   {text:"เทียบเคียงหลักสูตร",icon:""},
    //   {text:"ผู้ช่วยศาตราจารย์",icon:""},

    // ]
    // menuList =[
    //   {text:"รายละเอียด",icon:"pi pi-search",link:"https://angular.io/tutorial/first-app/first-app-lesson-08"},
    //   {text:"แก้ไขข้อมูล",icon:"pi pi-pencil",link:""},
    //   {text:"คัดลอกข้อมูล",icon:"pi pi-home",link:""},
    //   {text:"เอกสารการขออนุมัติ",icon:"pi pi-home",link:""},

    // ]
    // imgUrl="https://www.swu.ac.th/images/swu_logo_v3.png"
    //imgFlag = true;

    //---Exam Component---
    //   <app-card-list-common
    //     [title]="title"
    //     [titleStatus]="titleStatus"
    //     [detail]="detail"
    //     [chipList]="chipList"
    //     [menuList]="menuList"
    //     [imgUrl]="imgUrl"
    //     [imgFlag]="imgFlag">
    // </app-card-list-common>
}
