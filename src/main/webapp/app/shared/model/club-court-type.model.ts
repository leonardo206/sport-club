import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClubCourtType {
  id?: number;
  clubCourtTypeCode?: string;
  clubCourtTypeName?: string;
  clubCourtTypeDescription?: string;
  status?: ActiveInactiveStatus;
}

export class ClubCourtType implements IClubCourtType {
  constructor(
    public id?: number,
    public clubCourtTypeCode?: string,
    public clubCourtTypeName?: string,
    public clubCourtTypeDescription?: string,
    public status?: ActiveInactiveStatus
  ) {}
}
