import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlogEntry } from 'app/shared/model/blog-entry.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BlogEntryService } from './blog-entry.service';
import { BlogEntryDeleteDialogComponent } from './blog-entry-delete-dialog.component';

@Component({
  selector: 'jhi-blog-entry',
  templateUrl: './blog-entry.component.html'
})
export class BlogEntryComponent implements OnInit, OnDestroy {
  blogEntries: IBlogEntry[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected blogEntryService: BlogEntryService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.blogEntries = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.blogEntryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IBlogEntry[]>) => this.paginateBlogEntries(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.blogEntries = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInBlogEntries();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IBlogEntry) {
    return item.id;
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  registerChangeInBlogEntries() {
    this.eventSubscriber = this.eventManager.subscribe('blogEntryListModification', () => this.reset());
  }

  delete(blogEntry: IBlogEntry) {
    const modalRef = this.modalService.open(BlogEntryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.blogEntry = blogEntry;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateBlogEntries(data: IBlogEntry[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.blogEntries.push(data[i]);
    }
  }
}
