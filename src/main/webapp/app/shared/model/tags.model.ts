import { ITopics } from 'app/shared/model/topics.model';
import { IProjects } from 'app/shared/model/projects.model';
import { ISources } from 'app/shared/model/sources.model';

export interface ITags {
  id?: number;
  name?: string;
  entries?: ITopics[] | null;
  names?: IProjects[] | null;
  sources?: ISources[] | null;
}

export const defaultValue: Readonly<ITags> = {};
