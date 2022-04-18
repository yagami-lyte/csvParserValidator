/// <reference types="cypress" />

describe("Testing Upload CSV", ()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Should contain csv parser ", ()=>{
        cy.contains("CSV PARSER")
    })

     it('Should take csv as input',()=> {
        cy.get('#csv_id').selectFile('cypress/fixtures/countries.csv')
     })
})

describe("Testing set configuration",()=>{

    it('Should visit the config section', () => {
        cy.visit('http://localhost:3002/#config')
     })

    it("Should add type of value of field",()=>{
        cy.get('[data-cy=type]').select(3)
    })

    it("Should add file which has list of allowed values", ()=>{
        cy.get('[data-cy=text_file_id]').selectFile("cypress/fixtures/countries.txt")
    })

    it("Adding field Length value",()=>{
        cy.get('[data-cy=fixed-len]').type("1")
    })

    it("Adding these data to config file",()=>{
        cy.get('[data-cy=add]').click()
        cy.log("Added 1st Column")
    })
})

describe("Testing Submit Configuration button",()=>{

    it("Adding Type of value field name hold",()=>{
        cy.get('[data-cy=type]').select(3)
    })

    it("Adding value file which has list of possible values", ()=>{
        cy.get('[data-cy=text_file_id]').selectFile("cypress/fixtures/countries.txt")
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