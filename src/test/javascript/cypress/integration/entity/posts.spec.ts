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

describe('Posts e2e test', () => {
  const postsPageUrl = '/posts';
  const postsPageUrlPattern = new RegExp('/posts(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postsSample = { title: 'tan', content: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', date: '2022-03-16' };

  let posts: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/posts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/posts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/posts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (posts) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/posts/${posts.id}`,
      }).then(() => {
        posts = undefined;
      });
    }
  });

  it('Posts menu should load Posts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('posts');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Posts').should('exist');
    cy.url().should('match', postsPageUrlPattern);
  });

  describe('Posts page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Posts page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/posts/new$'));
        cy.getEntityCreateUpdateHeading('Posts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', postsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/posts',
          body: postsSample,
        }).then(({ body }) => {
          posts = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/posts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/posts?page=0&size=20>; rel="last",<http://localhost/api/posts?page=0&size=20>; rel="first"',
              },
              body: [posts],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Posts page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('posts');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', postsPageUrlPattern);
      });

      it('edit button click should load edit Posts page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Posts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', postsPageUrlPattern);
      });

      it('last delete button click should delete instance of Posts', () => {
        cy.intercept('GET', '/api/posts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('posts').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', postsPageUrlPattern);

        posts = undefined;
      });
    });
  });

  describe('new Posts page', () => {
    beforeEach(() => {
      cy.visit(`${postsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Posts');
    });

    it('should create an instance of Posts', () => {
      cy.get(`[data-cy="title"]`).type('cross-media Bedfordshire').should('have.value', 'cross-media Bedfordshire');

      cy.get(`[data-cy="content"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('images', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('additionalData', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="comments"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="date"]`).type('2022-03-16').should('have.value', '2022-03-16');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        posts = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', postsPageUrlPattern);
    });
  });
});
