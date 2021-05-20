(ns webchange.templates.library.first-words-book
  (:require
    [webchange.templates.core :as core]
    [webchange.utils.text :as text-utils]
    [clojure.string :refer [index-of]]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          44
        :name        "First Words Book"
        :tags        ["Vacabulary"]
        :description "Simple book"
        :options     [{:key         :letters
                       :label       "Letters"
                       :type        "string"
                       :description "Letters (i.e Aa)"}
                      {:key         :subtitle
                       :label       "Subtitle"
                       :type        "string"
                       :description "Subtitle (The letter a is for...)"}
                      {:key         :text1
                       :label       "Page 1 text"
                       :type        "string"
                       :description "Page 1 text (i.e. apple)"}
                      {:key   :image1
                       :type  "image"
                       :label "Page 1 Image"
                       :description "Page 1 image"}
                      {:key   :text2
                       :label "Page 2 text"
                       :type  "string"
                       :description "Page 2 text (i.e. apple)"}
                      {:key   :image2
                       :type  "image"
                       :label "Page 2 Image"
                       :description "Page 2 image"}
                      {:key   :text3
                       :label "Page 3 text"
                       :type  "string"
                       :description "Page 3 text (i.e. apple)"}
                      {:key   :image3
                       :type  "image"
                       :label "Page 3 Image"
                       :description "Page 3 image"}
                      {:key   :text4
                       :label "Page 4 text"
                       :type  "string"
                       :description "Page 4 text (i.e. apple)"}
                      {:key   :image4
                       :type  "image"
                       :label "Page 4 Image"
                       :description "Page 4 image"}
                      {:key   :text5
                       :label "Page 5 text"
                       :type  "string"
                       :description "Page 5 text (i.e. apple)"}
                      {:key   :image5
                       :type  "image"
                       :label "Page 5 Image"
                       :description "Page 5 image"}
                      {:key   :text6
                       :label "Page 6 text"
                       :type  "string"
                       :description "Page 6 text (i.e. apple)"}
                      {:key   :image6
                       :type  "image"
                       :label "Page 6 Image"
                       :description "Page 6 image"}]})

