(ns yaml.writer-test
  (:require [clojure.test :refer :all]
            [yaml.writer :refer :all]))

(deftest dump-opts
  (let [data [{:age 33 :name "jon"} {:age 44 :name "boo"}]]

    (is (= "- age: 33\n  name: jon\n- age: 44\n  name: boo\n"
           (generate-string data :dumper-options {:flow-style :block})))
    (is (= "[{age: 33, name: jon}, {age: 44, name: boo}]\n"
           (generate-string data :dumper-options {:flow-style :flow})))

    (is (= "- \"age\": !!int \"33\"\n  \"name\": \"jon\"\n- \"age\": !!int \"44\"\n  \"name\": \"boo\"\n"
           (generate-string data :dumper-options {:scalar-style :double-quoted})))
    (is (= "- 'age': !!int '33'\n  'name': 'jon'\n- 'age': !!int '44'\n  'name': 'boo'\n"
           (generate-string data :dumper-options {:scalar-style :single-quoted})))
    (is (= "- \"age\": !!int |-\n    33\n  \"name\": |-\n    jon\n- \"age\": !!int |-\n    44\n  \"name\": |-\n    boo\n"
           (generate-string data :dumper-options {:scalar-style :literal})))
    (is (= "- \"age\": !!int >-\n    33\n  \"name\": >-\n    jon\n- \"age\": !!int >-\n    44\n  \"name\": >-\n    boo\n"
           (generate-string data :dumper-options {:scalar-style :folded})))
    (is (= "- {age: 33, name: jon}\n- {age: 44, name: boo}\n"
           (generate-string data :dumper-options {:scalar-style :plain})))))
