/// <reference types="cypress" />

describe("Testing Upload CSV", ()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Should contain Upload your CSV file here ", ()=>{
        cy.contains("Upload your CSV file here")
    })

     it('Should take csv as input',()=> {
        cy.get('#csv_id').selectFile('cypress/fixtures/countries.csv',{force: true})
        cy.get('input[type="file"]').attachFile('countries.csv');
     })

     it('Should contain button to navigate to config section', () => {
        cy.get('#uploadCSV').click({force: true})
        cy.visit('http://localhost:3002/#config')
    })
})