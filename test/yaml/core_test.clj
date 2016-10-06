(ns yaml.core-test
  (:require [clojure.test :refer :all]
            [yaml.core :as yaml]))

(deftest from-file-test
  (testing "should parse a single document"
    (let [yml (yaml/from-file "test/fixtures/petstore.yaml" true)]
      (is (= (:schemes yml) ["http"]))))
  (testing "should parse multiple documents"
    (let [yml (yaml/from-file "test/fixtures/multi.yaml" true)]
      (is (vector? yml)))))
