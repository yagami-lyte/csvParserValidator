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

describe("CSV Parser div", ()=>{
    it("choosing file csv file",()=>{
        cy.get("#csv_id").selectFile("src/main/public/assets/data.csv")
    })

    it("Uploading the file",()=>{
        cy.get("#csv-submit-button").click()
    })
})


