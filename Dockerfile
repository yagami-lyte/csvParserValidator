FROM cypress/browsers:node16.13.2-chrome100-ff98

RUN apt-get --allow-releaseinfo-change update && apt-get install -y openjdk-11-jre
