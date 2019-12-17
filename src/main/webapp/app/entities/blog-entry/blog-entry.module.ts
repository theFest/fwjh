import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FwblogSharedModule } from 'app/shared/shared.module';
import { BlogEntryComponent } from './blog-entry.component';
import { BlogEntryDetailComponent } from './blog-entry-detail.component';
import { BlogEntryUpdateComponent } from './blog-entry-update.component';
import { BlogEntryDeletePopupComponent, BlogEntryDeleteDialogComponent } from './blog-entry-delete-dialog.component';
import { blogEntryRoute, blogEntryPopupRoute } from './blog-entry.route';

const ENTITY_STATES = [...blogEntryRoute, ...blogEntryPopupRoute];

@NgModule({
  imports: [FwblogSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BlogEntryComponent,
    BlogEntryDetailComponent,
    BlogEntryUpdateComponent,
    BlogEntryDeleteDialogComponent,
    BlogEntryDeletePopupComponent
  ],
  entryComponents: [BlogEntryComponent, BlogEntryUpdateComponent, BlogEntryDeleteDialogComponent, BlogEntryDeletePopupComponent]
})
export class FwblogBlogEntryModule {}
