import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Sources e2e test', () => {
  const sourcesPageUrl = '/sources';
  const sourcesPageUrlPattern = new RegExp('/sources(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sourcesSample = { name: 'Account', url: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', author: 'Namibia', date: '2022-03-16' };

  let sources: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sources) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sources/${sources.id}`,
      }).then(() => {
        sources = undefined;
      });
    }
  });

  it('Sources menu should load Sources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sources');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Sources').should('exist');
    cy.url().should('match', sourcesPageUrlPattern);
  });

  describe('Sources page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sourcesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Sources page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sources/new$'));
        cy.getEntityCreateUpdateHeading('Sources');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sources',
          body: sourcesSample,
        }).then(({ body }) => {
          sources = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sources?page=0&size=20>; rel="last",<http://localhost/api/sources?page=0&size=20>; rel="first"',
              },
              body: [sources],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sourcesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Sources page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sources');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesPageUrlPattern);
      });

      it('edit button click should load edit Sources page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Sources');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesPageUrlPattern);
      });

      it('last delete button click should delete instance of Sources', () => {
        cy.intercept('GET', '/api/sources/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('sources').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourcesPageUrlPattern);

        sources = undefined;
      });
    });
  });

  describe('new Sources page', () => {
    beforeEach(() => {
      cy.visit(`${sourcesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Sources');
    });

    it('should create an instance of Sources', () => {
      cy.get(`[data-cy="name"]`).type('Handmade Mississippi Crossroad').should('have.value', 'Handmade Mississippi Crossroad');

      cy.get(`[data-cy="url"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="author"]`).type('value-added').should('have.value', 'value-added');

      cy.setFieldImageAsBytesOfEntity('attachments', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="date"]`).type('2022-03-16').should('have.value', '2022-03-16');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        sources = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', sourcesPageUrlPattern);
    });
  });
});
