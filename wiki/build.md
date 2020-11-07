# Production Build

```
$ lein clean
$ lein with-profile prod uberjar
```

That should compile the clojurescript code first, and then create the standalone jar.

When you run the jar you can set the port the ring server will use by setting the environment variable PORT.
If it's not set, it will run on port 3000 by default.

To deploy to heroku, first create your app:

```
$ heroku create
```

Then deploy the application:

```
$ git push heroku master
```

To compile clojurescript to javascript:

```
$ lein clean
$ lein sass once
$ lein cljsbuild once min
```
