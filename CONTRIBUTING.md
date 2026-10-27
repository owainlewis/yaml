# Contributing

## Local checks

Install Java 11 or newer, the Clojure CLI and clj-kondo.

```bash
clojure -T:build test
clojure -M:fmt/check
clj-kondo --lint src test --fail-level error
clojure -T:build jar
```

`clojure -T:build test` compiles the Java classes in `src-java` before running the tests.

## Pull requests

- Keep the public API of `yaml.core`, `yaml.reader` and `yaml.writer` backwards compatible unless the change is planned major-version work.
- Add a test for every bug fix and feature.
- Add an entry under `[Unreleased]` in `CHANGELOG.md`.
- Do not weaken SnakeYAML's safe defaults. Loading arbitrary Java types from YAML tags must stay off.

## Releases

1. Move the `[Unreleased]` changelog entries under the new version and set the default version in `build.clj`.
2. Merge the release commit.
3. Push a matching tag: `git tag v1.1.0 && git push origin v1.1.0`.
4. The release workflow tests, builds and deploys to Clojars using the tag as the version. It needs the `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` repository secrets.
