(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as deploy]))

(def lib 'io.forward/yaml)
(def version (or (System/getenv "VERSION") "1.0.12-SNAPSHOT"))
(def class-dir "target/classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean
  "Remove build outputs."
  [_]
  (b/delete {:path "target"}))

(defn compile-java
  "Compile Java support classes."
  [_]
  (b/javac {:basis @basis
            :src-dirs ["src-java"]
            :class-dir class-dir}))

(defn test
  "Compile Java support classes and run tests."
  [_]
  (compile-java nil)
  (let [{:keys [exit]} (b/process {:command-args ["clojure" "-M:test"]})]
    (when-not (zero? exit)
      (throw (ex-info "Tests failed" {:exit exit}))))
  {:exit 0})

(defn jar
  "Build a library jar."
  [_]
  (clean nil)
  (compile-java nil)
  (b/write-pom {:basis @basis
                :class-dir class-dir
                :lib lib
                :src-dirs ["src"]
                :version version
                :scm {:url "https://github.com/owainlewis/yaml"
                      :tag (str "v" version)}})
  (b/copy-dir {:src-dirs ["src"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file})
  {:jar-file jar-file})

(defn install
  "Build and install the jar to the local Maven repository."
  [_]
  (jar nil)
  (b/install {:basis @basis
              :class-dir class-dir
              :jar-file jar-file
              :lib lib
              :version version})
  {:jar-file jar-file})

(defn deploy
  "Build and deploy the jar to Clojars.

  Requires CLOJARS_USERNAME and CLOJARS_PASSWORD in the environment."
  [_]
  (jar nil)
  (deploy/deploy {:artifact jar-file
                  :installer :remote
                  :pom-file (b/pom-path {:class-dir class-dir
                                         :lib lib})})
  {:jar-file jar-file})
