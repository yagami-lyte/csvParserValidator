ARG N8N_VERSION=0.98.0

FROM cypress/browsers:node16.13.2-chrome100-ff98

RUN apt-get --allow-releaseinfo-change update && apt-get install -y openjdk-11-jre
