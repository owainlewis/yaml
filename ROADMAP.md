# Roadmap

Maintained by the project's agents and the owner. See `AGENTS.md` for how items move.

## Now

- Release 1.1.0 to Clojars (needs the owner to tag). This also answers #40, which has asked for a non-snapshot release since 2022.
- Close the stale backlog: #41 (CircleCI is gone), #12 (ClojureScript is out of scope for a SnakeYAML wrapper), #27 once custom tags ship.

## Next

- `:loader-options` to expose SnakeYAML limits (max aliases, code point limit, duplicate keys) for parsing untrusted input.
- Streaming: `parse-stream` over a reader for large or multi-document files, returning a lazy sequence.
- Custom tags on output: write records or tagged values back with their tag.
- Move `PassthroughConstructor` out of SnakeYAML's Java package (keep the old class as a deprecated subclass).

## Later

- Benchmarks against clj-yaml, published in the README.

## Done

- 2026-10: custom tag handlers, `to-file`, multi-document fallback fix, SnakeYAML 2.7, hardened CI.
- 2026-06: Clojure CLI and tools.build, GitHub Actions, SnakeYAML 2.6.
