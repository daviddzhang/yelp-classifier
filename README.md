# Yelp Classifier

This project uses a Soar agent and the Naive Bayes model to predict whether the user will find a restaurant good, okay, or bad based on training. Users can search for restaurants they have been to using keywords and specific locations to query the Yelp API for a business search. They can then select the restaurant and provide whether it was good, okay, or bad. They can follow a similar process to query the system for a classification on the restaurant. The user can save and load at any time.

Currently, the features that the model uses are *category, price, review count, and rating*.

This project makes use of [Soar Reasoning Models](https://github.com/daviddzhang/soar-reasoning-models).

## Getting Started

These instructions will explain how to run the application and the tests. This project is only compatible with **Java 11** and above.

### Soar Reasoning Models

As mentioned above, this application requires Soar Reasoning Models to run. Download the latest jar [here](https://github.com/daviddzhang/soar-reasoning-models/releases). Ensure that the name of the jar is **"SoarReasoningModels"**. You should install this jar into Maven via the following command while in the project directory (with the appropriate fields filled out):

`mvn install:install-file -Dfile=<path-to-file> -DgroupId=com.davidzhang -DartifactId=SoarReasoningModels -Dversion=<version> -Dpackaging=jar`

### The API Key

Before compiling, you should enter your API Key into the application. In `src/main/java/yelpclassifier/yelpquery/HttpClientQuery.java`, there is a field, `API_KEY`, for an API Key at the top of the class. Set this field to be your Yelp API Key.

### Compiling

Assuming Maven is properly installed (see link below if not) and you are in the correct working directory, to compile the project after it has been downloaded, run the command `mvn compile`.

### Tests

**Mac**

To run the tests, simply use Maven's test lifecycle via the following command `mvn test`.

**Windows**

To run the tests, use the Maven surefire plugin's `test` goal. In the command line, enter `mvn surefire:test@windows`.

### Running the Application

**Mac**

Simply use Maven exec plugin's exec goal via the following command on the command line: `mvn exec:exec`.

**Windows**

Before running the main class, set a run configuration that has the following environmental variable:
* Name = `PATH`
* Value = `/path/to/this/project/src/main/resources`

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Soar](https://soar.eecs.umich.edu/) - Cognitive Architecture
* [Yelp API](https://www.yelp.com/developers/documentation/v3)
