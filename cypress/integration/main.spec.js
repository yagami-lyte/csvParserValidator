/// <reference types="cypress" />

describe("Connection testing", ()=>{
    it("It loads successfully", ()=>{

        cy.visit('http://localhost:3002')

        cy.contains("Field").should("exist")

        cy.contains("CSV PARSER")
  
        cy.get('[data-cy=field]').type("Export")
    
        cy.get('[data-cy=type]').select(3)
   
        cy.get('[data-cy=text_file_id]').selectFile("src/main/public/assets/export.txt")
    
        cy.get('[data-cy=fixed-len]').type("2")

        cy.get('[data-cy=add]').click()

        cy.log("Added 1st Colomn")

        cy.get('[data-cy=field]').clear().type("Country Name")
    
        cy.get('[data-cy=type]').select(3)
    
        cy.get('[data-cy=text_file_id]').selectFile("src/main/public/assets/countries.txt")
    
        cy.get('[data-cy=fixed-len]').clear().type("2")
    
        cy.get('[data-cy=add]').click()

        cy.log("Added 2nd Colomn")

        cy.get('[data-cy=submit]').click()
        
        cy.log("Submitted the data")
   
        cy.get('[data-cy=chooseFile]').selectFile("src/main/public/assets/countries.csv")
    
        cy.get('[data-cy=upload]').click()
    })
    
})


