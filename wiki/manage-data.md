# Manage dataset items:

In order to configure dataset edn files dir set `:dataset-dir` in profiles `env`
default value is `resources/datasets/`

```
$ export DATASET_DIR="/tmp/"
```

Set public and upload folders path in profiles `env`:

```
:public-dir "resources/public"
:upload-dir "resources/public/upload"
```

lein run save-dataset <course> <new-name> <dataset> - saves dataset to *.edn file (new-name-dataset.edn)

```
$ lein run save-dataset test new-name concepts
```

lein run load-dataset-force <course> <dataset> - loads dataset from edn to db. overrides dataset fields and items

```
$ lein run load-dataset-force test saved-name concepts
```

lein run load-dataset-merge <course> <dataset> [field1] [field2] [...] - loads dataset from edn to db. merges dataset fields and items data

```
$ lein run load-dataset-merge test saved-name concepts word-1-skin word-2-skin
```

lein run save-course <course-slug> <saved-name> - save course from db to edn.

```
$ lein run save-course spanish spanish
```

lein run load-course <course-slug> <saved-name> - load course from edn to db.

```
$ lein run load-course spanish spanish
```

lein run merge-course-info <course-slug> <saved-name> [field1] [field2] [...] - load course from edn to db. merges course fields

```
$ lein run merge-course-info spanish spanish-saved initial-scene
```

lein run save-scene <course-slug> <saved-name> <scene-name> - save scene from db to edn.

```
$ lein run save-scene spanish home-stored home
```

lein run load-scene <course-slug> <saved-name> <scene-name> - load scene from edn to db.

```
$ lein run load-scene spanish home-stored home
```

lein run merge-scene-info <course-slug> <saved-name> <scene-name> [field1] [field2] [...] - load scene from edn to db. merges scene fields

```
$ lein run merge-scene-info spanish home-stored home actions
```
