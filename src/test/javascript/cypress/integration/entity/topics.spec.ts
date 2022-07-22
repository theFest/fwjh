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

describe('Topics e2e test', () => {
  const topicsPageUrl = '/topics';
  const topicsPageUrlPattern = new RegExp('/topics(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const topicsSample = { name: 'neural Comoros', date: '2022-03-16' };

  let topics: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/topics+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/topics').as('postEntityRequest');
    cy.intercept('DELETE', '/api/topics/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (topics) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/topics/${topics.id}`,
      }).then(() => {
        topics = undefined;
      });
    }
  });

  it('Topics menu should load Topics page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('topics');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Topics').should('exist');
    cy.url().should('match', topicsPageUrlPattern);
  });

  describe('Topics page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(topicsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Topics page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/topics/new$'));
        cy.getEntityCreateUpdateHeading('Topics');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', topicsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/topics',
          body: topicsSample,
        }).then(({ body }) => {
          topics = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/topics+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/topics?page=0&size=20>; rel="last",<http://localhost/api/topics?page=0&size=20>; rel="first"',
              },
              body: [topics],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(topicsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Topics page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('topics');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', topicsPageUrlPattern);
      });

      it('edit button click should load edit Topics page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Topics');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', topicsPageUrlPattern);
      });

      it('last delete button click should delete instance of Topics', () => {
        cy.intercept('GET', '/api/topics/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('topics').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', topicsPageUrlPattern);

        topics = undefined;
      });
    });
  });

  describe('new Topics page', () => {
    beforeEach(() => {
      cy.visit(`${topicsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Topics');
    });

    it('should create an instance of Topics', () => {
      cy.get(`[data-cy="name"]`).type('Alabama').should('have.value', 'Alabama');

      cy.get(`[data-cy="science"]`).type('parsing generating').should('have.value', 'parsing generating');

      cy.get(`[data-cy="information"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="date"]`).type('2022-03-16').should('have.value', '2022-03-16');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        topics = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', topicsPageUrlPattern);
    });
  });
});
