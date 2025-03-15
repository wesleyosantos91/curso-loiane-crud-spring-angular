import { Component, Inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confimation-dialog',
  imports: [
    MatDialogActions,
    MatButton,
    MatDialogContent
  ],
  template: `
    <div mat-dialog-content>
      {{ data }}
    </div>
    <div mat-dialog-actions align="center">
      <button mat-raised-button color="primary" (click)="onConfirm(true)">Yes</button>
      <button mat-raised-button color="warn" (click)="onConfirm(false)">No</button>
    </div>
  `
})
export class ConfirmationDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string
  ) {
  }

  onConfirm(result: boolean) {
    this.dialogRef.close(result);
  }

}
