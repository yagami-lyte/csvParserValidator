/// <reference types="cypress" />
import 'cypress-file-upload';

describe("Test for configuration body",()=> {

    it("Should load server successfully", () => {
        cy.visit('http://localhost:3004')
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

    it("Should Contain input box for configuration filename",()=>{
        cy.get("#card-3").get("#fields").get("#configInput").should('exist')
        cy.contains("Provide Configuration filename")
    })

    it("Should be Able to enter the config name in the input given",()=>{
        cy.get("#card-3").get("#fields")
            .get("#configInput").type('sample.csv', {force: true} )
    })

    it("Should be Able to check the checkbox for saving config data",()=>{
        cy.contains("Tick the checkBox to save the config Data")
        cy.get("#card-3").get("#fields")
            .get(".form-check").get("#configCheckBox").should('exist')
    })
})