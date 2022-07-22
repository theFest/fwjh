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

describe('Projects e2e test', () => {
  const projectsPageUrl = '/projects';
  const projectsPageUrlPattern = new RegExp('/projects(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const projectsSample = {
    project: 'Shoes',
    description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    author: 'HTTP',
    date: '2022-03-16',
  };

  let projects: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/projects+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/projects').as('postEntityRequest');
    cy.intercept('DELETE', '/api/projects/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (projects) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/projects/${projects.id}`,
      }).then(() => {
        projects = undefined;
      });
    }
  });

  it('Projects menu should load Projects page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('projects');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Projects').should('exist');
    cy.url().should('match', projectsPageUrlPattern);
  });

  describe('Projects page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(projectsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Projects page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/projects/new$'));
        cy.getEntityCreateUpdateHeading('Projects');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projectsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/projects',
          body: projectsSample,
        }).then(({ body }) => {
          projects = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/projects+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/projects?page=0&size=20>; rel="last",<http://localhost/api/projects?page=0&size=20>; rel="first"',
              },
              body: [projects],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(projectsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Projects page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('projects');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projectsPageUrlPattern);
      });

      it('edit button click should load edit Projects page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Projects');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projectsPageUrlPattern);
      });

      it('last delete button click should delete instance of Projects', () => {
        cy.intercept('GET', '/api/projects/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('projects').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projectsPageUrlPattern);

        projects = undefined;
      });
    });
  });

  describe('new Projects page', () => {
    beforeEach(() => {
      cy.visit(`${projectsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Projects');
    });

    it('should create an instance of Projects', () => {
      cy.get(`[data-cy="project"]`).type('Tanzanian bi-directional').should('have.value', 'Tanzanian bi-directional');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="content"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('files', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="author"]`).type('Operations primary Division').should('have.value', 'Operations primary Division');

      cy.get(`[data-cy="date"]`).type('2022-03-16').should('have.value', '2022-03-16');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        projects = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', projectsPageUrlPattern);
    });
  });
});
