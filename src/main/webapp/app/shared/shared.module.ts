import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FwblogSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [FwblogSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [FwblogSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FwblogSharedModule {
  static forRoot() {
    return {
      ngModule: FwblogSharedModule
    };
  }
}
