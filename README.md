# README #

Kount Java SDK

### What is this repository for? ###

* Contains sources, tests, and resources for the Kount Java SDK
* SDK version: `6.5.1-SNAPSHOT`

### How do I get set up? ###

* Clone the respository
* Dependencies
    * `maven 3+`
    * `JDK/JRE 1.7+`
* How to build the SDK and run integration tests
    * setup maven
    * run `mvn clean install -DargLine="-Dkount.ris.salt='...'"` in root directory
* Setting up IDE projects
    * `eclipse` - run `mvn eclipse:eclipse` and import as existing project
    * IDEA - IDEA has automatic maven integration

### Contribution guidelines ###

* New features development / bug fixes should be implemented in their own branch having a meaningful name. Create a pull request, assign a reviewer, and respond to comments.
* Code review -- if you are the assigned reviewer, check for
    * correct usage of programming language tools
    * nice comments
    * code cleanliness (no unused imports, variables, methods)
    * code complexity
    * tests

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact

