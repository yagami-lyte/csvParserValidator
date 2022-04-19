/// <reference types="cypress" />

describe("Testing Upload CSV", ()=>{

    it("Should load server successfully", ()=>{
        cy.visit('http://localhost:3002')
    })

    it("Should contain Upload your CSV file here ", ()=>{
        cy.contains("Upload your CSV file here")
    })

     it('Should take csv as input',()=> {
        cy.get('#csv_id').selectFile('cypress/fixtures/countries.csv')
     })

     it('Should contain button to navigate to config section', () => {
        cy.get('#uploadCSV').click()
        cy.visit('http://localhost:3002/#config')
     })
})

//describe("Testing Set Configuration",()=>{
//
//    it('Should visit the config section', () => {
//        cy.visit('http://localhost:3002/#config')
//     })
//
//      it('Should have Export attribute for configuration', () => {
//        cy.get('h4')
//          .should('have.text', ' Export')
//      })
//
//})
//
//describe("Testing View Errors",()=>{
//
//     it('Should visit the error section', () => {
//        cy.visit('http://localhost:3002/#error-msgs')
//     })
//
//     it("Should contain heading ERRORS", ()=>{
//             cy.contains("ERRORS")
//         })
//
//
//})


//describe('Cypress Tests Related to Input Fields', () => {
//
//  it('Should have Name attribute for configuration', () => {
//    cy.get('#rowExport > #fields >h4')
//      .should('have.text', ' Name')
//  })
//
//  it('Should have Age attribute for configuration', () => {
//    cy.get('#rowAge > [style="display: flex;justify-content: left;align-items: center;margin-left:0px"] > p')
//      .should('have.text', ' Age')
//  })
//
//  it('Should have Gender attribute for configuration', () => {
//    cy.get('#rowGender > [style="display: flex;justify-content: left;align-items: center;margin-left:0px"] > p')
//      .should('have.text', ' Gender')
//  })
//
//  it('Should have DOB attribute for configuration', () => {
//    cy.get('#rowDOB > [style="display: flex;justify-content: left;align-items: center;margin-left:0px"] > p')      .should('have.text', ' DOB')
//  })
//
//  it('Should have correct field options for data types', () => {
//    cy.get(':nth-child(1) > :nth-child(2) > select')
//      .contains('Choose')
//
//    cy.get(':nth-child(1) > :nth-child(2) > select')
//      .contains('Numeric')
//
//    cy.get(':nth-child(1) > :nth-child(2) > select')
//      .contains('AlphaNumeric')
//
//    cy.get(':nth-child(1) > :nth-child(2) > select')
//      .contains('Alphabetic')
//  })
//
//  it('Should have an input type of file of the domain values of the column', () => {
//    cy.get('#textOrUploadValuesGender')
//      .select('Upload File')
//  })
//
//  it('Should have an input type of file of the domain values of the column', () => {
//    cy.get('#uploadFileGender > .btn > input').selectFile('cypress/fixtures/exportValues.txt')
//  })
//
//  it('Should have a input field for length as numeric input ', () => {
//    cy.get('#Name')
//  })
//
//  it('Should have correct field options for dependent coulmns', () => {
//    cy.get('#rowName > :nth-child(8)').contains('Choose')
//    cy.get('#rowName > :nth-child(8)').contains('Age')
//    cy.get('#rowName > :nth-child(8)').contains('Gender')
//  })
//
//})
//
//describe('Cypress Tests Related to SEND CONFIGURATION Button', () => {
//
//  it('Should have written SEND CONFIGURATION ON IT', () => {
//    cy.get('.waves-effect').contains('SEND CONFIGURATIONsend')
//  })
//
//  it('Should not be disabled', () => {
//    cy.get('.waves-effect').should('not.be.disabled')
//  })
//
//  it('Should be visible', () => {
//    cy.get('.waves-effect').should('be.visible')
//  })
//
//  it('Should be clickable', () => {
//    cy.get('.waves-effect').click()
//  })
//})
//
//describe('Cypress test related to filling form', () => {
//
//  it('Should select data types for each column', () => {
//    cy.get(':nth-child(2) > :nth-child(2) > select')
//      .select('AlphaNumeric')
//      .should('have.value', 'AlphaNumeric')
//
//    cy.get(':nth-child(1) > :nth-child(2) > select')
//      .select('AlphaNumeric')
//      .should('have.value', 'AlphaNumeric')
//
//    cy.get(':nth-child(3) > :nth-child(2) > select')
//      .select('Alphabetic')
//      .should('have.value', 'Alphabetic')
//
//    cy.get(':nth-child(4) > :nth-child(2) > select')
//      .select('Alphabetic')
//      .should('have.value', 'Alphabetic')
//  })
//
//  // it('',()=>{
//  //   cy.get('#DateTimeD\.O\.B')
//  //     .select('yyyyMMdd')
//  // })
//
//  it('Should have an input type of file of the domain values of the column', () => {
//    cy.get('#textOrUploadValuesName')
//      .select('Upload File')
//  })
//
//  it('Should be able to select a files for domain values of the column', () => {
//    cy.get('#uploadFileName > .btn > input').selectFile('cypress/fixtures/exportValues.txt')
//  })
//
//  it('Should type lengths for different columns', () => {
//    cy.get('#lengthName')
//      .type(15)
//    cy.get('#lengthAge')
//      .type(3)
//    cy.get('#lengthGender')
//      .type(1)
//  })
//
//  // it('Should select dependencies for columns', () => {
//  //   cy.get(':nth-child(1) > :nth-child(5) > select')
//  //     .select('Name')
//  //     .should('have.value', 'Name')
//  // })
//
//  // it('Should be able to select data type', () => {
//  //   cy.get(':nth-child(6) > select')
//  //     .select('yyyyMMdd')
//  //     .should('have.value', 'yyyyMMdd')
//  // })
//
//  // it('Should type for nullValue', () => {
//  //   cy.get('#nullValue').type('N')
//  // })
//
//  it('Should be able to navigate to error page after configuration', () => {
//    cy.get('.waves-effect').click()
//    cy.url().should('include', 'http://localhost:3000/error.html')
//  })
//})
//
//describe('Cypress test for contents on the page', () => {
//
//  it('Should contain Error Page as headding', () => {
//    cy.get('h2').contains('Error Page')
//  })
//
//  it('Should have a button to go to home page', () => {
//    cy.get('.btn').contains('Go to Home page')
//  })
//
//  it('Should have 3 errors', () => {
//    cy.get('.row')
//      .its('length')
//      .should('be.eq', 3)
//  })
//
//  it('Should navigate to home page on clicking the button', () => {
//    cy.get(':nth-child(3) > .col > .btn').click()
//    cy.url().should('include', 'http://localhost:3000/')
//  })
//
//})
