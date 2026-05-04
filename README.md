# Fribok

This repository contains a (slightly) modernized build of Fribok 2.3-SNAPSHOT based on the SourceForge SVN source.

But why, one may ask? Well, I was thrilled to find out the newly released Fribok 2.3-SNAPSHOT ran perfectly fine even on more recent java versions. However, I could not get the most recent SVN rev 244 to build on my system, and I wanted to fix that.
 
## Build requirements

- JDK 21
- Apache Maven 3.9.x

## Build

mvn clean package assembly:single

## Run

java -jar target/fribok-2.3-SNAPSHOT-jar-with-dependencies.jar

## Notes

This version includes build-system fixes for Java 21 and bundles required font resources directly so printing works without manually installing local Maven artifacts.
