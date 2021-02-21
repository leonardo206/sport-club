import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClubType, ClubType } from 'app/shared/model/club-type.model';
import { ClubTypeService } from './club-type.service';

@Component({
  selector: 'jhi-club-type-update',
  templateUrl: './club-type-update.component.html',
})
export class ClubTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    clubTypeCode: [null, [Validators.required]],
    clubTypeName: [null, [Validators.required]],
    clubTypeDescription: [null, [Validators.required]],
    status: [],
  });

  constructor(protected clubTypeService: ClubTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clubType }) => {
      this.updateForm(clubType);
    });
  }

  updateForm(clubType: IClubType): void {
    this.editForm.patchValue({
      id: clubType.id,
      clubTypeCode: clubType.clubTypeCode,
      clubTypeName: clubType.clubTypeName,
      clubTypeDescription: clubType.clubTypeDescription,
      status: clubType.status,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clubType = this.createFromForm();
    if (clubType.id !== undefined) {
      this.subscribeToSaveResponse(this.clubTypeService.update(clubType));
    } else {
      this.subscribeToSaveResponse(this.clubTypeService.create(clubType));
    }
  }

  private createFromForm(): IClubType {
    return {
      ...new ClubType(),
      id: this.editForm.get(['id'])!.value,
      clubTypeCode: this.editForm.get(['clubTypeCode'])!.value,
      clubTypeName: this.editForm.get(['clubTypeName'])!.value,
      clubTypeDescription: this.editForm.get(['clubTypeDescription'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClubType>>): void {
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
