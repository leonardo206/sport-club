import { IClubCourt } from 'app/shared/model/club-court.model';
import { IClubManager } from 'app/shared/model/club-manager.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IClub {
  id?: number;
  clubName?: string;
  status?: ActiveInactiveStatus;
  clubTypeClubTypeName?: string;
  clubTypeId?: number;
  clubCourts?: IClubCourt[];
  organizationOrganizationName?: string;
  organizationId?: number;
  clubManagers?: IClubManager[];
}

export class Club implements IClub {
  constructor(
    public id?: number,
    public clubName?: string,
    public status?: ActiveInactiveStatus,
    public clubTypeClubTypeName?: string,
    public clubTypeId?: number,
    public clubCourts?: IClubCourt[],
    public organizationOrganizationName?: string,
    public organizationId?: number,
    public clubManagers?: IClubManager[]
  ) {}
}
