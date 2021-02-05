(ns webchange.templates.library.conversation
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.image :as image]
    [webchange.templates.core :as core]))

(def m {:id          26
        :name        "Conversation"
        :tags        ["Independent Practice"]
        :description "Conversation"
        :actions     {:add-dialog   {:title   "Add dialog",
                                     :options {:dialog {:label "Dialog"
                                                        :type  "string"}}}
                      :add-question {:title   "Add question",
                                     :options {:question-page {:label       "Question"
                                                               :type        "questions-no-image"
                                                               :max-answers 5}}}
                      :add-image    {:title   "Set image",
                                     :options {:scene-image {:label       "Image"
                                                             :type        "image"
                                                             :max-answers 5}}}
                      }})
(def t {:assets
                       [
                        {:url "/raw/img/casa/background.jpg", :size 10 :type "image"}
                        ],
        :objects
                       {:background  {:type "background", :src "/raw/img/casa/background.jpg"},
                        :vera        {:type  "animation" :x 1128 :y 960 :name "vera" :anim "idle" :speed 0.3
                                      :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}
                        :senora-vaca {:type    "animation" :x 655 :y 960 :name "senoravaca" :anim "idle" :speed 0.3 :skin "vaca"
                                      :width   351 :height 717 :scale {:x 1 :y 1} :start true
                                      :actions {:click {:type "action" :id "restart" :on "click"}}}

                        :mari        {:type    "animation" :scene-name "mari" :name "mari" :anim "idle"
                                      :start   true :speed 0.35 :transition "mari"
                                      :x       1000 :y 311 :width 473 :height 511 :anim-offset {:x 0 :y -150}
                                      :scale-y 0.5 :scale-x 0.5}

                        },
        :scene-objects [["background"] ["vera" "senora-vaca" "mari"]],
        :actions       {
                        :placeholder {:type "empty" :duration 200}
                        :stop-activity       {:type "stop-activity", :id "conversation"},
                        }
        :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "placeholder"}},
        :metadata      {:autostart   true
                        :last-insert "placeholder"
                        :prev        "map"}})

(defn f
  [args]
  (common/init-metadata m t args))

(defn fu
  [old-data args]
  (if (or (contains? args :dialog) (contains? args :question-page))
    (let [params (common/get-replace-params old-data)
          [action-name actions assets] (if (contains? args :dialog)
                    (dialog/create (:dialog args) params)
                    (question/create (:question-page args) params))
          old-data (-> old-data
          (common/add-track-action  {:track-name "Dialog"
                                                      :action-id (keyword action-name)
                                                      :type (if (contains? args :dialog) "dialog" "question")})
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

