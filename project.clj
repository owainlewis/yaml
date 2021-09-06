(defproject io.forward/yaml "1.0.11-SNAPSHOT"
  :description "A YAML library for Clojure"
  :url "http://github.com/owainlewis/yaml"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.yaml/snakeyaml "1.29"]
                 [org.flatland/ordered "1.5.7"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases false}]]
  :java-source-paths ["src-java"]
  :javac-options ["-target" "1.7", "-source" "1.7"])
