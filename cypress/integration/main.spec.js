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

     it('Should contain button to navigate to config section', () => {
        cy.get('a')
          .should('have.attr', 'href').and('include', 'config')
          .then((href) => {
            cy.visit('http://localhost:3002/#config')
          })
     })
})

describe("Testing Set Configuration",()=>{

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

describe("Testing View Errors",()=>{

     it('Should visit the error section', () => {
        cy.visit('http://localhost:3002/#error-msgs')
     })

     it("Should contain heading ERRORS", ()=>{
             cy.contains("ERRORS")
         })


})


