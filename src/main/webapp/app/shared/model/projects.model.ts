import dayjs from 'dayjs';
import { ITags } from 'app/shared/model/tags.model';

export interface IProjects {
  id?: number;
  project?: string;
  description?: string;
  content?: string;
  filesContentType?: string | null;
  files?: string | null;
  author?: string;
  date?: string;
  tags?: ITags[] | null;
}

export const defaultValue: Readonly<IProjects> = {};
