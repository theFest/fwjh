import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ITags } from 'app/shared/model/tags.model';

export interface ITopics {
  id?: number;
  name?: string;
  science?: string | null;
  information?: string | null;
  date?: string;
  user?: IUser | null;
  tags?: ITags[] | null;
}

export const defaultValue: Readonly<ITopics> = {};
