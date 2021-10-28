(ns webchange.templates.library.cinema
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.characters :as characters]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          43
        :name        "cinema"
        :tags        ["Direct Instruction - Educational Video"]
        :description "Simple video"
        :lesson-sets ["concepts-single"]
        :fields      [{:name "chanting-video-src"
                       :type "video"}]
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   3}}})

(def t {:assets        [{:url "/raw/img/cinema/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/cinema/screen-off.png", :size 10, :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/cinema/background.jpg"},
                        :letter-video
                                    {:type    "video",
                                     :x       342,
                                     :y       111,
                                     :width   1236,
                                     :height  674,
                                     :visible false,
                                     :editable? {:select        true
                                                 :show-in-tree? true}},
                        :play-button
                                    {:type      "button",
                                     :x         816,
                                     :y         400,
                                     :actions   {:click {:id "play-video", :on "click", :type "action"}},
                                     :font-size 76,
                                     :text      "Play"
                                     :filters       [{:name "brightness" :value 0}
                                                     {:name "glow" :outer-strength 0 :color 0xffd700}]
                                     :transition    "play-button"},
                        :screen-overlay
                                    {:type    "image",
                                     :x       342,
                                     :y       109,
                                     :width   1238,
                                     :height  678,
                                     :src     "/raw/img/cinema/screen-off.png",
                                     :visible true}},
        :scene-objects [["background"] ["letter-video" "screen-overlay" "play-button"]],
        :actions
                       {:finish-activity             {:type "finish-activity"},
                        :play-video                  {:type "sequence-data",
                                                      :data [{:type "action" :id "stop-timeout"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "play-button"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "screen-overlay"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value true :target "letter-video"}
                                                             {:type     "play-video",
                                                              :target   "letter-video",
                                                              :from-var [{:var-name "current-concept", :var-property "chanting-video-src", :action-property "src"}]}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "letter-video"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value true :target "screen-overlay"}
                                                             {:id "dialog-finish-activity", :type "action"}
                                                             {:id "finish-activity", :type "action"}]},
                        :dialog-finish-activity      (dialog/default "Finish activity")
                        :dialog-intro                (-> (dialog/default "Intro")
                                                         (assoc :available-activities ["highlight-play"]))
                        :dialog-timeout-instructions (-> (dialog/default "Timeout instructions")
                                                         (assoc :available-activities ["highlight-play"]))
                        :start-scene                 {:type        "sequence-data",
                                                      :data        [{:type "start-activity"},
                                                                    {:type "lesson-var-provider", :from "concepts-single", :provider-id "concepts", :variables ["current-concept"]},
                                                                    {:type "action" :id "dialog-intro"}
                                                                    {:type "action" :id "start-timeout"}],
                                                      :description "Initial action"},
                        :stop-activity               {:type "stop-activity"}
                        :start-timeout        {:type      "start-timeout-counter",
                                               :id        "inactive-counter",
                                               :action    "continue-try",
                                               :autostart true
                                               :interval  30000}
                        :stop-timeout      {:type "remove-interval"
                                            :id        "inactive-counter"}
                        :continue-try         {:type "sequence",
                                               :data ["start-timeout"
                                                      "dialog-timeout-instructions"]},
                        :highlight-play {:type               "transition"
                                         :transition-id      "play-button"
                                         :return-immediately true
                                         :from               {:brightness 0 :glow 0}
                                         :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "map", :autostart true}})

(defn f
  [t args]
  (-> t
      (characters/add-characters (:characters args))))

(core/register-template
  m
  (partial f t))
