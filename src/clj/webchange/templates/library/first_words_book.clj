(ns webchange.templates.library.first-words-book
  (:require
    [webchange.templates.core :as core]
    [webchange.utils.text :as text-utils]
    [clojure.string :refer [index-of]]
    [webchange.templates.utils.dialog :as dialog]))

(def options
  [{:key         :letters
    :label       "Letters"
    :type        "string"
    :optional?   true
    :description "Letters (i.e Aa)"}
   {:key         :title
    :label       "Title"
    :type        "string"
    :optional?   true
    :description "Title (i.e Letter)"}
   {:key         :subtitle
    :label       "Subtitle"
    :type        "string"
    :optional?   true
    :description "Subtitle (The letter a is for...)"}
   {:key         :text1
    :label       "Page 1 text"
    :type        "string"
    :optional?   true
    :description "Page 1 text (i.e. apple)"}
   {:key         :image1
    :type        "image"
    :label       "Page 1 Image"
    :optional?   true
    :description "Page 1 image"}
   {:key         :text2
    :label       "Page 2 text"
    :type        "string"
    :optional?   true
    :description "Page 2 text (i.e. apple)"}
   {:key         :image2
    :type        "image"
    :label       "Page 2 Image"
    :optional?   true
    :description "Page 2 image"}
   {:key         :text3
    :label       "Page 3 text"
    :type        "string"
    :optional?   true
    :description "Page 3 text (i.e. apple)"}
   {:key         :image3
    :type        "image"
    :label       "Page 3 Image"
    :optional?   true
    :description "Page 3 image"}
   {:key         :text4
    :label       "Page 4 text"
    :type        "string"
    :optional?   true
    :description "Page 4 text (i.e. apple)"}
   {:key         :image4
    :type        "image"
    :label       "Page 4 Image"
    :optional?   true
    :description "Page 4 image"}
   {:key         :text5
    :label       "Page 5 text"
    :type        "string"
    :optional?   true
    :description "Page 5 text (i.e. apple)"}
   {:key         :image5
    :type        "image"
    :label       "Page 5 Image"
    :optional?   true
    :description "Page 5 image"}
   {:key         :text6
    :label       "Page 6 text"
    :type        "string"
    :optional?   true
    :description "Page 6 text (i.e. apple)"}
   {:key         :image6
    :type        "image"
    :label       "Page 6 Image"
    :optional?   true
    :description "Page 6 image"}])

