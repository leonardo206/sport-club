import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClubCourtType } from 'app/shared/model/club-court-type.model';

@Component({
  selector: 'jhi-club-court-type-detail',
  templateUrl: './club-court-type-detail.component.html',
})
export class ClubCourtTypeDetailComponent implements OnInit {
  clubCourtType: IClubCourtType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubCourtType }) => (this.clubCourtType = clubCourtType));
  }

  previousState(): void {
    window.history.back();
  }
}
