import dayjs from 'dayjs';
import { ITopics } from 'app/shared/model/topics.model';

export interface IPosts {
  id?: number;
  title?: string;
  content?: string;
  imagesContentType?: string | null;
  images?: string | null;
  additionalDataContentType?: string | null;
  additionalData?: string | null;
  comments?: string | null;
  date?: string;
  topics?: ITopics | null;
}

export const defaultValue: Readonly<IPosts> = {};