(def t {:assets        [{:url "/raw/img/library/book/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/library/book/background2.jpg", :size 10, :type "image"}
                        {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}],
        :objects
                       {:background
                                                {:type   "background",
                                                 :src    "/raw/img/library/book/background2.jpg",
                                                 :states {:open
                                                          {:src "/raw/img/library/book/background.jpg"},
                                                          :closed
                                                          {:src "/raw/img/library/book/background2.jpg"}}},
                        :book
                                                {:type        "animation",
                                                 :x           928,
                                                 :y           1048,
                                                 :width       1439,
                                                 :height      960,
                                                 :scene-name  "book",
                                                 :anim        "close_idle",
                                                 :anim-offset {:x 0, :y -480},
                                                 :name        "book",
                                                 :skin        "default"
                                                 :start       true},
                        :spread-0               {:type     "group" :opacity 1
                                                 :children ["spread-0-title-text"]}
                        :spread-1               {:type     "group" :opacity 0
                                                 :children ["spread-1-title-letters" "spread-1-title-text"]}
                        :spread-2               {:type     "group" :opacity 0
                                                 :children ["spread-2-left-image" "spread-2-left-text" "spread-2-right-image" "spread-2-right-text"]}
                        :spread-3               {:type     "group" :opacity 0
                                                 :children ["spread-3-left-image" "spread-3-left-text" "spread-3-right-image" "spread-3-right-text"]}
                        :spread-4               {:type     "group" :opacity 0
                                                 :children ["spread-4-left-image" "spread-4-left-text" "spread-4-right-image" "spread-4-right-text"]}
                        :spread-0-title-text    {:type           "text",
                                                 :x              929,
                                                 :y              164,
                                                 :width          680,
                                                 :height         800,
                                                 :align          "center",
                                                 :chunks         [],
                                                 :fill           "white",
                                                 :font-family    "Lexend Deca",
                                                 :font-size      290,
                                                 :shadow-blur    5,
                                                 :shadow-color   "#1a1a1a",
                                                 :shadow-offset  {:x 5, :y 5},
                                                 :shadow-opacity 0.5,
                                                 :text           ""
                                                 :vertical-align "middle"}
                        :spread-1-title-letters {:type           "text",
                                                 :x              929,
                                                 :y              264,
                                                 :width          680,
                                                 :height         200,
                                                 :align          "center",
                                                 :chunks         [],
                                                 :fill           "black",
                                                 :font-family    "Lexend Deca",
                                                 :font-size      90,
                                                 :text           "",
                                                 :vertical-align "middle"}
                        :spread-1-title-text    {:type           "text",
                                                 :x              940,
                                                 :y              164,
                                                 :width          600,
                                                 :height         800,
                                                 :align          "center",
                                                 :chunks         [],
                                                 :fill           "black",
                                                 :font-family    "Lexend Deca",
                                                 :font-size      90,
                                                 :text           "",
                                                 :vertical-align "middle"}
                        :spread-2-left-image    {:type  "image",
                                                 :x     420,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-2-left-text     {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              300
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :spread-2-right-image   {:type  "image",
                                                 :x     1050,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-2-right-text    {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              950
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :spread-3-left-image    {:type  "image",
                                                 :x     420,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-3-left-text     {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              300
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :spread-3-right-image   {:type  "image",
                                                 :x     1050,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-3-right-text    {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              950
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :spread-4-left-image    {:type  "image",
                                                 :x     420,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-4-left-text     {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              300
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :spread-4-right-image   {:type  "image",
                                                 :x     1050,
                                                 :y     270,
                                                 :scale {:x 4 :y 4}}
                        :spread-4-right-text    {:type           "text"
                                                 :word-wrap      true
                                                 :vertical-align "top"
                                                 :align          "center"
                                                 :font-size      98
                                                 :font-family    "Lexend Deca"
                                                 :x              950
                                                 :y              700
                                                 :width          600
                                                 :height         200
                                                 :fill           "black"
                                                 :text           "text"
                                                 :editable?      {:select true}
                                                 :chunks         []}
                        :next-button            {:type    "image",
                                                 :visible false
                                                 :x       1790,
                                                 :y       492,
                                                 :actions {:click {:id "next-page", :on "click", :type "action"}},
                                                 :scale-x -1,
                                                 :src     "/raw/img/ui/back_button_01.png"}
                        :prev-button            {:type    "image",
                                                 :visible false
                                                 :x       78
                                                 :y       492,
                                                 :actions {:click {:id "prev-page", :on "click", :type "action"}},
                                                 :src     "/raw/img/ui/back_button_01.png"}
                        :open-button            {:type    "image",
                                                 :visible false
                                                 :x       1790,
                                                 :y       492,
                                                 :actions {:click {:id "open-page", :on "click", :type "action"}},
                                                 :scale-x -1,
                                                 :filters [{:name "brightness" :value 0}
                                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                                                 :src     "/raw/img/ui/back_button_01.png"}
                        :close-button           {:type    "image",
                                                 :visible false
                                                 :x       78
                                                 :y       492,
                                                 :actions {:click {:id "close-page", :on "click", :type "action"}},
                                                 :filters [{:name "brightness" :value 0}
                                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                                                 :src     "/raw/img/ui/back_button_01.png"}
                        :finish-button          {:type    "image",
                                                 :visible false
                                                 :x       1790,
                                                 :y       492,
                                                 :scale-x -1
                                                 :actions {:click {:id "finish-activity", :on "click", :type "action"}},
                                                 :src     "/raw/img/ui/back_button_01.png"},},
        :scene-objects [["background"] ["book" "spread-0" "spread-1" "spread-2" "spread-3" "spread-4"
                                        "next-button" "prev-button" "open-button" "close-button" "finish-button"]],
        :actions
                       {:finish-activity        {:type "sequence-data",
                                                 :data [{:type "action" :id "dialog-finish-activity"}
                                                        {:type "finish-activity"}]},
                        :next-page              {:type "sequence-data"
                                                 :data [{:type "action" :id "turn-next-page"}
                                                        {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}]}
                        :prev-page              {:type "sequence-data"
                                                 :data [{:type "action" :id "turn-prev-page"}
                                                        {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}]}
                        :open-page              {:type "sequence-data"
                                                 :data [{:type "action" :id "turn-open-page"}
                                                        {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}]}
                        :close-page             {:type "sequence-data"
                                                 :data [{:type "action" :id "turn-close-page"}
                                                        {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}]}
                        :turn-next-page         {:type "sequence-data"
                                                 :data [{:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:id "page", :loop false, :type "animation", :target "book"}
                                                        {:type "counter", :counter-id "current-spread", :counter-action "increase"}
                                                        {:type "action" :id "show-navigation"}
                                                        {:type "empty" :duration 1500}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}]}
                        :turn-prev-page         {:type "sequence-data"
                                                 :data [{:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        #_{:id "page", :loop false, :type "animation", :target "book"}
                                                        {:type "counter", :counter-id "current-spread", :counter-action "decrease"}
                                                        {:type "action" :id "show-navigation"}
                                                        {:type "empty" :duration 1500}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}]}
                        :turn-open-page         {:type "sequence-data"
                                                 :data [{:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:id "open_book", :type "animation", :target "book"}
                                                        {:id "open", :type "state", :target "background"}
                                                        {:id "idle", :type "add-animation", :target "book"}
                                                        {:type "counter", :counter-id "current-spread", :counter-action "increase"}
                                                        {:type "action" :id "show-navigation"}
                                                        {:type "empty" :duration 1500}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}]}
                        :turn-close-page        {:type "sequence-data"
                                                 :data [{:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        #_{:id "open_book", :type "animation", :target "book"}
                                                        {:id "closed", :type "state", :target "background"}
                                                        {:id "close_idle", :type "add-animation", :target "book"}
                                                        {:type "counter", :counter-id "current-spread", :counter-action "decrease"}
                                                        {:type "action" :id "show-navigation"}
                                                        {:type "empty" :duration 1500}
                                                        {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                        {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                         :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}]}
                        :show-navigation        {:type "sequence-data"
                                                 :data [{:type "set-attribute" :attr-name "visible", :attr-value false :target "prev-button"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value false :target "next-button"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value false :target "close-button"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value false :target "open-button"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value false :target "finish-button"}
                                                        {:type     "case",
                                                         :from-var [{:template "spread-%" :var-name "current-spread", :action-property "value"}]
                                                         :options
                                                                   {:spread-0 {:type "set-attribute" :attr-name "visible", :attr-value true :target "open-button"},
                                                                    :spread-1 {:type "sequence-data"
                                                                               :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "close-button"}
                                                                                      {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                    :spread-2 {:type "sequence-data"
                                                                               :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                      {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                    :spread-3 {:type "sequence-data"
                                                                               :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                      {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                    :spread-4 {:type "sequence-data"
                                                                               :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                      {:type "set-attribute" :attr-name "visible", :attr-value true :target "finish-button"}]}}}]}

                        :start-scene            {:type "sequence-data",
                                                 :data [{:type "start-activity"},
                                                        {:type "set-variable", :var-name "current-spread", :var-value 0},
                                                        {:type "action" :id "show-navigation"}
                                                        {:type "action" :id "dialog-intro"}]},
                        :stop-activity          {:type "stop-activity"}
                        :dialog-spread-0        (dialog/default "Front page")
                        :dialog-spread-1        (dialog/default "Title page")
                        :dialog-spread-2        (dialog/default "First page")
                        :dialog-spread-3        (dialog/default "Second page")
                        :dialog-spread-4        (dialog/default "Third page")
                        :dialog-finish-activity (dialog/default "Finish activity")
                        :dialog-intro           (-> (dialog/default "Intro")
                                                    (assoc :available-activities ["highlight-open" "highlight-close" "turn-open-page" "turn-close-page"]))
                        :highlight-open         {:type               "transition"
                                                 :transition-id      "open-button"
                                                 :return-immediately true
                                                 :from               {:brightness 0 :glow 0}
                                                 :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}
                        :highlight-close        {:type               "transition"
                                                 :transition-id      "close-button"
                                                 :return-immediately true
                                                 :from               {:brightness 0 :glow 0}
                                                 :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "library", :autostart true},})

(defn f
  [args]
  (-> t
      (assoc-in [:objects :spread-0-title-text :text] (:letters args))
      (assoc-in [:objects :spread-0-title-text :chunks] (text-utils/text->chunks (:letters args)))
      (assoc-in [:objects :spread-1-title-letters :text] (:letters args))
      (assoc-in [:objects :spread-1-title-letters :chunks] (text-utils/text->chunks (:letters args)))
      (assoc-in [:objects :spread-1-title-text :text] (:subtitle args))
      (assoc-in [:objects :spread-1-title-text :chunks] (text-utils/text->chunks (:subtitle args)))
      (assoc-in [:objects :spread-2-left-text :text] (:text1 args))
      (assoc-in [:objects :spread-2-left-text :chunks] (text-utils/text->chunks (:text1 args)))
      (assoc-in [:objects :spread-2-right-text :text] (:text2 args))
      (assoc-in [:objects :spread-2-right-text :chunks] (text-utils/text->chunks (:text2 args)))

      (assoc-in [:objects :spread-3-left-text :text] (:text3 args))
      (assoc-in [:objects :spread-3-left-text :chunks] (text-utils/text->chunks (:text3 args)))
      (assoc-in [:objects :spread-3-right-text :text] (:text4 args))
      (assoc-in [:objects :spread-3-right-text :chunks] (text-utils/text->chunks (:text4 args)))

      (assoc-in [:objects :spread-4-left-text :text] (:text5 args))
      (assoc-in [:objects :spread-4-left-text :chunks] (text-utils/text->chunks (:text5 args)))
      (assoc-in [:objects :spread-4-right-text :text] (:text6 args))
      (assoc-in [:objects :spread-4-right-text :chunks] (text-utils/text->chunks (:text6 args)))

      (assoc-in [:objects :spread-2-left-image :src] (-> args :image1 :src))
      (assoc-in [:objects :spread-2-right-image :src] (-> args :image2 :src))
      (assoc-in [:objects :spread-3-left-image :src] (-> args :image3 :src))
      (assoc-in [:objects :spread-3-right-image :src] (-> args :image4 :src))
      (assoc-in [:objects :spread-4-left-image :src] (-> args :image5 :src))
      (assoc-in [:objects :spread-4-right-image :src] (-> args :image6 :src))

      (update :assets concat [{:url (-> args :image1 :src), :size 1, :type "image"}
                              {:url (-> args :image2 :src), :size 1, :type "image"}
                              {:url (-> args :image3 :src), :size 1, :type "image"}
                              {:url (-> args :image4 :src), :size 1, :type "image"}
                              {:url (-> args :image5 :src), :size 1, :type "image"}
                              {:url (-> args :image6 :src), :size 1, :type "image"}])))

(core/register-template
  m f)
