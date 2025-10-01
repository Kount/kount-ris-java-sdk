# Kount RIS JAVA  SDK #

The Kount RIS JAVA  SDK contains the JAVA  SDK, tests, and build/package routines. This enables integrating the Kount fraud fighting solution into your JAVA  app.

## Documentation ##

For official **integration documentation**, go to [How to Integrate the RIS JAVA  SDK](https://developer.kount.com/hc/en-us/articles/4418705623316) on the Kount Developer site.

## Release Notes ##

For the complete **release notes history**, go to [Kount RIS JAVA  SDK Release Notes History](https://developer.kount.com/hc/en-us/articles/10404690838676) on the Kount Developer site.

## SDK Supported Versions ##
The Kount RIS JAVA  SDK supports the following versions of Java:
* Java 8
* Java 11
* Java 17
* Java 21
* Java 25

This support is available via MRJAR that was introduced in Java 9. The SDK needs to be built with Java 8 JDK version to ensure compatibility with Java 8.
You will also need to create a toolchains.xml file to point to the different JDK versions you have installed on your system.

It should look something like this and be located at ~/.m2/toolchains.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
  <toolchains>
  
  <toolchain>
  <type>jdk</type>
  <provides>
  <version>8</version>
  </provides>
  <configuration>
  <jdkHome>~/jdk8</jdkHome>
  </configuration>
  </toolchain>
  
  <toolchain>
  <type>jdk</type>
  <provides>
  <version>17</version>
  </provides>
  <configuration>
  <jdkHome>~/jdk17</jdkHome>
  </configuration>
  </toolchain>
  
  <toolchain>
  <type>jdk</type>
  <provides>
  <version>21</version>
  </provides>
  <configuration>
  <jdkHome>~/jdk21</jdkHome>
  </configuration>
  </toolchain>
  
  <toolchain>
  <type>jdk</type>
  <provides>
  <version>25</version>
  </provides>
  <configuration>
  <jdkHome>~/jdk25</jdkHome>
  </configuration>
  </toolchain>
      
</toolchains>
```