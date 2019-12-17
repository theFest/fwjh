import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BlogEntry } from 'app/shared/model/blog-entry.model';
import { BlogEntryService } from './blog-entry.service';
import { BlogEntryComponent } from './blog-entry.component';
import { BlogEntryDetailComponent } from './blog-entry-detail.component';
import { BlogEntryUpdateComponent } from './blog-entry-update.component';
import { IBlogEntry } from 'app/shared/model/blog-entry.model';

@Injectable({ providedIn: 'root' })
export class BlogEntryResolve implements Resolve<IBlogEntry> {
  constructor(private service: BlogEntryService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBlogEntry> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((blogEntry: HttpResponse<BlogEntry>) => blogEntry.body));
    }
    return of(new BlogEntry());
  }
}

export const blogEntryRoute: Routes = [
  {
    path: '',
    component: BlogEntryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BlogEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BlogEntryDetailComponent,
    resolve: {
      blogEntry: BlogEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BlogEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BlogEntryUpdateComponent,
    resolve: {
      blogEntry: BlogEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BlogEntries'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BlogEntryUpdateComponent,
    resolve: {
      blogEntry: BlogEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'BlogEntries'
    },
    canActivate: [UserRouteAccessService]
  }
];
