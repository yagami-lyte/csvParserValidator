/// <reference types="cypress" />
import 'cypress-file-upload';

describe("Test for Error body",()=> {

    it("Should load server successfully", () => {
        cy.visit('http://localhost:3004')
    })

    it("Should Contain the Error Header", () => {
        cy.contains('CSV Errors')
    })

    it("Should contain View Errors Button", () => {
        cy.contains('View Errors')
        cy.get('#view-errors').should('exist')
    })

    it("Should Contain CSV Errors as heading",()=>{
        cy.contains('CSV Errors')
        cy.get('h5').should('exist')
    })

})

Cypress.on('uncaught:exception', (err, runnable) => {
    return false
})