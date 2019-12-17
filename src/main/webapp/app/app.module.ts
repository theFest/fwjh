import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FwblogSharedModule } from 'app/shared/shared.module';
import { FwblogCoreModule } from 'app/core/core.module';
import { FwblogAppRoutingModule } from './app-routing.module';
import { FwblogHomeModule } from './home/home.module';
import { FwblogEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FwblogSharedModule,
    FwblogCoreModule,
    FwblogHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FwblogEntityModule,
    FwblogAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class FwblogAppModule {}
