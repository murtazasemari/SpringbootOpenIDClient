# 

#Descrption 


#PreRquisites 


# Project Title

SpringbootOpenIDClient

## Description

Spring Security Client created to perfrom OpenID Connect Flow with Forgerock OpenAM 

## Getting Started
Forgerock Installation and configruation is not part of this Code. 
Please change the Application.yaml for correct Authorisaton Server , Client details. 

### Dependencies

* ForgeRock Access Management 7.1.0

### Executing program
java -jar openamClient-0.0.1-SNAPSHOT.jar --spring.config.location=<path-to>/application.yml -Djavax.net.ssl.trustStore=<path-to>\truststore -Djavax.net.ssl.trustStorePassword=<truststore password>
