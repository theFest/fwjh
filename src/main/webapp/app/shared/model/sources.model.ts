import dayjs from 'dayjs';
import { ITags } from 'app/shared/model/tags.model';

export interface ISources {
  id?: number;
  name?: string;
  url?: string;
  author?: string;
  attachmentsContentType?: string | null;
  attachments?: string | null;
  date?: string;
  tags?: ITags[] | null;
}

export const defaultValue: Readonly<ISources> = {};
