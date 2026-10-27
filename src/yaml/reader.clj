(ns yaml.reader
  (:require [flatland.ordered.set :refer [ordered-set]]
            [flatland.ordered.map :refer [ordered-map]])
  (:refer-clojure :exclude [load])
  (:import [org.yaml.snakeyaml Yaml]
           [org.yaml.snakeyaml.constructor BaseConstructor Constructor PassthroughConstructor]
           [org.yaml.snakeyaml.composer ComposerException]
           [org.yaml.snakeyaml LoaderOptions]
           [io.forward.yaml TaggedConstructor]
           [java.util.function Function]))

(def ^:dynamic *keywordize* true)
(def ^:dynamic *constructor* (fn [] (Constructor. (LoaderOptions.))))
(def passthrough-constructor
  "Custom constructor that will not barf on unknown YAML tags. This constructor
  will treat YAML objects with unknown tags with the underlying type (i.e. map,
  sequence, scalar) "
  (fn [] (PassthroughConstructor.)))

(defprotocol YAMLReader
  (decode [data]))

(defn- decode-key
  "When *keywordize* is bound to true decode map keys into keywords else leave them
  as strings. When *keywordize* is a function, calls function on the key."
  [k]
  (cond (and (true? *keywordize*) (string? k)) (keyword k)
        (fn? *keywordize*) (*keywordize* k)
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

(defn- tag-handler
  "Wrap a user tag function so it receives decoded Clojure data."
  ^Function [f]
  (reify Function
    (apply [_ value] (f (decode value)))))

(defn tagged-constructor
  "Returns a 0-arity constructor function, for use as `:constructor`, that
   passes values with the given local tags to handler functions.

   `tags` maps a tag string such as \"!money\" to a function of one argument.
   The function receives the tagged value as Clojure data (a string, vector
   or map) and its return value is used in the result.

   Options:
     :passthrough-unknown  when true, tags with no handler are read as their
                           underlying value instead of throwing. Default false."
  [tags & {:keys [passthrough-unknown]}]
  (let [handlers (into {} (map (fn [[tag f]] [(str tag) (tag-handler f)])) tags)]
    (fn []
      (TaggedConstructor. (LoaderOptions.) handlers (boolean passthrough-unknown)))))

(defn- new-yaml
  ^Yaml [constructor]
  (Yaml. ^BaseConstructor (constructor)))

(defn- resolve-constructor
  [constructor tags]
  (if (seq tags)
    (tagged-constructor tags :passthrough-unknown (identical? constructor passthrough-constructor))
    constructor))

(defn parse-documents
  "The YAML spec allows for multiple documents. This will take a string containing multiple yaml
   docs and return a vector containing each document.

   Accepts the same options as `parse-string`."
  [^String yaml-documents & {:keys [keywords constructor tags]
                             :or   {keywords    *keywordize*
                                    constructor *constructor*}}]
  (binding [*keywordize* keywords]
    (mapv decode
          (.loadAll (new-yaml (resolve-constructor constructor tags)) yaml-documents))))

(defn parse-string
  "Parse a yaml input string. If multiple documents are found it will return a vector of documents

  When keywords is a true (default), map keys are converted to keywords. When
  keywords is a function, invokes the function on the map keys.

  When a custom :constructor is provided, it's used to construct objects. Should
  be a 0-arity function that returns a constructor.

  When :tags is a map of tag string to function, values with those local tags
  are passed to the function. See `tagged-constructor`. Combining :tags with
  `:constructor passthrough-constructor` also reads any other unknown tags
  as their underlying value.
  "
  [^String string & {:keys [keywords constructor tags]
                     :or   {keywords    *keywordize*
                            constructor *constructor*}}]
  (binding [*keywordize* keywords]
    (let [constructor (resolve-constructor constructor tags)]
      (try
        (decode (.load (new-yaml constructor) string))
        (catch ComposerException _
          (mapv decode (.loadAll (new-yaml constructor) string)))))))
