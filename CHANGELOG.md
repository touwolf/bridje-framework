# Change Log

## [v0.4.1](https://github.com/bridje/bridje-framework/tree/v0.4.0) (2017-07-24)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.3.0...v0.4.0)

 

## [v0.4.0](https://github.com/bridje/bridje-framework/tree/v0.4.0) (2016-06-01)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.3.0...v0.4.0)

 - A brand new VFS API.
 - A brand new source code generation system in java with maven dependencies to the plugin.
 - A brand new ORM source code generation system a features.
 - A brand new WEB source code generation system a features.
 - A brand new JavaFX API with tools and source code generation for desktop applications.

## [v0.3.0](https://github.com/bridje/bridje-framework/tree/v0.3.0) (2016-11-23)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.2.3...v0.3.0)

 - The web framework now has layouts along with web themes generation, also several enhances were made to the framework logic to asure is fast and robust.
 - The derby dialet in the ORM was fix, now it can be use.
 - A reconnect test was added to the JDBC API, to avoid expired connections.
 - Adding a complete mime type list to be use in the VFS and the Web frameworks.
 - A new WebComponent annotation now replace the old Component(WebScope) sintax.
 - Several enhances were made to the VFS, create, read, update, and delete virtual files and folders are now more intuitive, the travel methods were remove and replace by a better glob expression matching in the find methods.
 - The Ioc now supports multiple components listing files.
 - New concepts were added to the ORM like custom data types, adapters, operations, etc... along with the code generation the ORM is now more powerfull and easy to use.
 - The maven plugin was reformed to allow xml data parsing for the generators in groovy inside each module, along with freemarker this is now a powerfull way of generating code in general.
 - The bridje-wui project was removed, and replace by a new way of defining themes, by generating code, bridje-web now support web themes generation, this feature allow users to entirely customize the way the application will look.
 - The juel integration for the web views was moved to a new project, bridje-el.

## [v0.2.3](https://github.com/bridje/bridje-framework/tree/v0.2.3) (2016-07-29)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.2.2...v0.2.3)

 - Enhances to the mecanism that use the maven plugin to generate code.
 - Enhances to the ORM code generation. 		
 - Adding initial version of webui api (Not released yet).
 - Adding SQL Adapters for PostgreSQL and Derby (Java DB).
 - Fixes to the VFS, to allow files creation and writing. 		
 - Web framework enhaces: web view reference, request path reference.
 - Adding get data method to the web scope.
 - Adding Thread Local Storage into the IoC framework.
 - Adding meta tags to the web views.
 - Refactoring web views.
 - Web view meta data feature.
 - ORM code generation templates.
 - VFS null pointer error.
 - HTTP request's size limit increased.
 - Implementing web session in the web api.
 - Add patch method to the HTTP server.

## [v0.2.2](https://github.com/bridje/bridje-framework/tree/v0.2.2) (2016-05-22)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.2.1...v0.2.2)

- #43 Glob syntax was added for bridje-vfs
- #42 bridje-web now supports cookies injection.
- #41 bridje-web now supports post and get parameters injection.
- #30 Support for prostgres and derby was added.
- Enhances to the web framework views feature.

## [v0.2.1](https://github.com/bridje/bridje-framework/tree/v0.2.1) (2016-05-06)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.2.0...v0.2.1)

- #40 The root handler of the HTTP server now has the Integer.MIN_VALUE priority.
- #39 The VFS now has some helper methods to easily mount class path resources and file system folders.
- #33 The maven plugin now has the functionality to load templates from the projects class path.
- #31 The HTTP server now supports web sockets.
- #29 The initial version for the web framework is now available.
- #28 The VFS now supports Files readers, that allows you to read a file by specifiying the result class.
- #27 FIXED java.lang.NullPointerException in the VFS API bug
- #26 FIXED The IocContext is not being injected.

## [v0.2.0](https://github.com/bridje/bridje-framework/tree/v0.2.0) (2016-04-23)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.7...v0.2.0)

- #23 The ioc scope concept was remaked, now scopes are clases that extends from Scope interface.
- #22 Varios performace fixes where made to the bridje ioc api, including components meta data cache to decrease memory consuption and increase scalability.
- #21 A new source code generation maven plugin is available now.
- #20 Several fixes to the architecture and performace of the orm framework were made.
- #19 A new API is now available, the VFS api serves as a common interface for accesing applications files.
- The bridje-cfg api was removed in favor of the new bridje vfs api.
- The bridje-jfx api was removed as the current version is no usefull and it needs to be rethink with a better aproach.

## [v0.1.7](https://github.com/bridje/bridje-framework/tree/v0.1.7) (2016-04-09)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.6...v0.1.7)

- Releasing JavaFX Docking Framework.

## [v0.1.6](https://github.com/bridje/bridje-framework/tree/v0.1.6) (2016-04-09)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.5-1...v0.1.6)

- #18 JavaFX Docking Framework.
- #17 Support for joins and relations in the orm framework.
- #16 HTTP server, support for html forms, file upload, query strings and cookies.
- #15 Bridje CFG dinamic config repositories, PropConfigAdapter, multiple configuration adapters, Configuration annotation optional.
- #14 JDBC service max and min connections.

## [v0.1.5-1](https://github.com/bridje/bridje-framework/tree/v0.1.5-1) (2016-03-25)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.4-3...v0.1.5-1)

- HTTP server SSL configuration.
- First version of Bridje ORM API.

## [v0.1.4-3](https://github.com/bridje/bridje-framework/tree/v0.1.4-3) (2016-03-14)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.3...v0.1.4-3)

- Adding configuration contextxs to the Bridje Cfg API
- First version of Bridje JDBC API

## [v0.1.3](https://github.com/bridje/bridje-framework/tree/v0.1.3) (2016-03-01)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.1.2...v0.1.3)

- IoC should now create services out of base clases's interfaces [225c6944bbc622f365fd4d480a5e43ae4e2f9375](https://github.com/bridje/bridje-framework/commit/225c6944bbc622f365fd4d480a5e43ae4e2f9375)
- First version of Configuration API [bridje-cfg v0.1.3](https://github.com/bridje/bridje-framework/tree/v0.1.3/bridje-cfg)
- First version of Netty base HTTP Server [bridje-http v0.1.3](https://github.com/bridje/bridje-framework/tree/v0.1.3/bridje-http)
- Maven XSD plugin removed [fcd6dad09d660782cd56fad125e61cd1197d143e](https://github.com/bridje/bridje-framework/commit/fcd6dad09d660782cd56fad125e61cd1197d143e)
