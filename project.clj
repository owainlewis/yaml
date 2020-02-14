(defproject io.forward/yaml "1.0.10"
  :description "A YAML library for Clojure"
  :url "http://github.com/owainlewis/yaml"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.yaml/snakeyaml "1.25"]
                 [org.flatland/ordered "1.5.7"]]
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases true}]]
  :java-source-paths ["src-java"])
