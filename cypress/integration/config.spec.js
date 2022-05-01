/// <reference types="cypress" />

describe("Test for configuration body",()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Should contain the Configuration Header",()=>{
        cy.visit('http://localhost:3002')
        cy.contains("Configure your CSV fields").should('exist')
        
    })

})