(ns webchange.templates.library.categorize-synonyms.template
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.merge :as utils-merge]
    [webchange.templates.library.categorize-synonyms.round-0 :refer [template-round-0]]
    [webchange.templates.library.categorize-synonyms.round-1 :refer [template-round-1]]
    [webchange.templates.library.categorize-synonyms.round-2 :refer [template-round-2]]
    [webchange.templates.library.categorize-synonyms.round-3 :refer [template-round-3]]
    [webchange.templates.core :as core]))

(def m {:id          29
        :name        "Categorize synonyms - 3 rounds"
        :tags        ["Independent Practice"]
        :description "Categorize"})

(defn prepare-templates
  []
  (let [
        pt0 (utils-merge/prepare-template template-round-0 "r0" [:target] [] [])
        pt (utils-merge/prepare-template template-round-1 "r1" [:say-item :target] [] [])
        pt1 (utils-merge/prepare-template template-round-2 "r2" [:say-item :target :box] [] [])
        pt2 (utils-merge/prepare-template template-round-3 "r3" [:say-item :target :self :placement-target :colliders :object]
                              ["object-1" "object-2" "check-collide" "group-name" "ungroup-object-1" "ungroup-object-2"]
                              ["next-task" "correct-answer"])
        rounds [pt0 pt pt1 pt2]]
    (utils-merge/basic-merge rounds)))

(defn f
  [args]
  (common/init-metadata m (prepare-templates) args))

(core/register-template m f)

