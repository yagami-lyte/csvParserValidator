/// <reference types="cypress" />

describe("Connection testing", ()=>{
    it("It loads successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Checking it certain elements present or not", ()=>{
        cy.contains("Field").should("exist")
        cy.contains("CSV PARSER")
    })
})


describe("Setting Config Data 1st column",()=>{
    it("Adding field Name",()=>{
        cy.get('[data-cy=field]').type("Export")
    })

    it("Adding Type of value field name hold",()=>{
        cy.get('[data-cy=type]').select(3)
    })

    it("Adding value file which has list of possible values", ()=>{
        cy.get('[data-cy=text_file_id]').selectFile("src/main/public/assets/export.txt")
    })

    it("Adding field Length value",()=>{
        cy.get('[data-cy=fixed-len]').type("2")
    })

    it("Adding these data to config file",()=>{

        cy.get('[data-cy=add]').click()
        cy.log("Added 1st Colomn")
    })
})


describe("Testing Submit button",()=>{
    it("Adding field Name",()=>{
        cy.get('[data-cy=field]').clear().type("Country Name")
    })

    it("Adding Type of value field name hold",()=>{
        cy.get('[data-cy=type]').select(3)
    })

    it("Adding value file which has list of possible values", ()=>{
        cy.get('[data-cy=text_file_id]').selectFile("src/main/public/assets/countries.txt")
    })


    it("Adding field Length value",()=>{
        cy.get('[data-cy=fixed-len]').clear().type("2")
    })


    it("Adding these data to config file",()=>{
        cy.get('[data-cy=add]').click()
        cy.log("Added 2nd Colomn")
    })

    it("Submitting the Config data",()=>{

        cy.get('[data-cy=submit]').click()
        cy.log("Submitted the data")
    })
})

describe("CSV Parser", ()=>{
    it("choosing file csv file",()=>{
        cy.get('[data-cy=chooseFile]').selectFile("src/main/public/assets/countries.csv")
    })

    it("Uploading the file",()=>{
        cy.get('[data-cy=upload]').click()
    })
    
})


