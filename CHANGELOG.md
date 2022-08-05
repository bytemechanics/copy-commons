# Version 1.8.3
---

* Add EnumerationSpliterator to easily stream Enumerations


# Version 1.8.2
---

* Fix YAMLPropertyReader close properly reader on closing returned stream
* Fix YAMLPropertyWriter close writen stream after processing


# Version 1.8.1
---

* Fix maven parent snapshot to release
* Fix apache felix configuration for the new packages


# Version 1.8.0
---

* Go to 2.2.2 maven parent
* Added MemorySeekableByteChannel to create SeekableByteChannel in memory
* Added YAMLPropertyReader to read simplified yaml files into key value pairs with key in path format (ex: first.second[1].third.withValue=my-value)
* Added YAMLPropertyWriter to write simplified yaml files from key value pairs with key in path format (ex: first.second[1].third.withValue=my-value)
* Fixed Javadoc bug on PaginatedSpliterator that can cause compilation failures with strict javadoc validation
* Fixed some tests

# Version 1.7.0
---

* Added Function replacers for Tuple class


# Version 1.6.0
---

* Added PaginatedSpliterator class to provide an spliterator capable to stream automatically populated content from any page supplier


# Version 1.5.2
---

* Fixed Make enum parameter parser able to parse non-case sensitive value #34


# Version 1.5.0
---

* Added changelog file
* Updated parent to 2.1.0 to provide test-sources
* Added org.bytemechanics.commons.tests.junit5.ArgumentsUtils to provide support to work with JUnit5 Arguments class as copiable resource
* Removed some unchecked warnings