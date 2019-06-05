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
$ lein dev
```

Figwheel will automatically push cljs changes to the browser.

- Wait a bit, then browse to [`0.0.0.0:3000`](0.0.0.0:3000).

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
