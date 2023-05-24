# Documentation of Mow-E Backend

# !!!ZIS IS NOT UP TO DATE!!!

## High Level Requirements

| #B1.1                                                                                 | #B1.2  | #B1.3  |
|---------------------------------------------------------------------------------------|---|---|
| Publish a REST API for reading and writing <br/>position data that is sent from the mower. | The REST API shall contain a service <br>for reading and writing image data.  | When image data is written, the service shall perform <br>an image classification via for example Google API.  |

### Low Level Requirements

<table style="table-layout: fixed; text-align: center; width: 822px">
<colgroup>
<col style="width: 196.333333px">
<col style="width: 209.333333px">
<col style="width: 205.333333px">
<col style="width: 211.333333px">
</colgroup>
<thead>
  <tr>
    <th>#B1.1<br>Websockets for R/W<br>mowers position data</th>
    <th>#B1.2<br>Service for read/write<br>image data</th>
    <th>#B1.3<br>Service for image classification</th>
    <th>#B1.4<br>Security</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>#B1.1a<br>Endpoint for accessing websocket</td>
    <td>#B1.2a<br>Websocket endpoint<br>for receiving images</td>
    <td>#B1.3a<br>Access to Google Vision API</td>
    <td>#B1.4a<br>Enabling security in Spring</td>
  </tr>
  <tr>
    <td>#B1.1b<br>Controller for processing the websocket endpoints</td>
    <td>#B1.2b<br>Image storing</td>
    <td>#B1.3b<br>Service for Image classification</td>
    <td>#B1.4b<br>User-Admin tables in db</td>
  </tr>
  <tr>
    <td>#B1.1c<br>Position data prototype</td>
    <td>#B1.2c<br>Bind image to collision coordinate</td>
    <td>#B1.3c<br>Saving data from Image classification service</td>
    <td>#B1.4c<br>Authentication &amp; Authorization via REST API</td>
  </tr>
  <tr>
    <td>#B1.1d<br>Service for saving positioning data</td>
    <td>#B1.2d<br></td>
    <td>#B1.3d</td>
    <td>#B1.4d<br>Authentification via JWT</td>
  </tr>
  <tr>
    <td>#B1.1e<br>Position data broadcast<br>for Mobile</td>
    <td>#B1.2e<br></td>
    <td>#B1.3e</td>
    <td>#B1.4e<br>Websocket authentification</td>
  </tr>
  <tr>
    <td>#B1.4f<br></td>
    <td>#B1.2f<br></td>
    <td>#B1.3f</td>
    <td>#B1.4f<br>Error handling</td>
  </tr>
  <tr>
    <td>#B1.4g<br></td>
    <td>#B1.2g<br></td>
    <td>#B1.3g</td>
    <td>#B1.4g</td>
  </tr>
</tbody>
</table>

# File structure overview
![detailed_structure_dark.png](doc%2Fimages%2Fdetailed_structure_dark.png)

## Main Class

-   [SweBotApplication.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2FSweBotApplication.java)
    -    The heart of the project
        -   Contains the swagger configuration.

## Configs

-   [ErrorConfig.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fconfigs%2FErrorConfig.java)
    -   This class captures all untracked errors from endpoints and returns a default error-json.

-   [ExecutorConfig.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fconfigs%2FExecutorConfig.java)
    -   We reserve a pool of executors that are needed for various tasks.
        -   Currently for the communication with the Google Vision API in a separate thread.

-   [SecurityConfig.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fconfigs%2FSecurityConfig.java)
    -   Setting the access authority for all endpoints.
    -   Error handling in case of missing authorities.
    -   Enabling the JwtRequestFilter for processing the JWT.
    -   Creates standard users for testing the application. (user & admin).
    > [EncoderComponent.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcomponents%2FEncoderComponent.java)
    > - Encodes the passwords for saving them in db
    
    > [JwtRequestFilter.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcomponents%2FJwtRequestFilter.java)
    > - Simply processes the JWT tokens if exists in request.
    > - All requests are going through this filter to identify the authority.


-   [WebConfig.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fconfigs%2FWebConfig.java)
    -   Resource handler
    -   Path matching
    -   CORS configuration

-   [WebSocketConfig.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fconfigs%2FWebSocketConfig.java)
    -   Stomp configuration
    -   Authentication for websockets
    -   Destination prefixes for endpoints

## Services

-   [JwtService.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fservices%2FJwtService.java)
    -   Generation of JWT
    -   Creation of JWT
    -   Validation of JWT
-   [AuthService.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fservices%2FAuthService.java)
    -   Creates JWT tokens.
    -   User authentication.
-   [ImageService.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fservices%2FImageService.java)
    -   Logic for receiving images
    -   Image storing
    -   Uses the ImageClassificationService
-   [ImgClassificationService.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fservices%2FImgClassificationService.java)
    -   Uses the Google Vision APIsimus for image classification
-   [MowerService.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fservices%2FMowerService.java)
    -   Creates a new mowing session
    -   Stores the coordinates


## Controllers

-   [AuthController.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcontrollers%2FAuthController.java)
    -   Makes use of the AuthService
    -   Auth endpoints
    -   Error handling
    > [LoginRequest.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fmodels%2FLoginRequest.java)
    > - Processes the username and password
-   [ErrorController.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcontrollers%2FErrorController.java)
    -   The real error handler
        -   Default errors
        -   Db errors
        -   Validation errors
- [ImageController.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcontrollers%2FImageController.java)
    -   Makes use of the ImageService
    -   Image endpoints
-   [MowerController.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcontrollers%2FMowerController.java)
    -   Makes use of the MowerService
    -   Broadcasts the coordinates to the subscribers of a specific mower
-   [MyControlous.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fcontrollers%2FMyControlous.java)
    -   A hello worldous endpointus

## Repository
-   [CoordinateRepo.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Frepository%2FCoordinateRepo.java)
    -   Methods for retrieving coordinate data from the db
    > [Coordinate.java](src%2Fmain%2Fjava%2Fse%2Fmow_e%2Fmodels%2FCoordinate.java) 
    > - Processes the coordinate data
    > - Creates the table

## Resources

-   [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
    -   A lot of settings for the entire application
    -   I resist to document all of them
-   [schema.sql](src%2Fmain%2Fresources%2Fschema.sql)
    -   Initializes the db tables on first application run

# Frontendus -> under construction

Dashboard that help the admin to see key information about the all project.

## Stack used

-   Vite
-   React.js
-   MUI 5

## How to launch it ?

```sh
cd frontend/
yarn install
yarn build
yarn start
```

The default port used is 3000

```sh
127.0.0.1:3000
```
