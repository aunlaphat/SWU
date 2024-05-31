import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SortModule } from './sort/sort.module';
import { FilterDropdownModule } from './filter-dropdown/filter-dropdown.module';
import { FormatdateModule } from './formatdate/formatdate.module';
import { FormatdatetimeModule } from './formatdatetime/formatdatetime.module';

@NgModule({
    imports: [CommonModule, SortModule, FormatdateModule, FormatdatetimeModule],
    exports: [SortModule, FormatdateModule, FormatdatetimeModule, FilterDropdownModule]
})
export class PipeModule {}
