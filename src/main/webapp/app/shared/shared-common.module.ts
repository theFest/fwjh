import { NgModule } from '@angular/core';

import { FwblogSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [FwblogSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [FwblogSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FwblogSharedCommonModule {}
