(ns webchange.templates.library.categorize-antonims.template
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.merge :as utils-merge]
    [webchange.templates.library.categorize-antonims.round-0 :refer [template-round-0]]
    [webchange.templates.library.categorize-antonims.round-1 :refer [template-round-1]]
    [webchange.templates.library.categorize-antonims.round-2 :refer [template-round-2]]
    [webchange.templates.library.categorize-antonims.round-3 :refer [template-round-3]]
    [webchange.templates.core :as core]))

(def m {:id          28
        :name        "Categorize antonims - 3 rounds"
        :tags        ["Independent Practice"]
        :description "Categorize"})

(defn prepare-templates
  []
  (let [pt0 (utils-merge/prepare-template template-round-0 "r0" [:target] [] [])
        pt1 (utils-merge/prepare-template template-round-1 "r1" [:say-item :target :correct-drop :box] [] [])
        pt2 (utils-merge/prepare-template template-round-2 "r2" [:say-item :target :correct-drop :box] [] [])
        pt3 (utils-merge/prepare-template template-round-3 "r3" [:say-item :target :correct-drop :box]
                              ["instruction" "check-collide"]
                              ["next-task" "correct-answer"])
        rounds [pt0 pt1 pt2 pt3]]
    (utils-merge/basic-merge rounds)))

(defn f
  [args]
  (common/init-metadata m (prepare-templates) args))

(core/register-template m f)

