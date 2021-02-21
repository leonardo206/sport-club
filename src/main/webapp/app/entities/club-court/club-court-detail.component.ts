import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClubCourt } from 'app/shared/model/club-court.model';

@Component({
  selector: 'jhi-club-court-detail',
  templateUrl: './club-court-detail.component.html',
})
export class ClubCourtDetailComponent implements OnInit {
  clubCourt: IClubCourt | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubCourt }) => (this.clubCourt = clubCourt));
  }

  previousState(): void {
    window.history.back();
  }
}
