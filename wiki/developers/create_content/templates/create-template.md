# Create Template

Ready templates are located in the folder [/src/clj/webchange/templates/library](/src/clj/webchange/templates/library).

Each template must be registered by calling the function `register-template` from webchange.templates.core namespace. Example is below:

```
(ns webchange.templates.library.pinata
  (:require
    [webchange.templates.core :as core]))
    
(def metadata {...})

(def template {...})

(defn create
  [template args]
  ;; Generate activity code here..
  )
    
(core/register-template
  (:id metadata)
  metadata
  (partial create template))
```

In the example above, the following elements are present:

- metadata - technical data describing template,
- template - the activity template we want to create,
- create - function that takes our template and parameters from the creator, and returns the ready activity code,
- apply currying to the `create` function to get the template when called as the first parameter,

  then register the template by calling the `register-template` function.

## `metadata`

Metadata - object that should contain the following fields:

- `:id` - numeric template id, must be unique among other templates
- `:name` - template name,
- `:description` - short description of the mechanics and skills to train,
- `:lesson-sets` - list of lesson-sets names that are used in the template,
- `:fields` - list of concept fields that are used in the template,
- `:options` - possible content variations that can be configured when creating an activity from the template.

  Options can be of the following types:
  - **"string"** - simple string value.
  - **"lookup"** - choose one of several predefined values. Described by the following fields:
    - :label - option title,
    - :options - list of available values. Each value is described by two fields:
      - :name - value title,
      - :value - actual value.
  - **"characters"** - select a character from the list of available ones. Described by the following fields:
    - :label - option title,
    - :max - maximum number of characters.

  - **"pages"** - specific type for Book activity. Described by the following fields:
    - :label - option title,
    - :max - maximum number of pages.

    Each page will contain fields:
      - :text - page text,
      - :img - src of an image uploaded by user.


Example metadata for Casa template:

```
{:id          1
 :name        "casa"
 :description "Some description of casa mechanics and covered skills"
 :lesson-sets ["concepts"]
 :fields      [{:name "image-src",
                :type "image"}]
 :options     {:characters {:label "Characters"
                            :type  "characters"
                            :max   3}
               :boxes      {:label   "Number of boxes"
                            :type    "lookup"
                            :options [{:name "1" :value 1}
                                      {:name "2" :value 2}
                                      {:name "3" :value 3}]}}}
```


## `template`

The template must be an object that contains not customizable activity's data.
Consists of the following fields:
- :assets - descriptions of used static resources,
- :objects - description of scene components,
- :scene-objects - list of component names to be rendered,
- :actions - all scene actions,
- :triggers - events and corresponding actions.

## `create`

This is the function to which the above `template` is passed as the first parameter,
and second - user-filled `options`, described in `metadata`.
Next, the function must generate and return a ready activity code.

---

[← Back to index](../../index.md)
