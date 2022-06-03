/// <reference types="cypress" />
import 'cypress-file-upload';

describe("Test for configuration body", () => {

    it("Should load server successfully", () => {
        cy.visit('http://localhost:8080')
    })

    it("Should contain the Configuration Header", () => {
        cy.contains("Configure your CSV fields").should('exist')
    })

    it("Links in the body should work properly", () => {
        cy.get('input[type="file"]').attachFile("booking_stats.csv")
        cy.get('#uploadCSV').click({force: true})
    })

    it("Should have Six input fields", () => {
        cy.get('#card-2')
            .get('.row')
            .its('length')
            .should('be.eq', 6)
    })

    it("Should Contain Dropdown for selecting Saved Configurations", () => {
        cy.get('#listOfFileNames').should('exist')
    })

    it("Should Have a Button to fetch Config File", () => {
        cy.get('#fetchConfigButton').should('be.visible')
    })

    it("Should Have a button to Save Configuration", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('#name')
            .should('exist')
    })

    it("Should Contain input box for configuration filename", () => {
        cy.get("#card-3").get("#fields").get("#fileName").should('exist')
        cy.contains("Provide Configuration filename")
    })

    it("Should be Able to enter the config name in the input given", () => {
        cy.get("#card-3").get("#fields")
            .get("#fileName").type('sample.csv', {force: true})
    })

    it("Should be Able to check the checkbox for saving config data", () => {
        cy.contains("Tick the checkBox to save the config Data")
        cy.get("#card-3").get("#fields")
            .get(".form-check").get("#configCheckBox").should('exist')
    })

    it("Should contain the choose Type of data dropdown and should select types of data from the dropdown", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('#typeoperation').select('Text', {force: true})
    })

    it("Should contain the length input field", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('input[type="number"]')
            .should('exist')
    })

    /*it("Should contain the Upload or type values and should be able to select type values", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('#value-divoperation').click({force:true})
    })*/

    it("Should contain Allow empty values", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('label').should('exist')
    })

    it("Should contain Dependent value ", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('input[placeholder="Dependent Value"]')
            .should('exist')
    })

    it("Should contain Dependent fields ", () => {
        cy.get('input[type="file"]')
            .attachFile("booking_stats.csv")
            .get('#uploadCSV')
            .click({force: true})
            .get('select[id="dependentstatus')
            .should('exist')
    })


})
Cypress.on('uncaught:exception', () => {
    return false
})