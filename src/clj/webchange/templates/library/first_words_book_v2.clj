(ns webchange.templates.library.first-words-book-v2
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.core :as core]
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

(def image-common-params {:type       "image"
                          :width      400
                          :height     400
                          :image-size "contain"})
(def image-left-params (merge image-common-params
                              {:x 420
                               :y 270}))
(def image-right-params (merge image-common-params
                               {:x 1050
                                :y 270}))

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
                                                   :text-idx     1}}
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
                                                   :text-idx     0}}
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
                                                   :text-idx     0}}
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
                                                   :text-idx     1}}
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
                        ["book" "spread-0" "spread-1"]
                        ["next-button" "prev-button"]]
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
                                       :data [{:type     "case",
                                               :from-var [{:template "spread-%" :var-name "current-spread", :action-property "value"}]
                                               :options
                                               {:spread-0 {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"},
                                                :spread-1 {:type "sequence-data"
                                                           :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                                  {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                ;:spread-2 {:type "sequence-data"
                                                ;           :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                ;                  {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                ;:spread-3 {:type "sequence-data"
                                                ;           :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}
                                                ;                  {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-button"}]}
                                                ;:spread-4 {:type "sequence-data"
                                                ;           :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-button"}]}
                                                }}]}

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
                                       :interval  15000}
         :stop-timeout                {:type "remove-interval" :id "inactive-counter"}
         :continue-try                {:type "sequence-data",
                                       :data [{:type "action" :id "dialog-timeout-instructions"}
                                              {:type "action" :id "start-timeout"}]},

         :dialog-spread-0             (-> (dialog/default "Front page")
                                          (assoc :tags ["dialog"]))
         :dialog-spread-1             (-> (dialog/default "Title page")
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
                                                    :action-id :dialog-spread-0}
                                                   {:type      "dialog"
                                                    :action-id :dialog-spread-1}
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

#_[{:data [{:type "empty", :duration 0}
           {:animation   "color",
            :fill        45823,
            :phrase-text "Apple",
            :start       5.43,
            :type        "text-animation",
            :duration    0.4800000000000004,
            :audio       "/upload/TVBYWNDGVKIEAZDJ.mp3",
            :target      "spread-2-left-text",
            :end         5.91,
            :data        [{:at       5.43,
                           :end      5.91,
                           :chunk    0,
                           :start    5.43,
                           :duration 0.4800000000000004}]}],
    :type "sequence-data"}
   {:data [{:type "empty", :duration "500"}
           {:animation   "color",
            :fill        45823,
            :phrase-text "Alligator",
            :start       6.48,
            :type        "text-animation",
            :duration    0.75,
            :audio       "/upload/TVBYWNDGVKIEAZDJ.mp3",
            :target      "spread-2-right-text",
            :end         7.23,
            :data        [{:at 6.48, :end 7.23, :chunk 0, :start 6.48, :duration 0.75}]}],
    :type "sequence-data"}]

;; Add Spread

(defn- spread-idx->spread-prefix
  ([spread-idx]
   (spread-idx->spread-prefix spread-idx nil))
  ([spread-idx side]
   (cond-> (str "spread-" spread-idx)
           (some? side) (str "-" (clojure.core/name side)))))

(defn- spread-idx->spread-name
  [spread-idx]
  (spread-idx->spread-prefix spread-idx))

(defn- spread-idx->dialog-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-dialog")))

(defn- spread-idx->text-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-text")))

(defn- spread-idx->image-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-image")))

(defn- add-dialog
  [activity-data spread-idx side text]
  (let [text-object-name (spread-idx->text-name spread-idx side)
        inner-action {:type        "text-animation"
                      :target      text-object-name
                      :phrase-text text
                      :animation   "color"
                      :fill        45823
                      :audio       nil
                      :data        []}

        dialog-name (-> (spread-idx->dialog-name spread-idx side) (keyword))
        dialog-data (-> (str "Spread " spread-idx " - " (case side
                                                          :left "Left"
                                                          :right "Right") " page")
                        (dialog/default {:inner-action-data inner-action})
                        (assoc :tags ["dialog"]))]
    (-> activity-data
        (assoc-in [:actions dialog-name] dialog-data)
        (update-in [:metadata :tracks 0 :nodes] conj {:type      "dialog"
                                                      :action-id dialog-name}))))

(defn- add-text
  [activity-data spread-idx side text]
  (let [text-name (-> (spread-idx->text-name spread-idx side) (keyword))
        text-data {:type           "text"
                   :text           text
                   :chunks         [{:start 0 :end (count text)}]
                   :x              (case side
                                     :left 300
                                     :right 950)
                   :y              700
                   :align          "center"
                   :vertical-align "top"
                   :width          600
                   :height         200
                   :word-wrap      true
                   :font-family    "Tabschool"
                   :font-size      98
                   :fill           "black"
                   :metadata       {:display-name (str "Spread " spread-idx " - " (case side :left "Left" :right "Right"))
                                    :page-idx     spread-idx
                                    :text-idx     (case side :left 0 :right 1)}}]
    (assoc-in activity-data [:objects text-name] text-data)))

(defn- add-image
  [activity-data spread-idx side {:keys [src]}]
  (let [image-name (-> (spread-idx->image-name spread-idx side) (keyword))
        image-data {:type       "image"
                    :src        src
                    :x          (case side
                                  :left 420
                                  :right 1050)
                    :y          270
                    :width      400
                    :height     400
                    :image-size "contain"}]
    (assoc-in activity-data [:objects image-name] image-data)))

(defn- add-dialogs
  [activity-data spread-idx {:keys [text-left text-right]}]
  (-> activity-data
      (add-dialog spread-idx :left text-left)
      (add-dialog spread-idx :right text-right)))

(defn- add-texts
  [activity-data spread-idx {:keys [text-left text-right]}]
  (-> activity-data
      (add-text spread-idx :left text-left)
      (add-text spread-idx :right text-right)))

(defn- add-images
  [activity-data spread-idx {:keys [image-left image-right]}]
  (-> activity-data
      (add-image spread-idx :left image-left)
      (add-image spread-idx :right image-right)))

(defn- add-spread-object
  [activity-data spread-idx]
  (let [spread-name (spread-idx->spread-name spread-idx)
        spread-data {:type      "group"
                     :visible   false
                     :children  [(spread-idx->text-name spread-idx :left)
                                 (spread-idx->text-name spread-idx :right)
                                 (spread-idx->image-name spread-idx :left)
                                 (spread-idx->image-name spread-idx :right)]
                     :editable? {:show-in-tree? true}}]     ;; ToDo: change to :metadata
    (-> activity-data
        (assoc-in [:objects (keyword spread-name)] spread-data)
        (update-in [:scene-objects 1] #(-> (conj % spread-name) (vec))))))

(defn- add-spread-action
  [activity-data spread-idx]
  (let [action-name (-> (spread-idx->spread-name spread-idx) (keyword))
        action-data {:type "sequence"
                     :data [(spread-idx->dialog-name spread-idx :left)
                            (spread-idx->dialog-name spread-idx :right)]}]
    (assoc-in activity-data [:actions action-name] action-data)))

(defn- add-spread
  [activity-data args]
  {:pre [(string? (:text-left args))
         (string? (:text-right args))
         (string? (get-in args [:image-left :src]))
         (string? (get-in args [:image-left :src]))]}
  (let [current-spread-idx (-> activity-data (get-in [:metadata :last-spread-idx]) (inc))]
    (-> activity-data
        (assoc-in [:metadata :last-spread-idx] current-spread-idx)
        (add-spread-object current-spread-idx)
        (add-spread-action current-spread-idx)
        (add-dialogs current-spread-idx args)
        (add-texts current-spread-idx args)
        (add-images current-spread-idx args))))

(defn f
  [args]
  (-> t
      (set-data args)
      (assoc-in [:metadata :actions] (:actions m))))

(defn fu
  [old-data {:keys [action-name] :as args}]
  (log/debug ">>" action-name)
  (case action-name
    "add" (add-spread old-data args)
    "edit" (set-data old-data args)))

(core/register-template
  m f fu)
