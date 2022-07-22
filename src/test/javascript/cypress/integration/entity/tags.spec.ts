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

describe('Tags e2e test', () => {
  const tagsPageUrl = '/tags';
  const tagsPageUrlPattern = new RegExp('/tags(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tagsSample = { name: 'convergence invoice Borders' };

  let tags: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tags) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tags/${tags.id}`,
      }).then(() => {
        tags = undefined;
      });
    }
  });

  it('Tags menu should load Tags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tags');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tags').should('exist');
    cy.url().should('match', tagsPageUrlPattern);
  });

  describe('Tags page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tagsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tags page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tags/new$'));
        cy.getEntityCreateUpdateHeading('Tags');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tagsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tags',
          body: tagsSample,
        }).then(({ body }) => {
          tags = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tags?page=0&size=20>; rel="last",<http://localhost/api/tags?page=0&size=20>; rel="first"',
              },
              body: [tags],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tagsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tags page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tags');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tagsPageUrlPattern);
      });

      it('edit button click should load edit Tags page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tags');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tagsPageUrlPattern);
      });

      it('last delete button click should delete instance of Tags', () => {
        cy.intercept('GET', '/api/tags/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('tags').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tagsPageUrlPattern);

        tags = undefined;
      });
    });
  });

  describe('new Tags page', () => {
    beforeEach(() => {
      cy.visit(`${tagsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tags');
    });

    it('should create an instance of Tags', () => {
      cy.get(`[data-cy="name"]`).type('azure').should('have.value', 'azure');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tags = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tagsPageUrlPattern);
    });
  });
});
