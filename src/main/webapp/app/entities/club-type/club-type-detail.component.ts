import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClubType } from 'app/shared/model/club-type.model';

@Component({
  selector: 'jhi-club-type-detail',
  templateUrl: './club-type-detail.component.html',
})
export class ClubTypeDetailComponent implements OnInit {
  clubType: IClubType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubType }) => (this.clubType = clubType));
  }

  previousState(): void {
    window.history.back();
  }
}
