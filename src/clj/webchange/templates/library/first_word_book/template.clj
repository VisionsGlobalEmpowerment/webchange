(ns webchange.templates.library.first-word-book.template
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.core :as core]
    [webchange.templates.library.first-word-book.add-spread :refer [add-spread spread-idx->dialog-name]]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.utils.text :as text-utils]))

(def create-options
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
    :description "Subtitle (The letter a is for...)"}])

(def add-spread-options
  [{:key         :text-left
    :label       "Left Page Text"
    :type        "string"
    :optional?   true
    :description "Left page text (i.e. 'apple')"}
   {:key         :image-left
    :label       "Left Page Image"
    :type        "image"
    :optional?   true
    :description "Left page image"}
   {:key         :text-right
    :label       "Right Page Text"
    :type        "string"
    :optional?   true
    :description "Right page text (i.e. 'alligator')"}
   {:key         :image-right
    :label       "Right Page Image"
    :type        "image"
    :optional?   true
    :description "Right page image"}])

(def m {:id          49
        :name        "First Words Book V2"
        :tags        ["Vocabulary"]
        :description "Simple book"
        :actions     {:edit {:title   "Edit"
                             :options create-options}
                      :add  {:title   "Add Spread"
                             :options add-spread-options}}
        :options     create-options})

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
         :spread-0-title-letters {:type           "text",
                                  :x              929,
                                  :y              244,
                                  :width          680,
                                  :height         800,
                                  :align          "center",
                                  :chunks         [],
                                  :fill           "white",
                                  :font-family    "Tabschool",
                                  :font-size      290,
                                  :shadow-blur    5,
                                  :shadow-color   "#1a1a1a",
                                  :shadow-offset  {:x 5, :y 5},
                                  :shadow-opacity 0.5,
                                  :text           ""
                                  :vertical-align "middle"
                                  :metadata       {:display-name "Cover letter"
                                                   :page-idx     0
                                                   :text-idx     1}
                                  :editable?      {:select true}}
         :spread-0-title-text    {:type           "text",
                                  :x              929,
                                  :y              264,
                                  :width          680,
                                  :height         200,
                                  :align          "center",
                                  :chunks         [],
                                  :fill           "white",
                                  :font-family    "Tabschool",
                                  :font-size      120,
                                  :shadow-blur    5,
                                  :shadow-color   "#1a1a1a",
                                  :shadow-offset  {:x 5, :y 5},
                                  :shadow-opacity 0.5,
                                  :text           ""
                                  :vertical-align "middle"
                                  :metadata       {:display-name "Cover title"
                                                   :page-idx     0
                                                   :text-idx     0}
                                  :editable?      {:select true}}
         :spread-1-title-letters {:type           "text",
                                  :x              900,
                                  :y              264,
                                  :width          680,
                                  :height         200,
                                  :align          "center",
                                  :chunks         [],
                                  :fill           "black",
                                  :font-family    "Tabschool",
                                  :font-size      90,
                                  :text           "",
                                  :vertical-align "middle"
                                  :metadata       {:display-name "First page letters"
                                                   :page-idx     1
                                                   :text-idx     0}
                                  :editable?      {:select true}}
         :spread-1-title-text    {:type           "text",
                                  :x              940,
                                  :y              164,
                                  :width          600,
                                  :height         800,
                                  :align          "center",
                                  :chunks         [],
                                  :fill           "black",
                                  :font-family    "Tabschool",
                                  :font-size      90,
                                  :text           "",
                                  :vertical-align "middle"
                                  :metadata       {:display-name "First page text"
                                                   :page-idx     1
                                                   :text-idx     1}
                                  :editable?      {:select true}}
         :left-page-click-area   {:type    "transparent"
                                  :x       220
                                  :y       100
                                  :width   710
                                  :height  920
                                  :actions {:click {:id "handle-left-page-click" :on "click" :type "action"}}}
         :right-page-click-area  {:type    "transparent"
                                  :x       930
                                  :y       100
                                  :width   710
                                  :height  920
                                  :actions {:click {:id "handle-right-page-click" :on "click" :type "action"}}}
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
        :scene-objects [["background"]
                        ["book" "left-page-click-area" "right-page-click-area" "spread-0" "spread-1"]
                        ["next-button" "prev-button"]]
        :actions
        {:finish-activity             {:type "sequence-data",
                                       :data [{:type "action" :id "dialog-finish-activity"}
                                              {:type "finish-activity"}]},
         :handle-left-page-click      {:type       "test-expression"
                                       :expression [">" "@current-spread" 1]
                                       :success    {:type     "action"
                                                    :from-var [{:template        (spread-idx->dialog-name "%" :left)
                                                                :var-name        "current-spread"
                                                                :action-property "id"}]}}
         :handle-right-page-click     {:type       "test-expression"
                                       :expression [">" "@current-spread" 1]
                                       :success    {:type     "action"
                                                    :from-var [{:template        (spread-idx->dialog-name "%" :right)
                                                                :var-name        "current-spread"
                                                                :action-property "id"}]}}
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
                                              {:type "action" :from-var [{:template "spread-%", :var-name "current-spread", :action-property "id"}]}
                                              {:type "action" :id "test-completed"}]}
         :prev-page                   {:type "sequence-data"
                                       :data [{:type "remove-flows" :flow-tag "dialog"}
                                              {:type "action" :id "stop-timeout"}
                                              {:type "action" :id "turn-prev-page"}
                                              {:type "action" :from-var [{:template "spread-%", :var-name "current-spread", :action-property "id"}]}
                                              {:type "action" :id "test-completed"}]}
         :open-page                   {:type "sequence-data"
                                       :data [{:type "remove-flows" :flow-tag "dialog"}
                                              {:type "skip"}
                                              {:type "action" :id "stop-timeout"}
                                              {:type "action" :id "turn-open-page"}
                                              {:type "action" :from-var [{:template "spread-%", :var-name "current-spread", :action-property "id"}]}
                                              {:type "action" :id "test-completed"}]}
         :close-page                  {:type "sequence-data"
                                       :data [{:type "remove-flows" :flow-tag "dialog"}
                                              {:type "skip"}
                                              {:type "action" :id "stop-timeout"}
                                              {:type "action" :id "turn-close-page"}
                                              {:type "action" :from-var [{:template "spread-%", :var-name "current-spread", :action-property "id"}]}
                                              {:type "action" :id "test-completed"}]}
         :test-completed              {:type       "test-expression"
                                       :expression ["eq" ["dec" "@total-spreads"] "@current-spread"]
                                       :success    {:type "action" :id "finish-activity"}
                                       :fail       {:type "action" :id "start-timeout"}}
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
         :turn-open-page              {:type         "sequence-data"
                                       :on-interrupt {:type "sequence-data"
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:type "set-attribute" :attr-name "opacity", :attr-value 0 :target "spread-0"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "spread-0"}
                                                             {:id "open", :type "state", :target "background"}
                                                             {:id "idle", :type "add-animation", :target "book"}
                                                             {:type "set-variable", :var-name "current-spread", :var-value 1}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value true :target "spread-1"}
                                                             {:type "set-attribute" :attr-name "opacity", :attr-value 1 :target "spread-1"}
                                                             {:type "action" :id "show-navigation"}]}
                                       :data         [{:type "action" :id "hide-navigation"}
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
         :turn-close-page             {:type         "sequence-data"
                                       :on-interrupt {:type "sequence-data"
                                                      :data [{:type "action" :id "hide-navigation"}
                                                             {:type "set-attribute" :attr-name "opacity", :attr-value 0 :target "spread-1"}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value false :target "spread-1"}
                                                             {:id "closed", :type "state", :target "background"}
                                                             {:id "close_idle", :type "add-animation", :target "book"}
                                                             {:type "set-variable", :var-name "current-spread", :var-value 0}
                                                             {:type "set-attribute" :attr-name "visible", :attr-value true :target "spread-0"}
                                                             {:type "set-attribute" :attr-name "opacity", :attr-value 1 :target "spread-0"}
                                                             {:type "action" :id "show-navigation"}]}
                                       :data         [{:type "action" :id "hide-navigation"}
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
                                       :data [{:type       "test-expression"
                                               :expression ["eq" "@current-spread" 0]
                                               :success    {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}
                                               :fail       {:type       "test-expression"
                                                            :expression ["eq" ["dec" "@total-spreads"] "@current-spread"]
                                                            :success    {:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                            :fail       {:type "sequence-data"
                                                                         :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                                {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}}}]}

         :start-scene                 {:type "sequence-data",
                                       :data [{:type "start-activity"},
                                              {:type "set-variable", :var-name "current-spread", :var-value 0},
                                              {:type "action" :id "set-total-spreads-number"},
                                              {:type "action" :id "show-navigation"}
                                              {:type "action" :id "dialog-intro"}]},
         :set-total-spreads-number    {:type "set-variable", :var-name "total-spreads", :var-value 2}
         :stop-activity               {:type "stop-activity"}
         :start-timeout               {:type      "start-timeout-counter",
                                       :id        "inactive-counter",
                                       :action    "continue-try",
                                       :autostart true
                                       :interval  15000}
         :stop-timeout                {:type "remove-interval" :id "inactive-counter"}
         :continue-try                {:type "sequence-data",
                                       :data [{:type "action" :id "dialog-timeout-instructions"}
                                              {:type "action" :id "start-timeout"}]},

         :spread-0                    (-> (dialog/default "Front page")
                                          (assoc :tags ["dialog"]))
         :spread-1                    (-> (dialog/default "Title page")
                                          (assoc :tags ["dialog"]))
         :dialog-finish-activity      (dialog/default "Finish activity")
         :dialog-intro                (-> (dialog/default "Intro")
                                          (assoc :available-activities ["highlight-prev" "highlight-next" "turn-open-page" "turn-close-page"]))
         :dialog-timeout-instructions (-> (dialog/default "Timeout instructions")
                                          (assoc :tags ["dialog"])
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
        :metadata      {:autostart       true
                        :last-spread-idx 1
                        :tracks          [{:title "Book"
                                           :nodes [{:type      "dialog"
                                                    :action-id :dialog-intro}
                                                   {:type      "dialog"
                                                    :action-id :spread-0}
                                                   {:type      "dialog"
                                                    :action-id :spread-1}
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
          (assoc-in [:objects :spread-1-title-text :chunks] (text-utils/text->chunks (:subtitle args)))))

(defn f
  [args]
  (-> t
      (set-data args)
      (assoc-in [:metadata :actions] (:actions m))))

(defn fu
  [old-data {:keys [action-name] :as args}]
  (case action-name
    "add" (add-spread old-data args)
    "edit" (set-data old-data args)))

(core/register-template
  m f fu)

