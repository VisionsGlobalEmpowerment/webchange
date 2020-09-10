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

### Datasets

- **Saves** dataset to *.edn file (new-name-dataset.edn)
    ```
    $ lein run save-dataset <course> <new-name> <dataset>
    
    # Example:
    $ lein run save-dataset test new-name concepts
    ```

- **Loads** dataset from edn to db. overrides dataset fields and items
    ```
    $ lein run load-dataset-force <course> <dataset>
    
    # Example:
    $ lein run load-dataset-force test saved-name concepts
    ```

- **Loads** dataset from edn to db. merges dataset fields and items data
    ```
    $ lein run load-dataset-merge <course> <dataset> [field1] [field2] [...]
    
    # Example:
    $ lein run load-dataset-merge test saved-name concepts word-1-skin word-2-skin
    ```

### Courses

- **Save** course from db to edn.
    ```
    $ lein run save-course <course-slug> <saved-name>
    
    # Example:
    $ lein run save-course spanish spanish
    ```
  
- **Load** course from edn to db.
    ```
    lein run load-course <course-slug> <saved-name>
    
    # Example:
    $ lein run load-course spanish spanish
    ```
  
- **Load** course from edn to db. merges course fields
    ```
    $ lein run merge-course-info <course-slug> <saved-name> [field1] [field2] [...]
    
    # Example:
    $ lein run merge-course-info spanish spanish-saved initial-scene
    ```

### Scenes

- **Save** scene `scene-name` from course `course-slug` to `course-slug` folder. 
    ```
    $ lein run save-scene <course-slug> <scene-name> 
  
    # Example:
    $ lein run save-scene spanish home
    ```

- **Save** scene `scene-name` from course `course-slug` to `saved-name` folder.
    ```
    $ lein run save-scene <course-slug> <saved-name> <scene-name>
    
    # Example:
    $ lein run save-scene spanish home-stored home
    ```
  
- **Load** `scene-name` scene from `course-slug` folder to `course-slug` course.
    ```
    $ lein run load-scene <course-slug> <scene-name>
    
    # Example:
    $ lein run load-scene spanish home
    ```

- **Load** `scene-name` scene from `saved-name` folder to `course-slug` course.
    ```
    $ lein run load-scene <course-slug> <saved-name> <scene-name>
    
    # Example:
    $ lein run load-scene spanish  home-stored home
    ```

- **Merge** scene fields from edn to db.
    ```
    lein run merge-scene-info <course-slug> <saved-name> <scene-name> [field1] [field2] [...]
    
    # Example:
    $ lein run merge-scene-info spanish home-stored home actions
    ```

### Resources

- **Load skins**
    ```
    $ lein run update-character-skins
    ```

- **Load backgrounds** Background files should be placed in `/raw/clipart`
    ```
    $ lein run update-editor-assets
    ```
