# YAML

![](https://travis-ci.org/owainlewis/yaml.svg?branch=master)

A YAML library for Clojure based on Snake YAML and yaml-clj

[![Clojars Project](http://clojars.org/yaml/latest-version.svg)](http://clojars.org/yaml)

## Install

### Lein

```[yaml "1.0.0"]```

### Maven

```
<dependency>
  <groupId>yaml</groupId>
  <artifactId>yaml</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

```clojure
(ns demo.core
  (:require [yaml.core :as yaml]))
  
;; Parse a YAML file

(yaml/from-file "config.yml")

;; Parse a YAML string

(yaml/parse-string "foo: bar")

;; Dump YAML

(yaml/generate-string {:foo "bar"})

```

This is mainly an updated version of yaml-clj with some updates

1. Updates snake YAML to latest version
2. Split reader and writer into separate protocols and files
3. Ability to read YAML from file in single function
4. Return vector [] instead of list when parsing java.util.ArrayList

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License 
