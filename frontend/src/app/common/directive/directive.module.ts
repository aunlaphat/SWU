import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThaiCalendarModule } from './thai-calendar/thai-calendar.module';
import { PermissionModule } from './permission/permission.module';
import { RestrictionModule } from './restriction/restriction.module';

@NgModule({
    imports: [CommonModule, ThaiCalendarModule, PermissionModule, RestrictionModule],
    exports: [ThaiCalendarModule, PermissionModule, RestrictionModule]
})
export class DirectiveModule {}
