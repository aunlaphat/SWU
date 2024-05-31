import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebsiteBannerManageComponent } from './website-banner-manage.component';

describe('WebsiteBannerManageComponent', () => {
  let component: WebsiteBannerManageComponent;
  let fixture: ComponentFixture<WebsiteBannerManageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WebsiteBannerManageComponent]
    });
    fixture = TestBed.createComponent(WebsiteBannerManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
