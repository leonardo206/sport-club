import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClubCourtType, ClubCourtType } from 'app/shared/model/club-court-type.model';
import { ClubCourtTypeService } from './club-court-type.service';

@Component({
  selector: 'jhi-club-court-type-update',
  templateUrl: './club-court-type-update.component.html',
})
export class ClubCourtTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    clubCourtTypeCode: [null, [Validators.required]],
    clubCourtTypeName: [null, [Validators.required]],
    clubCourtTypeDescription: [],
    status: [],
  });

  constructor(protected clubCourtTypeService: ClubCourtTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubCourtType }) => {
      this.updateForm(clubCourtType);
    });
  }

  updateForm(clubCourtType: IClubCourtType): void {
    this.editForm.patchValue({
      id: clubCourtType.id,
      clubCourtTypeCode: clubCourtType.clubCourtTypeCode,
      clubCourtTypeName: clubCourtType.clubCourtTypeName,
      clubCourtTypeDescription: clubCourtType.clubCourtTypeDescription,
      status: clubCourtType.status,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clubCourtType = this.createFromForm();
    if (clubCourtType.id !== undefined) {
      this.subscribeToSaveResponse(this.clubCourtTypeService.update(clubCourtType));
    } else {
      this.subscribeToSaveResponse(this.clubCourtTypeService.create(clubCourtType));
    }
  }

  private createFromForm(): IClubCourtType {
    return {
      ...new ClubCourtType(),
      id: this.editForm.get(['id'])!.value,
      clubCourtTypeCode: this.editForm.get(['clubCourtTypeCode'])!.value,
      clubCourtTypeName: this.editForm.get(['clubCourtTypeName'])!.value,
      clubCourtTypeDescription: this.editForm.get(['clubCourtTypeDescription'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClubCourtType>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
