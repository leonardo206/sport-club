import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClubManager } from 'app/shared/model/club-manager.model';

@Component({
  selector: 'jhi-club-manager-detail',
  templateUrl: './club-manager-detail.component.html',
})
export class ClubManagerDetailComponent implements OnInit {
  clubManager: IClubManager | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubManager }) => (this.clubManager = clubManager));
  }

  previousState(): void {
    window.history.back();
  }
}
