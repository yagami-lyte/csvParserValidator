// <reference types="cypress" />
import 'cypress-file-upload';

describe("Testing Upload CSV", ()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3004')
    })

    it("Should contain the header",()=>{
        cy.visit('http://localhost:3004')
        cy.contains("CSV Parser And Validator").should('exist')
    })

    it("Should contain the body", ()=>{
        cy.visit('http://localhost:3004')
        cy.contains("Upload your CSV file here").should('exist')
        cy.contains("Upload").should('exist')
        cy.get('div#upload').should('exist')
        cy.get('a#uploadCSV').should('exist')
    })

     it("Links in the body should work properly",()=>{
         cy.visit('http://localhost:3004')
         cy.get('input[type="file"]').attachFile("countries.csv")
     })


})