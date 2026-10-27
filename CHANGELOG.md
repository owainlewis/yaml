# Changelog

All notable changes to this project are documented here. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses [Semantic Versioning](https://semver.org/).

## [Unreleased]

### Added

- Custom tag handlers: `(parse-string s :tags {"!money" bigdec})`. Handlers receive the tagged value as Clojure data. Combine with `passthrough-constructor` to pass every other unknown tag through. Also available as `yaml.reader/tagged-constructor`. (#27)
- `yaml.core/to-file` writes data to a YAML file.
- `yaml.core/parse-documents`, and `parse-documents` now accepts the same options as `parse-string`.

### Fixed

- The multi-document fallback in `parse-string` ignored a custom `:constructor`, so multi-document input with unknown tags failed even with `passthrough-constructor`.
- The `writing-order` writer test asserted nothing. It now checks ordered map, ordered set and vector output.
- Removed reflection when constructing the SnakeYAML parser.
- Java classes are compiled with `--release 11`, so a jar built on a newer JDK still loads on Java 11.

### Changed

- SnakeYAML 2.7.
- CI on Java 11, 17 and 21 with linting, packaging and a dependency vulnerability scan. Weekly dependency freshness check. Actions are pinned by commit and kept current by Dependabot.
- Version moved to 1.1.0-SNAPSHOT because of the new features.

## [1.0.12-SNAPSHOT] - 2026-06-28

- Moved from Leiningen to the Clojure CLI and tools.build. Replaced CircleCI with GitHub Actions.
- SnakeYAML 2.6, Clojure 1.12.
- `from-file` accepts the same options as `parse-string` and returns nil for missing files.
- Invalid dumper options throw `ExceptionInfo`.
