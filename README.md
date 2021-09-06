# YAML

[![CircleCI](https://circleci.com/gh/owainlewis/yaml/tree/master.svg?style=svg)](https://circleci.com/gh/owainlewis/yaml/tree/master)
An updated YAML library for Clojure based on Snake YAML and heavily inspired by clj-yaml

## Install

### Lein

[![Clojars Project](http://clojars.org/io.forward/yaml/latest-version.svg)](http://clojars.org/io.forward/yaml)

## Usage

```clojure
(ns demo.core
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]))

;; Note on DSL
;; yaml/load & yaml/parse-string are identical
;; yaml/dump & yaml/generate-string are identical

;; Parse a YAML file

(yaml/from-file "config.yml")

;; Parse a YAML string

(yaml/parse-string "foo: bar")

;; Optionally pass `true` as a second argument to from-file or parse-string to keywordize all keys
(yaml/parse-string "foo: bar" :keywords true)

;; Parsing YAML with unknown tags
(yaml/parse-string "--- !foobar
  foo: HELLO WORLD")

;; This will parse properly
(yaml/parse-string "--- !foobar
  foo: HELLO WORLD" :constructor yaml.reader/passthrough-constructor)

;; Dump YAML

(yaml/generate-string {:foo "bar"})

;; Examples

(yaml/generate-string [{:name "John Smith", :age 33} {:name "Mary Smith", :age 27}])
;; "- {name: John Smith, age: 33}\n- {name: Mary Smith, age: 27}\n"

(yaml/parse-string "
- {name: John Smith, age: 33}
- name: Mary Smith
  age: 27
")

=> ({:name "John Smith", :age 33}
    {:name "Mary Smith", :age 27})

;; Output Formatting examples

(def data [{:name "John Smith", :age 33} {:name "Mary Smith", :age 27}])

(yaml/generate-string data)
=> - {age: 33, name: John Smith}
   - {age: 27, name: Mary Smith}

(yaml/generate-string data :dumper-options {:flow-style :flow})
=> [{age: 33, name: John Smith}, {age: 27, name: Mary Smith}]

(yaml/generate-string data :dumper-options {:flow-style :block})
=> - age: 33
     name: John Smith
   - age: 27
     name: Mary Smith

(yaml/generate-string data :dumper-options {:flow-style :flow :scalar-style :single-quoted})
=> [{'age': !!int '33', 'name': 'John Smith'}, {'age': !!int '27', 'name': 'Mary Smith'}]

Valid values for flow-style are:
- :auto
- :block
- :flow

Valid values for scalar-style are:
- :double-quoted
- :single-quoted
- :literal
- :folded
- :plain

All are documented at http://yaml.org/spec/current.html
```

This is mainly an updated version of clj-yaml with some updates

1. Updates snake YAML to latest version
2. Split reader and writer into separate protocols and files
3. Ability to read YAML from file in single function
4. Return vector [] instead of list when parsing java.util.ArrayList
5. Ability to parse multiple documents

## License

Distributed under the Eclipse Public License
