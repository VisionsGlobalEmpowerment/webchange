# Tests

Clean:

```
$ lein clean
```

Run your tests (app should be running):

```
$ lein doo chrome-headless test once
```

or in watch mode:

```
$ lein doo chrome-headless test auto
```

Please note that [doo](https://github.com/bensu/doo) can be configured to run cljs.test in many JS environments (phantom, chrome, ie, safari, opera, slimer, node, rhino, or nashorn).
