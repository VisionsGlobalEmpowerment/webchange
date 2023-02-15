(ns webchange.templates.library.letter-intro
  (:require
    [clojure.string :as str]
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.dialog :as dialog]))

(def template-options
  [{:type "note"
    :text "You will be introducing a letter to show a student in this activity. Below you choose the letter and a sample word and word image to use. These will show on an easel. You will specify the dialog within the builder next."}
   {:type "letter-lookup"
    :key :letter
    :label "Choose Letter"
    :placeholder "Choose"}
   {:key         :word
    :label       "Word"
    :type        "string"
    :placeholder "Ex. baby"}
   {:key         :image
    :label       "Word Image"
    :type        "image"}])

(def m {:id          39
        :name        "Letter intro"
        :tags        ["listening comprehension"]
        :description "letter introduction"
        :lesson-sets ["concepts-single"]
        :fields      [{:name "image-src",
                       :type "image"}]
        :actions {:template-options {:title "Template Options"
                                     :options template-options}}})

(def t {:assets
        [{:url "/raw/img/casa/background.jpg", :size 10, :type "image"}
         {:url "/raw/img/casa/painting_canvas.png", :size 10, :type "image"}
         ],
        :objects
        {:background {:type "background", :src "/raw/img/casa/background.jpg"},
         :canvas
         {:type    "image",
          :x       650,
          :y       130,
          :width   287,
          :height  430,
          :scale-x 2,
          :scale-y 2,
          :src     "/raw/img/casa/painting_canvas.png"},
         :image
         {:type       "image",
          :x          1805,
          :y          502,
          :width      100,
          :height     100,
          :transition "image",
          :scale-x    1.3,
          :scale-y    1.3,
          :src        "",
          :states     {:visible       {:visible true}
                       :hidden        {:visible false}
                       :init-position {:x 1805, :y 502}}},
         :letter-big
         {:type        "text",
          :x           720,
          :y           280,
          :width       165,
          :height      130,
          :scene-name  "letter-big",
          :align       "center",
          :fill        "#ef545c",
          :font-family "Tabschool",
          :font-size   120,
          :states      {:glow      {:permanent-pulsation {:speed 2}}
                        :stop-glow {:permanent-pulsation false}
                        :visible   {:visible true}
                        :hidden    {:visible false}},
          :text        ""
          :transition  "letter-big"},
         :letter-small
         {:type        "text",
          :x           915,
          :y           280,
          :width       150,
          :height      130,
          :align       "left",
          :fill        "#ef545c",
          :font-family "Tabschool",
          :font-size   120,
          :states      {:glow      {:permanent-pulsation {:speed 2}}
                        :stop-glow {:permanent-pulsation false}
                        :visible   {:visible true}
                        :hidden    {:visible false}},
          :text        ""
          :transition  "letter-small"},
         :letter-path
         {:type         "animated-svg-path",
          :x            915,
          :y            230,
          :width        325
          :height       300,
          :scene-name   "letter-path",
          :animation    "stop",
          :duration     5000,
          :line-cap     "round",
          :path         "",
          :scale-x      0.65,
          :scale-y      0.65,
          :stroke       "#ef545c",
          :stroke-width 15
          :visible      false
          :states       {:hidden  {:visible false},
                         :visible {:visible true}},
          },
         :mari
         {:type       "animation",
          :x          1600,
          :y          635,
          :width      473,
          :height     511,
          :transition "mari",
          :anim       "idle",
          :name       "mari",
          :scale-x    0.5,
          :scale-y    0.5,
          :speed      0.35,
          :start      true},
         :senora-vaca
         {:type       "animation",
          :x          380,
          :y          1000,
          :width      351,
          :height     717,
          :anim       "idle",
          :name       "senoravaca",
          :scene-name "senora-vaca"
          :skin       "vaca",
          :speed      0.3,
          :start      true},
         :word
         {:type           "text"
          :transition     "word"
          :text           ""
          :align          "center"
          :vertical-align "bottom"
          :font-size      80
          :font-family    "Tabschool"
          :fill           "#fc8e51"
          :x              720
          :y              500
          :width          470
          :height         60
          :states         {:glow      {:permanent-pulsation {:speed 2}}
                           :stop-glow {:permanent-pulsation false}
                           :hidden    {:visible false},
                           :visible   {:visible true}}
          :chunks         [{:start       0 :end 1
                            :fill        "#ef545c"
                            :font-weight "bold"
                            :actions     {:click {:id "word-click", :on "click", :type "action", :unique-tag "click"}},}
                           {:start 1 :end "last"}]}},
        :scene-objects
        [["background"]
         ["canvas"]
         ["letter-small" "letter-big" "word"]
         ["letter-path"]
         ["senora-vaca" "mari"]
         ["image"]],
        :actions
        {
         :finish-activity
         {:type "sequence-data", :data [{:id "inactive-counter", :type "remove-interval"}
                                        {:type "finish-activity"}]},
         :mari-init-wand          {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
         :mari-wand-hit
         {:type "sequence-data",
          :data
          [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
           {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}]},

         :start-activity          {:type "start-activity"},
         :stop-activity           {:type "stop-activity"},

         :init-concept           {:type "set-variable" :var-name "current-concept" :var-value nil},
         :init-state              {:type "parallel",
                                   :data
                                   [
                                    {:type      "set-attribute",
                                     :target    "letter-small",
                                     :from-var  [{:var-name "current-concept", :var-property "letter", :action-property "attr-value"}],
                                     :attr-name "text"}
                                    {:type      "set-attribute",
                                     :target    "letter-path",
                                     :from-var  [{:var-name "current-concept", :var-property "letter", :action-property "attr-value"}],
                                     :attr-name "path"}
                                    {:id "big", :type "state", :target "letter-big"}
                                    {:type      "set-attribute",
                                     :target    "letter-big",
                                     :from-var  [{:var-name "current-concept", :var-property "letter-big", :action-property "attr-value"}],
                                     :attr-name "text"}
                                    {:id "hidden", :type "state", :target "word"}
                                    {:id "init-position", :type "state", :target "image"}
                                    {:type      "set-attribute",
                                     :target    "image",
                                     :from-var  [{:var-name "current-concept", :var-property "image-src", :action-property "attr-value"}],
                                     :attr-name "src"}
                                    ]},

         :glow-big                {:type "state" :target "letter-big" :id "glow"}
         :stop-glow-big           {:type "state" :target "letter-big" :id "stop-glow"}

         :glow-small              {:type "state" :target "letter-small" :id "glow"}
         :stop-glow-small         {:type "state" :target "letter-small" :id "stop-glow"}
         :introduce-big-small     {:type                 "sequence-data",
                                   :editor-type          "dialog",
                                   :concept-var          "current-concept",
                                   :available-activities ["glow-big" "stop-glow-big" "glow-small" "stop-glow-small" "highlight-big-letter" "highlight-small-letter"]
                                   :data                 [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase               "intro",
                                   :phrase-description   "Introduce task"}
         :describe-writing        {:type                 "sequence-data",
                                   :editor-type          "dialog",
                                   :concept-var          "current-concept",
                                   :available-activities ["redraw-letter"]
                                   :data                 [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase               "describe-writing",
                                   :phrase-description   "Describe writing"}
         :introduce-image-dialog  {:type               "sequence-data",
                                   :editor-type        "dialog",
                                   :concept-var        "current-concept",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase             "introduce-image-dialog",
                                   :phrase-description "Introduce image dialog"}
         :whole-word-dialog       {:type                 "sequence-data",
                                   :editor-type          "dialog",
                                   :concept-var          "current-concept",
                                   :available-activities ["whole-word-glow" "whole-word-stop-glow"]
                                   :data                 [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase               "whole-word-dialog",
                                   :phrase-description   "Whole word dialog"}
         :writing-task            {:type "sequence-data",
                                   :data [
                                          {:type "state", :id "hidden", :target "letter-small"}
                                          {:type "state", :id "visible", :target "letter-path"}
                                          {:type "parallel"
                                           :data [{:type "path-animation", :state "play", :target "letter-path"}
                                                  {:type "action", :id "describe-writing"}]
                                           }
                                          ]}
         :redraw-letter           {:type "sequence-data"
                                   :data [{:type "set-attribute" :target "letter-path" :attr-name "stroke" :attr-value "#5c54ef"}
                                          {:type "path-animation", :state "play", :target "letter-path"}]}
         :introduce-image         {:type "sequence-data",
                                   :data [
                                          {:id "hidden" :type "state" :target "image"}
                                          {:to {:x 1500,
                                                :y 575,, :loop false, :duration 0.1}, :type "transition", :transition-id "image"}
                                          {:id "visible" :type "state" :target "image"}

                                          {:data
                                           [{:to {:x 1166, :y 295, :loop false, :duration 2}, :type "transition", :transition-id "mari"}
                                            {:to {:x 1045, :y 240, :loop false, :duration 2}, :type "transition", :transition-id "image"}],
                                           :type "parallel"}
                                          {:type "empty", :duration 1000}
                                          {:id "mari-init-wand", :type "action"}
                                          {:to {:x 1600, :y 635, :loop false, :duration 1.5}, :type "transition", :transition-id "mari"}
                                          {:type "action", :id "introduce-image-dialog"}
                                          ]}
         :whole-word-glow         {:type "state" :target "word" :id "glow"}
         :whole-word-stop-glow    {:type "state" :target "word" :id "stop-glow"}
         :whole-word              {:type "sequence-data",
                                   :data [{:id "visible" :type "state" :target "word"}
                                          {:id "whole-word-dialog" :type "action"}]}
         :start-scene             {:type        "sequence",
                                   :data        ["start-activity"
                                                 "init-concept"
                                                 "init-state"
                                                 "introduce-big-small"
                                                 "writing-task"
                                                 "introduce-image"
                                                 "whole-word"
                                                 ],
                                   :unique-tag  "instruction"
                                   :description "Initial action"},
         :word-click              {:type       "sequence-data",
                                   :unique-tag "instruction"
                                   :data       [{:type "action" :id "correct-response-dialog"}
                                                {:type "action" :id "finish-activity"}]}
         :correct-response-dialog {:type               "sequence-data",
                                   :editor-type        "dialog",
                                   :concept-var        "current-concept",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase             "Correct Response",
                                   :phrase-description "Correct Response and finish"}
         :highlight-small-letter  {:type               "transition"
                                   :transition-id      "letter-small"
                                   :return-immediately true
                                   :from               {:opacity 1},
                                   :to                 {:opacity 0.38 :yoyo true :duration 0.5}
                                   :kill-after         3000}
         :highlight-big-letter    {:type               "transition"
                                   :transition-id      "letter-big"
                                   :return-immediately true
                                   :from               {:opacity 1},
                                   :to                 {:opacity 0.38 :yoyo true :duration 0.5}
                                   :kill-after         3000}
         :dialog-tap-instructions (-> (dialog/default "Tap instructions")
                                      (assoc :concept-var "current-concept"))
         :show-small-letter {:type "state", :id "visible", :target "letter-small"}
         :hide-small-letter {:type "state", :id "hidden", :target "letter-small"}
         :show-letter-path {:type "state", :id "visible", :target "letter-path"}
         :hide-letter-path {:type "state", :id "hidden", :target "letter-path"}
         },
                                        ;
        :triggers  {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata  {:prev   "map", :autostart true
                    :available-actions [{:name "Redraw letter"
                                         :action "redraw-letter"}
                                        {:name "Show small letter"
                                         :action "show-small-letter"}
                                        {:name "Hide small letter"
                                         :action "hide-small-letter"}
                                        {:name "Show letter path"
                                         :action "show-letter-path"}
                                        {:name "Hide letter path"
                                         :action "hide-letter-path"}
                                        {:name "Whole word glow"
                                         :action "whole-word-glow"}
                                        {:name "Whole word stop glow"
                                         :action "whole-word-stop-glow"}]
                    :tracks [{:title "Dialogs"
                              :nodes [{:type "prompt"
                                       :text "Introduce big and small letters"}
                                      {:type      "dialog"
                                       :action-id :introduce-big-small}
                                      {:type "prompt"
                                       :text "Describe writing task. Letter is written on desk"}
                                      {:type      "dialog"
                                       :action-id :describe-writing}
                                      {:type "prompt"
                                       :text "Image from concept is moved to the desk"}
                                      {:type      "dialog"
                                       :action-id :introduce-image-dialog}
                                      {:type "prompt"
                                       :text "Show word from concept"}
                                      {:type      "dialog"
                                       :action-id :whole-word-dialog}
                                      {:type "prompt"
                                       :text "When correct letter from word is clicked"}
                                      {:type      "dialog"
                                       :action-id :correct-response-dialog}]}
                             ]},
        :variables {:status nil}})

(defn- word-chunks
  [word letter]
  (let [letter-position (str/index-of word letter)
        letter-length (count letter)]
    (if (= letter-position 0)
      [{:start       0 :end letter-length
        :fill        "#ef545c"
        :font-weight "bold"
        :actions     {:click {:id "word-click", :on "click", :type "action", :unique-tag "click"}},}
       {:start letter-length :end "last"}]
      [{:start 0 :end letter-position}
       {:start letter-position :end (+ letter-position letter-length)
        :fill        "#ef545c"
        :font-weight "bold"
        :actions     {:click {:id "word-click", :on "click", :type "action", :unique-tag "click"}},}
       {:start (+ letter-position letter-length) :end "last"}])))

(defn- init-concept
  [t {:keys [letter image word] :as template-options}]
  (-> t
      (assoc-in [:objects :word :text] word)
      (assoc-in [:objects :word :chunks] (word-chunks word letter))
      (assoc-in [:actions :init-concept :var-value] {:letter letter
                                                     :letter-big (str/upper-case letter)
                                                     :image-src (:src image)})
      (update :assets concat [{:url (:src image) :type "image"}])
      (assoc-in [:metadata :saved-props :template-options] template-options)))

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (init-concept args)))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :template-options (init-concept activity-data args)))

(core/register-template m f update-template)
