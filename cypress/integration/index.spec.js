/// <reference types="cypress" />
import 'cypress-file-upload';

describe("Testing Upload CSV", ()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Should contain the header",()=>{
        cy.visit('http://localhost:3002')
        cy.contains("CSV Parser And Validator").should('exist')
    })


})