import { IClubManager } from 'app/shared/model/club-manager.model';
import { IClub } from 'app/shared/model/club.model';
import { ActiveInactiveStatus } from 'app/shared/model/enumerations/active-inactive-status.model';

export interface IOrganization {
  id?: number;
  organizationName?: string;
  taxNumber?: string;
  status?: ActiveInactiveStatus;
  userLogin?: string;
  userId?: number;
  clubManagers?: IClubManager[];
  clubs?: IClub[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public organizationName?: string,
    public taxNumber?: string,
    public status?: ActiveInactiveStatus,
    public userLogin?: string,
    public userId?: number,
    public clubManagers?: IClubManager[],
    public clubs?: IClub[]
  ) {}
}
