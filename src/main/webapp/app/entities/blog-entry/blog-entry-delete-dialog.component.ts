import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBlogEntry } from 'app/shared/model/blog-entry.model';
import { BlogEntryService } from './blog-entry.service';

@Component({
  templateUrl: './blog-entry-delete-dialog.component.html'
})
export class BlogEntryDeleteDialogComponent {
  blogEntry: IBlogEntry;

  constructor(protected blogEntryService: BlogEntryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.blogEntryService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'blogEntryListModification',
        content: 'Deleted an blogEntry'
      });
      this.activeModal.dismiss(true);
    });
  }
}
