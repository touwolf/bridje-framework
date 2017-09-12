[![Build Status](https://travis-ci.org/bridje/bridje-framework.svg?branch=master)](https://travis-ci.org/bridje/bridje-framework)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/096fce438e94496185cbb855c0e16b67)](https://www.codacy.com/app/gilberto-vento/bridje-framework)

Introduction
============

This package contains the Bridje Java Applications Framework. A project that provides a set of Java APIs to be use in Java projects. Bridje can be compared to Spring but it´s more simplistic and lightweight. The project bring you a way of doing things that is not compromised by any JSR. The goals for this project are:

- Simple: We try to follow the KISS principle, we avoid providing too much ways of resolving the same problem. 
- Reuse: We try to reuse concepts as much as possible in the framework internals and in your apps, so they both use the same things .
- Minimal: We try to reduce the dependencies of both the framework and your application to the minimum necessary. This means that for many things we make our own implementations instead of using a third-party APIs. 
- Multipurpose: The framework is mean to be use in CLI, desktop and web apps. 
- Standalone: Your applications can run by themselves, no further server or container is needed besides the Java Virtual Machine. 
- Embedded: The APIs are designed to be embedded in your applications and not the other way around.
- Modular: You must use what you need, and in some case some unavoidable dependencies but you are not force by any means to put in your classpath any library that it´s not absolutely necessary, for the framework or your app. 
- Performance: We try to reduce to the maximum the overhead of the framework and enforce you to use good practices in your app to gain maximum performance 
- Low Memory: By reducing the amount of classes and code, and by enforcing good architectural patterns in your app the memory consumption can decrease a lot. 
- Productivity: We try to reduce to the minimum possible the code that it´s not part of your applications domain logic, without taking away from you the control that you need as a software developer over your code. 
- Robust: We try to enforce encapsulation, static typing and all the nice features that Java as a language has to offer. So not your code, nor the framework itself it´s easily broken. 
- Scalable: We try to enforce practices and patterns that cost you the same amount of hardware resources (more or less) no matter the amount of users your will handle in production.
 
All of these goals a really ambitious, and many time one or more of then need to be sacrifice in favor of others, but we think that is a matter of balance and priorities. So the framework will orbit around these principles.

See [Changelog](https://github.com/bridje/bridje-framework/blob/master/CHANGELOG.md)
See [The Framework´s Wiki](https://github.com/bridje/bridje-framework/wiki)

LICENSE
=================

The project is licensed under the Apache License:

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Installation and Usage
======================

Bridje libraries are available on the [Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent)

```xml
    <properties>
        ....
        <bridje.version>0.4.2</bridje.version>
    </properties>

    <dependencies>
	....
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-ioc</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-vfs</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-http</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-web</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-el</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-jdbc</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-orm</artifactId>
            <version>${bridje.version}</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-jfx</artifactId>
            <version>${bridje.version}</version>
        </dependency>
	....
    </dependencies>

    <build>
        <plugins>
            ....
            <plugin>
                <groupId>org.bridje</groupId>
                <artifactId>bridje-maven-plugin</artifactId>
                <version>${bridje.version}</version>
		<executions>
			<execution>
			    <id>generate-bridje-sources</id>
			    <goals>
				<goal>generate-sources</goal>
			    </goals>
			    <phase>generate-sources</phase>
			</execution>
		</executions>
		<dependencies>
			<dependency>
			    <groupId>org.bridje</groupId>
			    <artifactId>bridje-web-srcgen</artifactId>
			    <version>${bridje.version}</version>
			</dependency>
			<dependency>
			    <groupId>org.bridje</groupId>
			    <artifactId>bridje-orm-srcgen</artifactId>
			    <version>${bridje.version}</version>
			</dependency>
			<dependency>
			    <groupId>org.bridje</groupId>
			    <artifactId>bridje-jfx-srcgen</artifactId>
			    <version>${bridje.version}</version>
			</dependency>
		</dependencies>
            <plugin>
            ....
        <plugins>
     </build>
```

Dependencies
============

## Runtime Dependencies 

Your application will include the framework's librarys as well as this dependencies at runtime

 * [Netty](http://netty.io/) [4.0.34](http://netty.io/wiki/user-guide-for-4.x.html) Netty is a great network framework, the http server is build on top of it.
 * [Freemarker 2.0.23](http://freemarker.org/) The template engine used in render the views on the bridje-web framework.
 * [Java Unified Expression Language 2.2.7](http://juel.sourceforge.net/) The expression language used in bridje-web.

## Compile Dependencies 

Your application does not need to depend on this libraries unless you specifically include then

 * [Freemarker 2.0.23](http://freemarker.org/) The template engine used in the bridje-maven-plugin for generating code.
 * [JavaParser 3.2.2](http://javaparser.org/) For java source code parsing in the source generation API.

## Framework Build Dependencies

The libraries that the framework uses in the build process.

 * [Apache Maven 3.3+](https://maven.apache.org/) The framework is build with maven, it´s mean to be use with it but nothing stop you from using it from gradle, ivy, ant, or whatever build system you choose, that supports the JVM languages.
 * [Junit 4](http://junit.org/junit4/) The framework is tested with junit.
