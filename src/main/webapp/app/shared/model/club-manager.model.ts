import { IClub } from 'app/shared/model/club.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClubManager {
  id?: number;
  status?: ActiveInactiveStatus;
  userLogin?: string;
  userId?: number;
  clubs?: IClub[];
  organizationOrganizationName?: string;
  organizationId?: number;
}

export class ClubManager implements IClubManager {
  constructor(
    public id?: number,
    public status?: ActiveInactiveStatus,
    public userLogin?: string,
    public userId?: number,
    public clubs?: IClub[],
    public organizationOrganizationName?: string,
    public organizationId?: number
  ) {}
}
