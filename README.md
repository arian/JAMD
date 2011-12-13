JAMD - A Java JavaScript AMD builder
====================================

A Tool to optimize your [AMD JavaScript Modules](https://github.com/amdjs/amdjs-api/wiki/AMD).
It will find the dependencies and package it into one file.

Java API
--------

A very simple example of the Java API:

``` java

JAMD jamd = new JAMD();
jamd.setBaseURL("path/to/files");
Modules modules = jamd.require("App");

String JS = modules.output();

```

JavaScript Examples:
--------------------

This are examples of your source files. You can already define an ID for the
module, but that's not very useful. The dependencies argument can be relative
paths to the other modules, or set paths.

**Source/Storage.js**: Only the factory function

``` javascript
define(function(){
	var storage = {};
	return {
		store: function(key, value){
			storage[key] = value;
			return this;
		},
		retrieve: function(key){
			return storage[key];
		}
	};
});
```

**Source/App.js**: With dependencies

``` javascript
define(['Core/Utility/typeOf', './Storage.js'], function(typeOf, Storage){
	Storage.store('foo', 'bar');
	alert(storage.retrieve('foo')); // bar
});
```

After that you can write a build script.
The builder will add an ID to each `define()` function an ID so when each
`define()` is in the same file, everything continues to work. If the module
already had an ID, it will not replace it.


Notes
-----

This is not a full implementation of the AMD specification.

Some restrictions are:

- It does not execute JavaScript, so the `define` function MUST be in the literal form. It also MUST use square brackets (`[` and `]`) for dependencies.


Requirements
------------

- Java

License
-------

Just the MIT-License
