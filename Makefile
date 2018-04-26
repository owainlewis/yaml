VERSION := $(shell head -1 project.clj | cut -d " " -f 3 | tr -d '"')

PHONY: test
test:
	lein test

.PHONY: tag
tag:
	git tag -a $(VERSION) -m "Release $(VERSION)"
	git push origin $(VERSION)

.PHONY: deploy
deploy:
	@GPG_TTY=$(tty) lein deploy clojars

.PHONY: release
release:
	deploy tag
