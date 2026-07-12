API Gateway

This service acts as the central entry point for all microservice requests. It leverages Spring Cloud Gateway and Eureka Service Discovery to dynamically route traffic to backend services.
Architecture Overview

This gateway does not have hardcoded routes. Instead, it queries the Eureka Service Registry at runtime to locate backend services based on their application name.

    Eureka Server: https://eureka-service-s7wp.onrender.com/eureka/

    Discovery Mode: Enabled (automatically maps /service-name/ to the target instance).

Getting Started (Local Development)

To run the gateway locally and connect it to the registry:

    Prerequisites:

        Java 21

        Maven

    Run the application:
    Bash

    ./mvnw spring-boot:run

    Configuration:
    Ensure your local application.properties includes the registry URL:
    Properties

    eureka.client.serviceUrl.defaultZone=https://eureka-service-s7wp.onrender.com/eureka/

Routing Guide

You do not need to update the gateway to add new services. Simply ensure your new microservice:

    Sets spring.application.name in its properties.

    Registers with the Eureka server provided above.

Example Request Flow:

    Frontend calls: GET /complaint-service/api/tickets

    Gateway resolves: complaint-service via Eureka.

    Result: Request is forwarded to the complaint-service instance.

Deployment

    Environment: Render

    Deployment Trigger: Automatic on push to main branch.

    Important: Ensure EUREKA_CLIENT_SERVICEURL_DEFAULTZONE is set as an Environment Variable in the Render dashboard for proper cloud connectivity.

Troubleshooting

    404 Not Found: The Gateway is running, but the requested service is likely not registered with Eureka. Check the Eureka Dashboard.

    Connection Refused: Ensure the eureka.client.serviceUrl.defaultZone environment variable is correctly configured in Render.

How to add this to your repo:

    Create a file named README.md in the root of your api-gateway folder.

    Paste the content above.

    Commit and push:
    Bash

    git add README.md
    git commit -m "docs: add readme for team members"
    git push origin <your-branch-name>
