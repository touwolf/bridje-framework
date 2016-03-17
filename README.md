[![Build Status](https://travis-ci.org/bridje/bridje-framework.svg?branch=master)](https://travis-ci.org/bridje/bridje-framework)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bridje/bridje-parent)

Introduction
============

This package contains the Bridje Java Applications Framework. A project that provides set of Java APIs to be use in java projects. Bridje can be compared to Spring but it´s more simplistic and lightweigh. 
The project bring you e way of doing thinks that is not compromise by any jsr. The goals for this project are:

 - Simple: We try to follow the KISS principle, We avoid providing too much ways of resolving the same problem.
 - Reuse: We try to reuse concepts as much as posible in the framework internals and in your apps, So they both use the same things
 - Minimal: We try to reduce the dependencies of both the framework and your application to the minimun necesary, This means that for many things we make our own implementations instead of using a thirdparty api.
 - Multipropouse: The framework it´s mean to be use in cli, desktop, web and mobile apps.
 - Standalone: Your application can run by themselves, no server is needed.
 - Embbeded: The APIs are designed to be embbeded in your applications and not the other way arround
 - Modular: You must use what you need, and in some case some inavoidable dependencies but you are not force by any means to put in your classpath any library that it´s not absolutly necesary, for the framework or your app.
 - Performace: We try to reduce to the maximun the overhead of the framework and enforce you to use good practices in your app to gain maximun performace
 - Low Memory: By reducing the amount of clases and code, and by enforcing good architecture patterns in your app the memory consuption can decrease a lot.
 - Productivity: We try to reduce to the minimun posible the code that it´s not part of you applications domain logic without taking away from you the control that you need as a software developer over your code.
 - Robust: We try to enforce encapsulation, static typing and all the nice features that java as a languaje has. So not your code, nor the framework it self it´s easily broken.
 - Scalable: We try to enforce practices and patterns that cost you the same amount of resources from the hardware (more or less) no matter the amount of users your will handle in production
 
All of thees goals a really ambitious, and many time one or more of then need to be sacrifice in favor of others, but we think that is a matter of balance and priorities. So the framework will orbit arround thees principles.

See [Changelog](https://github.com/bridje/bridje-framework/blob/master/CHANGELOG.md)

Dependencies
============

- [Netty](http://netty.io/) [4.0.34](http://netty.io/wiki/user-guide-for-4.x.html)

Installation and Usage
======================

Bridje libraries are availables on the Maven Central Repository

    <dependencies>
		....
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-ioc</artifactId>
            <version>0.1.4-3</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-cfg</artifactId>
            <version>0.1.4-3</version>
        </dependency>
        <dependency>
            <groupId>org.bridje</groupId>
            <artifactId>bridje-http</artifactId>
            <version>0.1.4-3</version>
        </dependency>
		....
    </dependencies>

About the Project
=================

The project provides a set of tools for creating Java Applications.

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

