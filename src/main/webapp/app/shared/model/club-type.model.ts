import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClubType {
  id?: number;
  clubTypeCode?: string;
  clubTypeName?: string;
  clubTypeDescription?: string;
  status?: ActiveInactiveStatus;
}

export class ClubType implements IClubType {
  constructor(
    public id?: number,
    public clubTypeCode?: string,
    public clubTypeName?: string,
    public clubTypeDescription?: string,
    public status?: ActiveInactiveStatus
  ) {}
}
