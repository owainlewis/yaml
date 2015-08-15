(ns yaml.reader
 (:require [clojure.java.io :as io])
 (:import (org.yaml.snakeyaml Yaml)))

(def ^:dynamic *keywordize* true)

(defprotocol YAMLReader
  (decode [data]))

(defn decode-key
  "When *keywords* is bound to true decode
   map keys into keywords else leave them as strings"
  [k]
  (if *keywordize*
    (keyword k)
  k))

(extend-protocol YAMLReader
  java.util.LinkedHashMap
  (decode [data]
    (into {}
          (for [[k v] data]
            [(decode-key k) (decode v)])))

  java.util.LinkedHashSet
  (decode [data]
    (into #{} data))

  java.util.ArrayList
  (decode [data]
    (into []
      (map decode data)))

  Object
  (decode [data] data)

  nil
  (decode [data] data))

(defn parse-string
  ([string keywords]
  (binding [*keywordize* keywords]
    (parse-string string)))
  ([string]
    (decode (.load (Yaml.) string))))
