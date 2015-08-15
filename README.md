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

## License

Copyright © 2015 Owain Lewis

Distributed under the Eclipse Public License 
