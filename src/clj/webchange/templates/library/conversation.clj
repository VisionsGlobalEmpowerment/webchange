(ns webchange.templates.library.conversation
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.image :as image]
    [webchange.templates.utils.characters :as characters]
    [webchange.templates.core :as core]))

(def m {:id          26
        :name        "Conversation"
        :tags        ["Independent Practice"]
        :description "Conversation"
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   5}}
        :actions     {:add-dialog   {:title   "Add dialog",
                                     :options {:dialog {:label "Dialog name"
                                                        :placeholder "(ex. Conversation about ball)"
                                                        :type  "string"}}}
                      :add-question {:title   "Add question",
                                     :options {:question-page {:label       "Question"
                                                               :type        "questions-no-image"
                                                               :max-answers 5}}}
                      :add-image    {:title   "Set image",
                                     :options {:scene-image {:label       "Image"
                                                             :type        "image"
                                                             :options {:max-width 300
                                                                       :max-height 300
                                                                       :min-height 100
                                                                       :min-width 100}}}}
                      }})
(def t {:assets
                       [
                        {:url "/raw/img/casa/background.jpg", :size 10 :type "image"}
                        ],
        :objects
                       {:background  {:type "background", :src "/raw/img/casa/background.jpg"}},
        :scene-objects [["background"]],
        :actions       {
                        :placeholder   {:type "empty" :duration 200}
                        :stop-activity {:type "stop-activity", :id "conversation"},
                        }
        :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "placeholder"}},
        :metadata      {:autostart   true
                        :last-insert "placeholder"
                        :prev        "map"}})

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (characters/add-characters (:characters args))))

(defn fu
  [old-data args]
  (if (or (contains? args :dialog) (contains? args :question-page))
    (let [params (common/get-replace-params old-data)
          [action-name actions assets] (if (contains? args :dialog)
                                         (dialog/create-and-place-before (:dialog args) params)
                                         (question/create-and-place-before (:question-page args) params))
          old-data (-> old-data
                       (common/add-track-action {:track-name "Dialog"
                                                 :action-id  (keyword action-name)
                                                 :type       (if (contains? args :dialog) "dialog" "question")})
                       (update-in [:assets] concat assets))]
      (common/merge-new-action old-data actions params))
    (if (contains? args :scene-image)
      (let [objects (if (contains? args :scene-image)
                      (image/create (:scene-image args) {:name "scene-image" :editable true})
                      {})]
        (-> old-data
            (update-in [:objects] merge objects)
            (common/add-scene-object (vec (map (fn [key] (name key)) (keys objects))))))
      old-data)))

(core/register-template
  m f fu)

