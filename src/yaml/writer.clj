(ns yaml.writer
 (:import (org.yaml.snakeyaml Yaml DumperOptions DumperOptions$FlowStyle)))

(def flow-styles
  {:auto DumperOptions$FlowStyle/AUTO
   :block DumperOptions$FlowStyle/BLOCK
   :flow DumperOptions$FlowStyle/FLOW})

(defn- make-dumper-options
  [& {:keys [flow-style]}]
  (doto (DumperOptions.)
    (.setDefaultFlowStyle (flow-styles flow-style))))

(defn make-yaml
  [& {:keys [dumper-options]}]
  (if dumper-options
    (Yaml. ^DumperOptions (apply make-dumper-options
                                 (mapcat (juxt key val)
                                         dumper-options)))
    (Yaml.)))

(defprotocol YAMLWriter
  (encode [data]))

(extend-protocol YAMLWriter

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

  clojure.lang.Keyword
  (encode [data]
    (name data))

  Object
  (encode [data] data)

  nil
  (encode [data] data))

(defn generate-string [data & opts]
  (.dump ^Yaml (apply make-yaml opts)
         ^Object (encode data)))

(def dump generate-string)