(def m {:id          44
        :name        "First Words Book"
        :tags        ["Vacabulary"]
        :description "Simple book"
        :actions     {:edit {:title   "Edit"
                             :options options}}
        :options     options})

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
                                                 :children ["spread-0-title-letters" "spread-0-title-text"]}
                        :spread-1               {:type     "group" :opacity 0
                                                 :children ["spread-1-title-letters" "spread-1-title-text"]}
                        :spread-2               {:type     "group" :opacity 0
                                                 :children ["spread-2-left-image" "spread-2-left-text" "spread-2-right-image" "spread-2-right-text"]}
                        :spread-3               {:type     "group" :opacity 0
                                                 :children ["spread-3-left-image" "spread-3-left-text" "spread-3-right-image" "spread-3-right-text"]}
                        :spread-4               {:type     "group" :opacity 0
                                                 :children ["spread-4-left-image" "spread-4-left-text" "spread-4-right-image" "spread-4-right-text"]}
                        :spread-0-title-letters {:type           "text",
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
                        :spread-0-title-text    {:type           "text",
                                                 :x              929,
                                                 :y              264,
                                                 :width          680,
                                                 :height         200,
                                                 :align          "center",
                                                 :chunks         [],
                                                 :fill           "white",
                                                 :font-family    "Lexend Deca",
                                                 :font-size      120,
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
                                                 :actions {:click {:id "next", :on "click", :type "action"}},
                                                 :scale-x -1,
                                                 :filters [{:name "brightness" :value 0}
                                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                                                 :src     "/raw/img/ui/back_button_01.png"}
                        :prev-button            {:type    "image",
                                                 :visible false
                                                 :x       78
                                                 :y       492,
                                                 :actions {:click {:id "prev", :on "click", :type "action"}},
                                                 :filters [{:name "brightness" :value 0}
                                                           {:name "glow" :outer-strength 0 :color 0xffd700}]
                                                 :src     "/raw/img/ui/back_button_01.png"}},
        :scene-objects [["background"] ["book" "spread-0" "spread-1" "spread-2" "spread-3" "spread-4"
                                        "next-button" "prev-button"]],
        :actions
                       {:finish-activity             {:type "sequence-data",
                                                      :data [{:type "action" :id "dialog-finish-activity"}
                                                             {:type "finish-activity"}]},
                        :next                        {:type     "test-value",
                                                      :value1   0
                                                      :fail     {:type "action" :id "next-page"},
                                                      :success  {:type "action" :id "open-page"},
                                                      :from-var [{:var-name "current-spread", :action-property "value2"}]}
                        :prev                        {:type     "test-value",
                                                      :value1   1
                                                      :fail     {:type "action" :id "prev-page"},
                                                      :success  {:type "action" :id "close-page"},
                                                      :from-var [{:var-name "current-spread", :action-property "value2"}]}
                        :next-page                   {:type "sequence-data"
                                                      :data [{:type "remove-flows" :flow-tag "dialog"}
                                                             {:type "skip"}
                                                             {:type "action" :id "stop-timeout"}
                                                             {:type "action" :id "turn-next-page"}
                                                             {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}
                                                             {:type     "set-variable", :var-value true
                                                              :from-var [{:template "dialog-spread-%-completed", :var-name "current-spread", :action-property "var-name"}]}
                                                             {:type "action" :id "test-completed"}]}
                        :prev-page                   {:type "sequence-data"
                                                      :data [{:type "remove-flows" :flow-tag "dialog"}
                                                             {:type "action" :id "stop-timeout"}
                                                             {:type "action" :id "turn-prev-page"}
                                                             {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}
                                                             {:type     "set-variable", :var-value true
                                                              :from-var [{:template "dialog-spread-%-completed", :var-name "current-spread", :action-property "var-name"}]}
                                                             {:type "action" :id "test-completed"}]}
                        :open-page                   {:type "sequence-data"
                                                      :data [{:type "remove-flows" :flow-tag "dialog"}
                                                             {:type "skip"}
                                                             {:type "action" :id "stop-timeout"}
                                                             {:type "action" :id "turn-open-page"}
                                                             {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}
                                                             {:type     "set-variable", :var-value true
                                                              :from-var [{:template "dialog-spread-%-completed", :var-name "current-spread", :action-property "var-name"}]}
                                                             {:type "action" :id "test-completed"}]}
                        :close-page                  {:type "sequence-data"
                                                      :data [{:type "remove-flows" :flow-tag "dialog"}
                                                             {:type "skip"}
                                                             {:type "action" :id "stop-timeout"}
                                                             {:type "action" :id "turn-close-page"}
                                                             {:type "action" :from-var [{:template "dialog-spread-%", :var-name "current-spread", :action-property "id"}]}
                                                             {:type     "set-variable", :var-value true
                                                              :from-var [{:template "dialog-spread-%-completed", :var-name "current-spread", :action-property "var-name"}]}
                                                             {:type "action" :id "test-completed"}]}
                        :test-completed              {:type      "test-var-list",
                                                      :success   {:type "action" :id "finish-activity"},
                                                      :fail      {:type "action" :id "start-timeout"}
                                                      :values    [true true true],
                                                      :var-names ["dialog-spread-2-completed" "dialog-spread-3-completed" "dialog-spread-4-completed"]}
                        :turn-next-page              {:type "sequence-data"
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:id "page", :loop false, :type "animation", :target "book"}
                                                             {:type "counter", :counter-id "current-spread", :counter-action "increase"}
                                                             {:type "empty" :duration 1500}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type "action" :id "show-navigation"}]}
                        :turn-prev-page              {:type "sequence-data"
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             #_{:id "page", :loop false, :type "animation", :target "book"}
                                                             {:type "counter", :counter-id "current-spread", :counter-action "decrease"}
                                                             {:type "empty" :duration 1500}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type "action" :id "show-navigation"}]}
                        :turn-open-page              {:type "sequence-data"
                                                      :on-interrupt {:type "sequence-data"
                                                                     :data [{:type "action" :id "hide-navigation"}
                                                                            {:type     "set-attribute" :attr-name "opacity", :attr-value 0 :target "spread-0"}
                                                                            {:type     "set-attribute" :attr-name "visible", :attr-value false :target "spread-0"}
                                                                            {:id "open", :type "state", :target "background"}
                                                                            {:id "idle", :type "add-animation", :target "book"}
                                                                            {:type "set-variable", :var-name "current-spread", :var-value 1}
                                                                            {:type     "set-attribute" :attr-name "visible", :attr-value true :target "spread-1"}
                                                                            {:type     "set-attribute" :attr-name "opacity", :attr-value 1 :target "spread-1"}
                                                                            {:type "action" :id "show-navigation"}]}
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:id "open_book", :type "animation", :target "book"}
                                                             {:id "open", :type "state", :target "background"}
                                                             {:id "idle", :type "add-animation", :target "book"}
                                                             {:type "counter", :counter-id "current-spread", :counter-action "increase"}
                                                             {:type "empty" :duration 1500}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type "action" :id "show-navigation"}]}
                        :turn-close-page             {:type "sequence-data"
                                                      :on-interrupt {:type "sequence-data"
                                                                     :data [{:type "action" :id "hide-navigation"}
                                                                            {:type     "set-attribute" :attr-name "opacity", :attr-value 0 :target "spread-1"}
                                                                            {:type     "set-attribute" :attr-name "visible", :attr-value false :target "spread-1"}
                                                                            {:id "closed", :type "state", :target "background"}
                                                                            {:id "close_idle", :type "add-animation", :target "book"}
                                                                            {:type "set-variable", :var-name "current-spread", :var-value 0}
                                                                            {:type     "set-attribute" :attr-name "visible", :attr-value true :target "spread-0"}
                                                                            {:type     "set-attribute" :attr-name "opacity", :attr-value 1 :target "spread-0"}
                                                                            {:type "action" :id "show-navigation"}]}
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:to       {:opacity 0, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value false
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             #_{:id "open_book", :type "animation", :target "book"}
                                                             {:id "closed", :type "state", :target "background"}
                                                             {:id "close_idle", :type "add-animation", :target "book"}
                                                             {:type "counter", :counter-id "current-spread", :counter-action "decrease"}
                                                             {:type "empty" :duration 1500}
                                                             {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "target"}]}
                                                             {:to       {:opacity 1, :duration 0.5}, :type "transition"
                                                              :from-var [{:template "spread-%", :var-name "current-spread", :action-property "transition-id"}]}
                                                             {:type "action" :id "show-navigation"}]}
                        :hide-navigation             {:type "parallel"
                                                      :data [{:type "set-attribute" :attr-name "visible", :attr-value false :target "prev-button"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "next-button"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "close-button"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "open-button"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "finish-button"}]}

                        :show-navigation             {:type "sequence-data"
                                                      :data [{:type     "case",
                                                              :from-var [{:template "spread-%" :var-name "current-spread", :action-property "value"}]
                                                              :options
                                                                        {:spread-0 {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"},
                                                                         :spread-1 {:type "sequence-data"
                                                                                    :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                           {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                         :spread-2 {:type "sequence-data"
                                                                                    :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                           {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                         :spread-3 {:type "sequence-data"
                                                                                    :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                           {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                                         :spread-4 {:type "sequence-data"
                                                                                    :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}]}}}]}

                        :start-scene                 {:type "sequence-data",
                                                      :data [{:type "start-activity"},
                                                             {:type "set-variable", :var-name "current-spread", :var-value 0},
                                                             {:type "action" :id "show-navigation"}
                                                             {:type "action" :id "dialog-intro"}]},
                        :stop-activity               {:type "stop-activity"}
                        :start-timeout               {:type      "start-timeout-counter",
                                                      :id        "inactive-counter",
                                                      :action    "continue-try",
                                                      :autostart true
                                                      :interval  10000}
                        :stop-timeout                {:type "remove-interval" :id "inactive-counter"}
                        :continue-try                {:type "sequence-data",
                                                      :data [{:type "action" :id "start-timeout"}
                                                             {:type "action" :id "dialog-timeout-instructions"}]},

                        :dialog-spread-0             (-> (dialog/default "Front page")
                                                         (assoc :tags ["dialog"]))
                        :dialog-spread-1             (-> (dialog/default "Title page")
                                                         (assoc :tags ["dialog"]))
                        :dialog-spread-2             (-> (dialog/default "First content spread")
                                                         (assoc :tags ["dialog"]))
                        :dialog-spread-3             (-> (dialog/default "Second content spread")
                                                         (assoc :tags ["dialog"]))
                        :dialog-spread-4             (-> (dialog/default "Third content spread")
                                                         (assoc :tags ["dialog"]))
                        :dialog-finish-activity      (dialog/default "Finish activity")
                        :dialog-intro                (-> (dialog/default "Intro")
                                                         (assoc :available-activities ["highlight-prev" "highlight-next" "turn-open-page" "turn-close-page"]))
                        :dialog-timeout-instructions (-> (dialog/default "Timeout instructions")
                                                         (assoc :available-activities ["highlight-prev" "highlight-next"]))
                        :highlight-next              {:type               "transition"
                                                      :transition-id      "next-button"
                                                      :return-immediately true
                                                      :from               {:brightness 0 :glow 0}
                                                      :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}
                        :highlight-prev              {:type               "transition"
                                                      :transition-id      "prev-button"
                                                      :return-immediately true
                                                      :from               {:brightness 0 :glow 0}
                                                      :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}}},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "library", :autostart true
                        :tracks [{:title "Book"
                                  :nodes [{:type      "dialog"
                                           :action-id :dialog-intro}
                                          {:type      "dialog"
                                           :action-id :dialog-spread-0}
                                          {:type      "dialog"
                                           :action-id :dialog-spread-1}
                                          {:type      "dialog"
                                           :action-id :dialog-spread-2}
                                          {:type      "dialog"
                                           :action-id :dialog-spread-3}
                                          {:type      "dialog"
                                           :action-id :dialog-spread-4}
                                          {:type      "dialog"
                                           :action-id :dialog-finish-activity}
                                          {:type      "dialog"
                                           :action-id :dialog-timeout-instructions}]}]},})

