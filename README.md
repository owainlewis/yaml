# yaml

[![test](https://github.com/owainlewis/yaml/actions/workflows/test.yml/badge.svg)](https://github.com/owainlewis/yaml/actions/workflows/test.yml)

A small Clojure YAML library built on SnakeYAML.

It reads YAML strings and files into Clojure data, writes Clojure data back to
YAML, preserves ordered maps and sets, supports multi-document YAML, and can
optionally pass through unknown tags.

## Install

`deps.edn`:

```clojure
io.forward/yaml {:mvn/version "1.0.12-SNAPSHOT"}
```

Leiningen consumers can still depend on the Maven artifact:

```clojure
[io.forward/yaml "1.0.12-SNAPSHOT"]
```

The current source version is `1.0.12-SNAPSHOT`.

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

Parse YAML with unknown tags:

```clojure
(yaml/parse-string "--- !custom-tag\nfoo: bar"
                   :constructor yaml-reader/passthrough-constructor)
;; => {:foo "bar"}
```

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

Run tests:

```sh
clojure -T:build test
```

Check formatting:

```sh
clojure -M:fmt/check
```

Format code:

```sh
clojure -M:fmt/fix
```

Build a jar:

```sh
clojure -T:build jar
```

Install locally:

```sh
clojure -T:build install
```

Deploy to Clojars:

```sh
CLOJARS_USERNAME=... CLOJARS_PASSWORD=... clojure -T:build deploy
```

Release from GitHub Actions:

1. Set `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` as repository secrets.
2. Create and push a version tag.

```sh
git tag v1.0.12
git push origin v1.0.12
```

The release workflow uses the tag without the leading `v` as the artifact
version.

## CI

GitHub Actions runs:

- `clojure -M:fmt/check`
- `clojure -T:build test`
- `clojure -T:build jar`

CircleCI has been removed.

Leiningen has been replaced by the Clojure CLI, `deps.edn`, and `tools.build`.

## License

Eclipse Public License 1.0.
