(ns yaml.writer
  (:require [flatland.ordered.set :refer [ordered-set]]
            [flatland.ordered.map :refer [ordered-map]])
  (:import (org.yaml.snakeyaml Yaml DumperOptions DumperOptions$FlowStyle DumperOptions$ScalarStyle)))

(def flow-styles
  {:auto DumperOptions$FlowStyle/AUTO
   :block DumperOptions$FlowStyle/BLOCK
   :flow DumperOptions$FlowStyle/FLOW})

(def scalar-styles
  {:double-quoted DumperOptions$ScalarStyle/DOUBLE_QUOTED
   :single-quoted DumperOptions$ScalarStyle/SINGLE_QUOTED
   :literal DumperOptions$ScalarStyle/LITERAL
   :folded DumperOptions$ScalarStyle/FOLDED
   :plain DumperOptions$ScalarStyle/PLAIN})

(defn- make-dumper-options
  [{:keys [flow-style scalar-style]}]
  (let [options (DumperOptions.)]
    (when flow-style
      (.setDefaultFlowStyle options (flow-styles flow-style)))
    (when scalar-style
      (.setDefaultScalarStyle options (scalar-styles scalar-style)))
    options))

(defn make-yaml
  [& {:keys [dumper-options]}]
  (if dumper-options
    (Yaml. ^DumperOptions (make-dumper-options dumper-options))
    (Yaml.)))

(defn- keyword->string
  [key]
  (if (nil? (namespace key))
      (name key)
      (str (namespace key) "/" (name key))))

(defprotocol YAMLWriter
  (encode [data]))

(extend-protocol YAMLWriter
  flatland.ordered.set.OrderedSet
  (encode [data]
    (java.util.LinkedHashSet.
     ^flatland.ordered.set.OrderedSet 
     (into (ordered-set)
           (map encode data))))
  flatland.ordered.map.OrderedMap
  (encode [data]
    (java.util.LinkedHashMap.
     ^flatland.ordered.map.OrderedMap
     (into (ordered-map)
           (for [[k v] data]
             [(encode k) (encode v)]))))
  clojure.lang.IPersistentMap
  (encode [data]
    (into {}
          (for [[k v] data]
            [(encode k) (encode v)])))
  clojure.lang.IPersistentSet
    (encode [data]
      (into #{}
        (map encode data)))
  clojure.lang.IPersistentCollection
  (encode [data]
    (map encode data))
  clojure.lang.PersistentTreeMap
  (encode [data]
    (into (sorted-map)
          (for [[k v] data]
            [(encode k) (encode v)])))
  clojure.lang.Keyword
  (encode [data]
    (keyword->string data))
  Object
  (encode [data] data)
  nil
  (encode [data] data))

(defn generate-string [data & opts]
  (.dump ^Yaml (apply make-yaml opts)
         ^Object (encode data)))
