(ns webchange.templates.library.categorize.colors.template
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.merge :as utils-merge]
    [webchange.templates.library.categorize.colors.round-0 :refer [template-round-0]]
    [webchange.templates.library.categorize.colors.round-1 :as round-1]
    [webchange.templates.library.categorize.colors.round-2 :as round-2]
    [webchange.templates.library.categorize.colors.round-3 :refer [template-round-3]]
    [webchange.templates.core :as core]))

(def m {:id          22
        :name        "Categorize - Colors"
        :tags        ["Independent Practice"]
        :description "Categorize"})

(defn prepare-templates
  []
  (let [pt0 (utils-merge/prepare-template template-round-0 "r0" [:target] [] [])
        pt1 (utils-merge/prepare-template round-1/template "r1" round-1/params-object-names round-1/var-object-names round-1/var-action-names round-1/all-vars-in-actions)
        pt2 (utils-merge/prepare-template round-2/template "r2" round-2/params-object-names round-2/var-object-names round-2/var-action-names round-2/all-vars-in-actions)
        pt3 (utils-merge/prepare-template template-round-3 "r3" [:say-color :target :self :colliders :crayon]
                                          ["object-1" "object-2" "check-collide"]
                                          ["next-task" "correct-answer" "instruction"])
        rounds [pt0 pt1 pt2 pt3]]
    (utils-merge/basic-merge rounds)))

(defn f
  [args]
  (common/init-metadata m (prepare-templates) args))

(core/register-template m f)
