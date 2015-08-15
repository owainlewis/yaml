# YAML

A YAML library for Clojure based on Snake YAML and yaml-clj

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

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
