import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FwblogSharedModule } from 'app/shared/shared.module';
import { BlogEntryComponent } from './blog-entry.component';
import { BlogEntryDetailComponent } from './blog-entry-detail.component';
import { BlogEntryUpdateComponent } from './blog-entry-update.component';
import { BlogEntryDeleteDialogComponent } from './blog-entry-delete-dialog.component';
import { blogEntryRoute } from './blog-entry.route';

@NgModule({
  imports: [FwblogSharedModule, RouterModule.forChild(blogEntryRoute)],
  declarations: [BlogEntryComponent, BlogEntryDetailComponent, BlogEntryUpdateComponent, BlogEntryDeleteDialogComponent],
  entryComponents: [BlogEntryDeleteDialogComponent]
})
export class FwblogBlogEntryModule {}
