(ns yaml.reader
 (:require [flatland.ordered.set :refer [ordered-set]]
           [flatland.ordered.map :refer [ordered-map]])
 (:refer-clojure :exclude [load])
 (:import [org.yaml.snakeyaml Yaml]
          [org.yaml.snakeyaml.composer ComposerException]))

(def ^:dynamic *keywordize* true)

(defprotocol YAMLReader
  (decode [data]))

(defn- decode-key
  "When *keywordize* is bound to true decode map keys into keywords else leaave them
  as strings. When *keywordize* is a function, calls function on the key."
  [k]
  (cond (true? *keywordize*)
        (keyword k)

        (fn? *keywordize*)
        (*keywordize* k)

        :else k))

(extend-protocol YAMLReader
  java.util.LinkedHashMap
  (decode [data]
    (into (ordered-map)
          (for [[k v] data]
            [(decode-key k) (decode v)])))
  java.util.LinkedHashSet
  (decode [data]
    (into (ordered-set) data))
  java.util.ArrayList
  (decode [data]
    (into []
      (map decode data)))
  Object
  (decode [data] data)
  nil
  (decode [data] data))

(defn parse-documents
  "The YAML spec allows for multiple documents. This will take a string containing multiple yaml
   docs and return a vector containing each document"
  [^String yaml-documents]
  (mapv decode
    (.loadAll (Yaml.) yaml-documents)))

(defn parse-string
  "Parse a yaml input string. If multiple documents are found it will return a vector of documents

  When keywords is a true (default), map keys are converted to keywords. When
  keywords is a function, invokes the function on the map keys.
  "
  ([^String string keywords]
  (binding [*keywordize* keywords]
    (parse-string string)))
  ([^String string]
   (try
     (decode (.load (Yaml.) string))
   (catch ComposerException e
     (parse-documents string)))))
