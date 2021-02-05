(ns webchange.templates.library.categorize-shapes.template
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.merge :as utils-merge]
    [webchange.templates.library.categorize-shapes.round-1 :refer [template-round-1]]
    [webchange.templates.library.categorize-shapes.round-2 :refer [template-round-2]]
    [webchange.templates.library.categorize-shapes.round-3 :refer [template-round-3]]
    [webchange.templates.core :as core]))

(def m {:id          30
        :name        "Categorize shapes - 3 rounds"
        :tags        ["Independent Practice"]
        :description "Categorize"
        :actions     {:add-question {:title   "Add question",
                                     :options {:question-page {:label         "Question"
                                                               :type          "questions-no-image"
                                                               :answers-label "Answers"
                                                               :max-answers   5}}}}})

(defn prepare-templates
  []
  (let [
        pt (utils-merge/prepare-template template-round-1 "r1" [:target] [] [])
        pt1 (utils-merge/prepare-template template-round-2 "r2" [:target :box] [] [])
        pt2 (utils-merge/prepare-template template-round-3 "r3" [:target :self :colliders :object]
                              ["object-1" "object-2" "check-collide" "group-name" "ungroup-object-1" "ungroup-object-2"]
                              ["next-task" "correct-answer"])
        rounds [pt pt1 pt2]]
    (utils-merge/basic-merge rounds)))

(defn f
  [args]
  (common/init-metadata m (prepare-templates) args))

(defn fu
  [old-data args]
  (let [params (common/get-replace-params old-data)
        [_ actions assets] (question/create (:question-page args) params)
        old-data (update-in old-data [:assets] concat assets)]
    (common/merge-new-action old-data actions params)))

(core/register-template
  m f fu)

