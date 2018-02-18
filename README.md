# Copy Commons
[![Latest version](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/copy-commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bytemechanics/copy-commons/badge.svg)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=org.bytemechanics%3Acopy-commons)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Acopy-commons)
[![Coverage](https://sonarcloud.io/api/badges/measure?key=org.bytemechanics%3Acopy-commons&metric=coverage)](https://sonarcloud.io/dashboard/index/org.bytemechanics%3Acopy-commons)

Base project to copy sources to the other projects in order to have a single place to test sources and keep zero dependencies in libraries

## Motivation
As bytemechanics libraries has the objective to create standalone and short libraries some classes are repackaged in order to have the source centralized but avoiding classpath collisions

## Quick start
_**IMPORTANT NOTE: We strongly recommends to use as dependency only in final projects to avoid dependency hell when someone use your library/framework**_
1. First of all include the Jar file in your compile and execution classpath.
**Maven**
```Maven
	<dependency>
		<groupId>org.bytemechanics</groupId>
		<artifactId>copy-commons</artifactId>
		<version>X.X.X</version>
	</dependency>
```
**Graddle**
```Gradle
dependencies {
    compile 'org.bytemechanics:copy-commons:X.X.X'
}
```
1. Use the library you prefer, to main reference please download and look in javadoc, we intend to keep it updated and explicative


