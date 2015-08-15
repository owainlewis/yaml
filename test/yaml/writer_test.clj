(ns yaml.writer-test
  (:require [clojure.test :refer :all]
            [yaml.writer :refer :all]))

(deftest dump-opts
  (let [data [{:age 33 :name "jon"} {:age 44 :name "boo"}]]
    (is (= "- age: 33\n  name: jon\n- age: 44\n  name: boo\n"
           (generate-string data :dumper-options {:flow-style :block})))
    (is (= "[{age: 33, name: jon}, {age: 44, name: boo}]\n"
           (generate-string data :dumper-options {:flow-style :flow})))))
