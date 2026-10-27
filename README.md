# yaml

[![CI](https://github.com/owainlewis/yaml/actions/workflows/ci.yml/badge.svg)](https://github.com/owainlewis/yaml/actions/workflows/ci.yml)
[![Clojars Project](https://img.shields.io/clojars/v/io.forward/yaml.svg)](https://clojars.org/io.forward/yaml)

A small, fast Clojure YAML library built on SnakeYAML 2.

- Reads YAML strings and files into Clojure data, with keyword keys by default
- Writes Clojure data back to YAML, with control over flow and scalar style
- Preserves the order of maps and sets
- Handles multi-document YAML
- Lets you register handlers for custom tags such as `!money`, or pass unknown tags through
- Safe by default: SnakeYAML 2 refuses to instantiate arbitrary Java classes from global tags

## Install

Use the version shown on the Clojars badge above.

`deps.edn`:

```clojure
io.forward/yaml {:mvn/version "VERSION"}
```

Leiningen:

```clojure
[io.forward/yaml "VERSION"]
```

Requires Java 11 or newer. See [CHANGELOG.md](CHANGELOG.md) for what is in the next release.

## Usage

```clojure
(ns demo.core
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]
            [yaml.reader :as yaml-reader]))
```

Parse a YAML string:

```clojure
(yaml/parse-string "foo: bar")
;; => {:foo "bar"}
```

Parse without keywordizing keys:

```clojure
(yaml/parse-string "foo: bar" :keywords false)
;; => {"foo" "bar"}
```

Parse with a custom key function:

```clojure
(yaml/parse-string "foo: bar" :keywords #(str "cfg/" %))
;; => {"cfg/foo" "bar"}
```

Parse a YAML file:

```clojure
(yaml/from-file "config.yml")
```

`from-file` accepts the same options as `parse-string`:

```clojure
(yaml/from-file "config.yml" :keywords false)
```

Parse multiple YAML documents:

```clojure
(yaml/parse-string "foo\n---\nbar\n...")
;; => ["foo" "bar"]
```

Parse every document explicitly, with the same options:

```clojure
(yaml/parse-documents "a: 1\n---\nb: 2" :keywords false)
;; => [{"a" 1} {"b" 2}]
```

Handle custom tags. Each handler receives the tagged value as Clojure data:

```clojure
(yaml/parse-string "price: !money 12.50\ntags: !upper [a, b]"
                   :tags {"!money" bigdec
                          "!upper" (fn [xs] (mapv clojure.string/upper-case xs))})
;; => {:price 12.50M, :tags ["A" "B"]}
```

Parse YAML with unknown tags:

```clojure
(yaml/parse-string "--- !custom-tag\nfoo: bar"
                   :constructor yaml-reader/passthrough-constructor)
;; => {:foo "bar"}
```

`:tags` and `:constructor yaml-reader/passthrough-constructor` can be combined:
registered tags go to their handler and every other tag passes through.

Generate YAML:

```clojure
(yaml/generate-string {:foo "bar"})
;; => "{foo: bar}\n"
```

Generate block-style YAML:

```clojure
(yaml/generate-string [{:name "John Smith" :age 33}
                       {:name "Mary Smith" :age 27}]
                      :dumper-options {:flow-style :block})
;; => "- age: 33\n  name: John Smith\n- age: 27\n  name: Mary Smith\n"
```

Write YAML to a file:

```clojure
(yaml/to-file "out.yml" {:foo "bar"} :dumper-options {:flow-style :block})
```

Valid `:flow-style` values:

- `:auto`
- `:block`
- `:flow`

Valid `:scalar-style` values:

- `:double-quoted`
- `:single-quoted`
- `:literal`
- `:folded`
- `:plain`

Other supported dumper options:

- `:split-lines`
- `:width`

## Notes

- Map keys are keywordized by default when they are strings.
- Non-string keys, such as numeric YAML keys, are preserved.
- Missing files return `nil` from `from-file`.
- Invalid dumper options throw `ExceptionInfo`.
- Unknown YAML tags throw by default. Use `yaml.reader/passthrough-constructor`
  when you want to keep the underlying scalar, sequence, or map value.

## Development

```sh
make test        # clojure -T:build test
make fmt-check   # clojure -M:fmt/check
make fmt         # clojure -M:fmt/fix
make jar         # clojure -T:build jar
```

See [CONTRIBUTING.md](CONTRIBUTING.md) for the full checks and the release process.

## License

Copyright © 2015-2026 Owain Lewis and contributors.

Distributed under the Eclipse Public License 1.0.
