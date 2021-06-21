(ns webchange.templates.library.dialog
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.characters :as characters]))

(def m {:id          4
        :name        "dialog"
        :tags        ["Direct Instruction - Educational Video"]
        :description "Simple dialog"
        :props       {:game-changer? true}
        :options     [{:key   "characters"
                       :label "Characters"
                       :type  "characters"
                       :max   4}]})

(def t {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}]
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/casa/background_casa.png"}
                                             :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}}
        :scene-objects [["layered-background"]],
        :actions       {:dialog-1 {:type               "sequence-data",
                                   :editor-type        "dialog",
                                   :concept-var        "current-word",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase             "dialog",
                                   :phrase-description "Dialog",
                                   :dialog-track       "1 Dialog"}
                        :intro
                                  {:type "sequence-data",
                                   :data [{:type "empty" :duration 2000}
                                          {:type "start-activity"}
                                          {:type "action" :id "dialog-1"}
                                          {:type "finish-activity"}]}}
        :triggers
                       {:start {:on "start", :action "intro"}}})

(defn f
  [t args]
  (-> t
      (characters/add-characters (:characters args))))

(core/register-template
  m
  (partial f t))