(defn- set-data
  [activity-data args]
  (cond-> activity-data
          (not-empty (:letters args))
          (assoc-in [:objects :spread-0-title-letters :text] (:letters args))
          (not-empty (:letters args))
          (assoc-in [:objects :spread-0-title-letters :chunks] (text-utils/text->chunks (:letters args)))

          (not-empty (:title args))
          (assoc-in [:objects :spread-0-title-text :text] (:title args))
          (not-empty (:title args))
          (assoc-in [:objects :spread-0-title-text :chunks] (text-utils/text->chunks (:title args)))

          (not-empty (:letters args))
          (assoc-in [:objects :spread-1-title-letters :text] (:letters args))
          (not-empty (:letters args))
          (assoc-in [:objects :spread-1-title-letters :chunks] (text-utils/text->chunks (:letters args)))

          (not-empty (:subtitle args))
          (assoc-in [:objects :spread-1-title-text :text] (:subtitle args))
          (not-empty (:subtitle args))
          (assoc-in [:objects :spread-1-title-text :chunks] (text-utils/text->chunks (:subtitle args)))

          (not-empty (:text1 args))
          (assoc-in [:objects :spread-2-left-text :text] (:text1 args))
          (not-empty (:text1 args))
          (assoc-in [:objects :spread-2-left-text :chunks] (text-utils/text->chunks (:text1 args)))

          (not-empty (:text2 args))
          (assoc-in [:objects :spread-2-right-text :text] (:text2 args))
          (not-empty (:text2 args))
          (assoc-in [:objects :spread-2-right-text :chunks] (text-utils/text->chunks (:text2 args)))

          (not-empty (:text3 args))
          (assoc-in [:objects :spread-3-left-text :text] (:text3 args))
          (not-empty (:text3 args))
          (assoc-in [:objects :spread-3-left-text :chunks] (text-utils/text->chunks (:text3 args)))

          (not-empty (:text4 args))
          (assoc-in [:objects :spread-3-right-text :text] (:text4 args))
          (not-empty (:text4 args))
          (assoc-in [:objects :spread-3-right-text :chunks] (text-utils/text->chunks (:text4 args)))

          (not-empty (:text5 args))
          (assoc-in [:objects :spread-4-left-text :text] (:text5 args))
          (not-empty (:text5 args))
          (assoc-in [:objects :spread-4-left-text :chunks] (text-utils/text->chunks (:text5 args)))

          (not-empty (:text6 args))
          (assoc-in [:objects :spread-4-right-text :text] (:text6 args))
          (not-empty (:text6 args))
          (assoc-in [:objects :spread-4-right-text :chunks] (text-utils/text->chunks (:text6 args)))

          (not-empty (:image1 args))
          (assoc-in [:objects :spread-2-left-image :src] (-> args :image1 :src))
          (not-empty (:image1 args))
          (update :assets conj {:url (-> args :image1 :src) :size 1 :type "image"})
          (not-empty (:image2 args))
          (assoc-in [:objects :spread-2-right-image :src] (-> args :image2 :src))
          (not-empty (:image2 args))
          (update :assets conj {:url (-> args :image2 :src) :size 1 :type "image"})
          (not-empty (:image3 args))
          (assoc-in [:objects :spread-3-left-image :src] (-> args :image3 :src))
          (not-empty (:image3 args))
          (update :assets conj {:url (-> args :image3 :src) :size 1 :type "image"})
          (not-empty (:image4 args))
          (assoc-in [:objects :spread-3-right-image :src] (-> args :image4 :src))
          (not-empty (:image4 args))
          (update :assets conj {:url (-> args :image4 :src) :size 1 :type "image"})
          (not-empty (:image5 args))
          (assoc-in [:objects :spread-4-left-image :src] (-> args :image5 :src))
          (not-empty (:image5 args))
          (update :assets conj {:url (-> args :image5 :src) :size 1 :type "image"})
          (not-empty (:image6 args))
          (assoc-in [:objects :spread-4-right-image :src] (-> args :image6 :src))
          (not-empty (:image6 args))
          (update :assets conj {:url (-> args :image6 :src) :size 1 :type "image"})))

(defn f
  [args]
  (-> t
      (set-data args)
      (assoc-in [:metadata :actions] (:actions m))))

(defn fu
  [old-data args]
  (set-data old-data args))

(core/register-template
  m f fu)
