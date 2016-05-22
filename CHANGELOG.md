# Change Log

## [v0.2.2](https://github.com/bridje/bridje-framework/tree/v0.2.2) (2016-05-22)
[Full Changelog](https://github.com/bridje/bridje-framework/compare/v0.2.1...v0.2.2)

- #40 The root handler of the HTTP server now has the Integer.MIN_VALUE priority.

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
