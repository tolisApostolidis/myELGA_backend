# Table of Contents
* [About](#about)
* [Requirements](#requirements)
* [Prepare execution](#prepare-execution)
* [How to run](#how-to-run)
* [How to build Docker image](#how-to-build-docker-image)
* [CI/CD](#cicd)
* [Related repositories](#related-repositories)

# About 
Backend component of the myELGA agricultural compensation management platform. The application provides:
- **REST API**
- **Role-based** authentication and authorization
- **Management of applications** submited by users
- **Database communication** 
- **PDF generation** for applications
- **Email notifications** for user actions

# Requirements
In order to build and run the backend application, you must install the following tools:
* [ ] Install Java JDK
    * 21 = required version
* [ ] Install Maven
  * 3.9+
  * 3.9.16 = recommended version
* [ ] Install Docker
    * Latest stable version = recommended
* [ ] Install Git
    * Required to clone the repository

# Prepare execution
Before running the backend application, create a local environment file based on the provided `.env.example` file. Create the file by running the following command:
```
$ cp .env.example .env
```
Open the created `.env` file and replace the example values with the actual configuration values and credentials.
> [!WARNING]
> The `.env` file contains sensitive configuration data and should not be committed to the repository.

# How to run
Run the application using Maven:
```
$ mvn spring-boot:run
```

Alternatively, build the application first and run the generated JAR file:
```
$ mvn clean package

$ java -jar target/myElga-0.0.1-SNAPSHOT.jar
```

# How to build Docker image
Build the image using Docker with the following command:
```
$ docker build -t myelga-backend .
```

# CI/CD
The `Jenkinsfile` automates the process of testing, building and publishing the Docker image to GHCR. For each build, Jenkins:
1. Runs the Maven tests
2. Generates an image tag based on Git commit and Jenkins build number
3. Builds the Docker image with both the generated tag and `latest`
4. Authenticates with GHCR
5. Publishes the image to GHCR with both tags
6. Triggers the `myELGA-deployment` Jenkins job with the `backend` component to restart the Kubernetes deployment

# Related repositories
<table>
    <thead>
        <tr>
            <th>Repository name</th>
            <th>URL</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>myELGA_database</td>
            <td>https://github.com/tolisApostolidis/myELGA_database</td>
        </tr>
        <tr>
            <td>myELGA_frontend</td>
            <td>https://github.com/tolisApostolidis/myELGA_frontend</td>
        </tr>
        <tr>
            <td>myELGA_deployment</td>
            <td>https://github.com/tolisApostolidis/myELGA_deployment</td>
        </tr>
</table>