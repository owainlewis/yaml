(ns yaml.writer-test
  (:require [clojure.test :refer :all]
            [yaml.writer :refer :all]
            [flatland.ordered.set :refer [ordered-set]]
            [flatland.ordered.map :refer [ordered-map]]))

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

(deftest preserve-namespaces
  (let [data {:foo/bar "baz"}]
    (is (= "{foo/bar: baz}\n"
           (generate-string data)))))

(deftest writing-order
  (let [om (into (ordered-map) (partition 2 (range 0 20)))
        os (into (ordered-set) (range 0 10))
        v  (into [] (range 0 10))]
    (= "0: 1\n2: 3\n4: 5\n6: 7\n8: 9\n10: 11\n12: 13\n14: 15\n16: 17\n18: 19\n"
       (generate-string om :dumper-options {:flow-style :block}))
    (= "!!set\n0: null\n1: null\n2: null\n3: null\n4: null\n5: null\n6: null\n7: null\n8: null\n9: null\n"
       (generate-string os :dumper-options {:flow-style :block}))
    (= "- 0\n- 1\n- 2\n- 3\n- 4\n- 5\n- 6\n- 7\n- 8\n- 9\n"
       (generate-string v :dumper-options {:flow-style :block}))))
