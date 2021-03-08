# Auto dialog generation

First, you need to install development dependencies:
Download:

```
$ wget http://host.webchange.com/dev-gems/mvn_repo.zip
```

Unzip folder, and add to your development dependencies in "project.clj":
1. to `:dependencies` add [de.dfki.mary/voice-cmu-rms-hsmm "5.2"]
2. to `:repositories` add ["local" {:url "file:native/mvn_repo"}]
as a result you shoould get like
```
:dependencies [[binaryage/devtools "0.9.10"]
              [day8.re-frame/re-frame-10x "0.3.3"]
              [day8.re-frame/tracing "0.5.1"]
              [ring/ring-mock "0.3.2"]
              [mockery "0.1.4"]
              [figwheel-sidecar "0.5.19"]
              [cider/piggieback "0.5.1"]
              [de.dfki.mary/voice-cmu-rms-hsmm "5.2"]
              ]
:repositories [["local" {:url "file:native/mvn_repo"}]]
```
Run `lein deps` to update dependencies.

To generate dialogs, go to file `env/dev/clj/webchange/tts.clj` load it to repl and call 
```
(process-audio "sdf-sdf-kvwevntu" "sdf")
```