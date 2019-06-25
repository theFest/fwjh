import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FwblogSharedModule } from 'app/shared';
import {
  BlogComponent,
  BlogDetailComponent,
  BlogUpdateComponent,
  BlogDeletePopupComponent,
  BlogDeleteDialogComponent,
  blogRoute,
  blogPopupRoute
} from './';

const ENTITY_STATES = [...blogRoute, ...blogPopupRoute];

@NgModule({
  imports: [FwblogSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [BlogComponent, BlogDetailComponent, BlogUpdateComponent, BlogDeleteDialogComponent, BlogDeletePopupComponent],
  entryComponents: [BlogComponent, BlogUpdateComponent, BlogDeleteDialogComponent, BlogDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FwblogBlogModule {}
