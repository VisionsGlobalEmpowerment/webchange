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
