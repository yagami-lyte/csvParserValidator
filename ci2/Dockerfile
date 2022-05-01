FROM cypress/base:16.14.2-slim

RUN apt-get update && apt-get install -y openjdk-11-jre unzip && rm -rf /var/lib/apt/lists/* &&  npm install --save-dev cypress-file-upload
