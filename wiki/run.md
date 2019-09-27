# Run

- Copy unversioned `profiles.clj` file to the root of the project.

- Apply migrations:

```
$ lein migratus migrate
```

- Run back:

```
$ lein run
```

- Build front:

```
$ lein clean
$ lein cljsbuild once sw
$ lein dev
```

Figwheel will automatically push cljs changes to the browser.

- Wait a bit, then browse to [`localhost:3000`](localhost:3000).

# Manage dataset items:

In order to configure dataset edn files dir set `:dataset-dir` in profiles `env`
default value is `resources/datasets/`

```
$ export DATASET_DIR="/tmp/"
```

Set public folder path in profiles `env`:

```
:database-url "resources/public"
```

lein run save-dataset <course> <dataset> - saves dataset to *.edn file

```
$ lein run save-dataset test concepts
```

lein run load-dataset-force <course> <dataset> - loads dataset from edn to db. overrides dataset fields and items

```
$ lein run load-dataset-force test concepts
```

lein run load-dataset-merge <course> <dataset> [field1] [field2] [...] - loads dataset from edn to db. merges dataset fields and items data
```
$ lein run load-dataset-merge test concepts word-1-skin word-2-skin
```

---

### Troubleshooting

- Lein throws error:

```
/usr/bin/lein: line 125: /usr/lib/jvm/java-8-openjdk-amd64/bin/java: No such file or directory
```

Fix: open to edit file `/usr/bin/lein` and update the next piece of code:

```
# NOTE(Debian): Force Java 8 usage for performance and compatibility
# See https://salsa.debian.org/clojure-team/leiningen-clojure/issues/1
JAVA_CMD=${JAVA_CMD:-"/usr/lib/jvm/___YOUR_JAVA_VERSION__/bin/java"}
```
