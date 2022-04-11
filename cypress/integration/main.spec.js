/// <reference types="cypress" />

describe("Connection testing", ()=>{
    it("It loads successfully", ()=>{
        cy.visit('http://localhost:3000')
    })

    it("Checking it certain elements present or not", ()=>{
        cy.contains("Field").should("exist")
        cy.contains("CSV PARSER")
    })
})

describe("Setting Config Data",()=>{
    it("Adding field Name",()=>{
        cy.get('[data-cy=field]').type("productID")
    })

    it("Adding Type of value field name hold",()=>{
        cy.get('[data-cy=type]').select(2)
    })
    it("Adding these data to config file",()=>{

        cy.get('[data-cy=add]').click()
        cy.log("Added 1st Colomn")
    })
    
})

describe("CSV Parser", ()=>{
    it("choosing file csv file",()=>{
        cy.get('[data-cy=chooseFile]').selectFile("src/main/public/assets/data.csv")
    })

    it("Uploading the file",()=>{
        cy.get('[data-cy=upload]').click()
    })

    it("Choosing Non-CSV file",()=>{
        cy.get('[data-cy=chooseFile]').not
    })
    
})


