.PHONY: test
test:
	clojure -T:build test

.PHONY: fmt
fmt:
	clojure -M:fmt/fix

.PHONY: fmt-check
fmt-check:
	clojure -M:fmt/check

.PHONY: jar
jar:
	clojure -T:build jar

.PHONY: tag
tag:
	@test -n "$(VERSION)" || (echo "VERSION is required, for example VERSION=1.0.12 make tag" && exit 1)
	git tag -a v$(VERSION) -m "Release $(VERSION)"
	git push origin v$(VERSION)

.PHONY: deploy
deploy:
	clojure -T:build deploy

.PHONY: release
release:
	test jar tag
