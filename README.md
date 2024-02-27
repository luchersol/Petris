# React petris
Small project based on spring-petris for teaching SPA architectures with react, java and spring developer for teaching DP1 at the Software Engineering degree of University of Sevilla.

This is a fork of https://github.com/spring-projects/spring-petris  The main changes that have been performed were:
- Trimming several parts of the application to keep the example low
- Reorganize some parts of the code according to best practices introduced in the course
- Modifying the Controllers to work as RestControllers creating several API endpoings
- Modifying the security configuration to use JWT
- Creating a React frontend.

## Understanding the Spring petris application  backend with a few diagrams
<a href="https://speakerdeck.com/michaelisvy/spring-petris-sample-application">See the presentation here</a>

## Running petris backend locally
petris is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/gii-is-DP1/spring-petris.git
cd spring-petris
./mvnw package
java -jar target/*.jar
```

You can then access petris backend here: [http://localhost:8080/](http://localhost:8080/swagger-ui/index.html)



Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```
## Database configuration

In its default configuration, petris uses an in-memory database (H2) which
gets populated at startup with data. The INSERTs are specified in the file data.sql.

## Working with React petris in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 17 or newer.
* Node.js 18 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
```
git clone https://github.com/gii-is-DP1/spring-petris.git
```
2) Inside Eclipse or STS
```
File -> Import -> Maven -> Existing Maven project
```

Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA

In the main menu, choose `File -> Open` and select the petris [pom.xml](pom.xml). Click on the `Open` button.

CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources`
or right click on the `spring-petris` project then `Maven -> Generates sources and Update Folders`.

A run configuration named `petrisApplication` should have been created for you if you're using a recent Ultimate
version. Otherwise, run the application by right clicking on the `petrisApplication` main class and choosing
`Run 'petrisApplication'`.

4) Navigate to petris
Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser.


## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [petrisApplication](https://github.com/gii-is-DP1/spring-petris/blob/master/src/main/java/org/springframework/samples/petris/petrisApplication.java) |
|Properties Files | [application.properties](https://github.com/gii-is-DP1/spring-petris/blob/master/src/main/resources) |


## Starting the frontend

The Spring petris is implemented with a React frontend in the folder named "frontend".
You can start the development server to see frontend using the command (maybe you should use the command npm insall prior to this):
```
npm start
```
You can then access the petris frontend at [http://localhost:3000](http://localhost:3000)
