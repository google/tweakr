import { Component, Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-user-prompt-dialog',
  templateUrl: './user-prompt-dialog.component.html',
  styleUrls: ['./user-prompt-dialog.component.scss']
})
export class UserPromptDialogComponent implements OnInit {

  getMessage() {
    return this.data?.message || 'Enter User PIN code';
  }

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
  }

}
