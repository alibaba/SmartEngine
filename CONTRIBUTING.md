# Contributing

This repository accepts contributions through pull requests to `master`.
`dev` can be used as a temporary integration branch, but changes should be merged in small, reviewable batches.

## Prerequisites

- JDK 8+
- Maven 3.6+
- MySQL/PostgreSQL only when running database integration tests

## Build and Test

Run at repository root:

```bash
mvn -q -DskipTests=false test
```

Module-level examples:

```bash
mvn -pl core -am test
mvn -pl extension/storage/storage-custom -am test
mvn -pl extension/storage/storage-mysql -am test
```

Reference: `docs/07-dev/build-and-test.md`

## Pull Request Requirements

- Keep each PR focused on one theme.
- Use the PR template completely, including risk and rollback notes.
- Add or update tests for behavior changes.
- Update docs for user-visible changes.
- Update `CHANGELOG.md` and `CHANGELOG-zh.md` for release-relevant changes.

For database-related changes:

- Update both MySQL and PostgreSQL DDL.
- Update MyBatis SQL maps.
- Provide migration notes.

## Recommended Merge Strategy for Large Branches

If a branch is too large to review in one PR, split into these categories:

1. Mechanical changes (format/move/rename).
2. Build/dependency changes.
3. Feature additions.
4. Compatibility or semantic behavior changes.

Each category should be merged only after CI passes.

## Commit Message

Use present tense and clearly describe the behavior change, not just the action taken.
