(ns yaml.core-test
  (:require [clojure.test :refer :all]
            [yaml.core :refer :all]))

(deftest from-file-test
  (let [yaml (from-file "test/fixtures/petstore.yaml" true)]
    (is (= (:schemes yaml) ["http"]))))

