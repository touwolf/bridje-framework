[![Build Status](https://travis-ci.org/bridje/bridje-framework.svg?branch=master)](https://travis-ci.org/bridje/bridje-framework)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/096fce438e94496185cbb855c0e16b67)](https://www.codacy.com/app/gilberto-vento/bridje-framework)

![alt tag](https://raw.githubusercontent.com/bridje/bridje-framework/master/bridje-logo.svg)

Introduction
============

This package contains the Bridje Java Applications Framework. A project that provides a set of Java APIs to be use in Java projects. Bridje can be compared to Spring but it´s more simplistic and lightweight. The project bring you a way of doing thinks that is not compromised by any JSR. The goals for this project are:

- Simple: We try to follow the KISS principle, we avoid providing too much ways of resolving the same problem. 
- Reuse: We try to reuse concepts as much as possible in the framework internals and in your apps, so they both use the same things .
- Minimal: We try to reduce the dependencies of both the framework and your application to the minimum necessary. This means that for many things we make our own implementations instead of using a third-party APIs. 
- Multipurpose: The framework is mean to be use in CLI, desktop, web and mobile apps. 
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

Bridje libraries are availables on the [Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent)

    <dependencies>
		....
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-ioc</artifactId>
            <version>0.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-cfg</artifactId>
            <version>0.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-http</artifactId>
            <version>0.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-jdbc</artifactId>
            <version>0.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-orm</artifactId>
            <version>0.1.7</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-jfx</artifactId>
            <version>0.1.7</version>
        </dependency>
		....
    </dependencies>

Dependencies
============

[Netty](http://netty.io/) [4.0.34](http://netty.io/wiki/user-guide-for-4.x.html) is for now the only dependency the framework has.
