import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorDiologComponent } from './error-dialog.component';

describe('ErrorDiologComponent', () => {
  let component: ErrorDiologComponent;
  let fixture: ComponentFixture<ErrorDiologComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ErrorDiologComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ErrorDiologComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
