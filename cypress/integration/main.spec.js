/// <reference types="cypress" />

describe("Connection testing", ()=>{
    it("hitting the URL", ()=>{
        cy.visit('http://localhost:3000')
    })

    it("Checking it certain elements present or not", ()=>{
        cy.contains("Field").should("exist")
        cy.contains("CSV PARSER")
    })
})

describe("Typing data and checking field", ()=>{
    it("Writing in config file",()=>{
        cy.get("#field").type("productId")
        cy.get("#type").select(1)
    })
})

