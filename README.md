# Bridje Framework [![Build Status](https://travis-ci.org/bridje/bridje-framework.svg?branch=master)](https://travis-ci.org/bridje/bridje-framework)
www.bridje.org

We are going to build different Java APIs named Core, like inversion of control and injection of dependency Api (Bridje IoC), virtual file system Api (Bridje VFS), Sql Api (Bridje Sql), template Api (Bridje Tpl), etc.

Using the Core Api we are going to buid a framework Web and ORM to general proposite.

Bridje IoC
--

This is the main Api and the base of the other Apis, all the Api dependens of IoC. In Java you instance an **Class** using the keyword **new**, with this API you dont have to use **new** any more, it's the idea. Bridje IoC are goning to instences the Classes with you need for you.

```java
ConcreteCompoenent conComp = Ioc.context().find(ConcreteCompoenent.class);
```

ConcreteCompoenent is an **interface** and IoC Bridje resolve the implementation class for you.

You need annotate the implementation class with **@Component**

```java
@Component
public class ConcreteCompoenentImpl implements ConcreteCompoenent
{
    
}
```

Bridje VFS
--

Bridje Sql
--

Bridje Tpl
--
