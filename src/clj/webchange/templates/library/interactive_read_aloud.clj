(ns webchange.templates.library.interactive-read-aloud
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.characters :as characters]
    [webchange.templates.utils.question :as question]
    [webchange.templates.library.flipbook.template :refer [book-options page-options]]
    [webchange.templates.library.flipbook.add-page :refer [add-page]]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.custom-spread :as custom-spread]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]
    [webchange.question.create :as question-object]
    [webchange.question.get-question-data :refer [form->question-data]]))

(def metadata {:id          32
               :name        "Interactive Read Aloud"
               :tags        ["Direct Instruction - Animated Instructor"]
               :description "Model reading comprehension strategies with this activity. Begin with a mini-lesson, then reference a read aloud book of your choice while inserting explanations and pop-up questions throughout the lesson."
               :props       {:game-changer? true}
               :options     (concat [{:key   "characters"
                                      :label "Characters"
                                      :type  "characters"
                                      :max   4}]
                                    book-options)
               :actions     {:add-dialog          {:title   "Add dialogue",
                                                   :options {:dialog {:label       "Dialogue"
                                                                      :placeholder "Name the dialogue"
                                                                      :type        "string"}}}
                             :add-page            {:title   "Add page"
                                                   :options page-options}
                             :add-question        {:title   "Add question",
                                                   :options {:question-page {:label         "Question"
                                                                             :type          "question"
                                                                             :answers-label "Answers"
                                                                             :max-answers   4}}}
                             :add-question-object {:title   "Add question object",
                                                   :options {:question-page-object {:label         "Question"
                                                                                    :type          "question-object"
                                                                                    :answers-label "Answers"
                                                                                    :max-answers   4}}}
                             :config-title        {:title   "Frontpage"
                                                   :options book-options}}})

(def empty-audio {:audio "" :start 0 :duration 0 :animation "color" :fill 0x00B2FF :data []})
(def template {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                               {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                               {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}]
               :objects       {:layered-background {:type       "layered-background"
                                                    :background {:src "/raw/img/casa/background_casa.png"}
                                                    :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                                    :surface    {:src "/raw/img/casa/surface_casa.png"}}

                               :book-background    {:type       "rectangle"
                                                    :x          0
                                                    :y          0
                                                    :transition "book-background"
                                                    :width      "---"
                                                    :height     "---"
                                                    :fill       "---"
                                                    :visible    false}

                               :book               {:type       "flipbook"
                                                    :transition "book"
                                                    :x          0
                                                    :y          0
                                                    :width      1920
                                                    :height     1080
                                                    :pages      []
                                                    :visible    false}}
               :scene-objects [["layered-background"]]
               :actions       {:script          {:type   "workflow"
                                                 :data   [{:type "start-activity"}
                                                          {:type "action" :id "dialog-intro"}
                                                          {:type "action" :id "front-page"}]
                                                 :on-end "finish"}
                               :finish
                                                {:type "finish-activity"}
                               :show-book
                                                {:type "parallel"
                                                 :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "book-background"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value true :target "book"}]}
                               :hide-book
                                                {:type "parallel"
                                                 :data [{:type "set-attribute" :attr-name "visible", :attr-value false :target "book-background"}
                                                        {:type "set-attribute" :attr-name "visible", :attr-value false :target "book"}]}

                               :next-page       {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-forward"
                                                         :target "book"
                                                         :read   false}]}

                               :read-left-page  {:type "flipbook-read-left" :target "book"}
                               :read-right-page {:type "flipbook-read-right" :target "book"}

                               :dialog-intro
                                                {:type               "sequence-data",
                                                 :editor-type        "dialog",
                                                 :concept-var        "current-word",
                                                 :data               [{:type "sequence-data"
                                                                       :data [{:type "empty" :duration 0}
                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                 :phrase             "instruction",
                                                 :phrase-description "Instruction"}

                               :front-page
                                                {:type "sequence-data"
                                                 :data [{:type "action" :id "show-book"}
                                                        {:type "flipbook-init" :target "book" :read false}]}}
               :triggers
                              {:start {:on "start", :action "script"}}
               :metadata      {:tracks            [{:title "Sequence"
                                                    :nodes [{:type      "dialog"
                                                             :action-id "dialog-intro"}
                                                            {:type "prompt"
                                                             :text "Show book"}]}]
                               :next-action-index 0
                               :stage-size        :contain
                               :stages            []
                               :flipbook-name     "book"
                               :flipbook-pages    {:total        0
                                                   :current-side "right"
                                                   :visible      true}}})

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xffffff
                  :border-color     0xdadada
                  :text-color       0x000000})

(defn- get-next-action-index
  [activity-data]
  (get-in activity-data [:metadata :next-action-index]))

(defn- increase-next-action-index
  [activity-data]
  (update-in activity-data [:metadata :next-action-index] inc))

(defn- place-question
  [activity-data actions action-name]
  (-> activity-data
      (update :actions merge actions)
      (update-in [:actions :script :data] conj {:type "action" :id action-name :workflow-user-input true})
      (update-in [:metadata :tracks 0 :nodes] conj {:type "question" :action-id action-name})))

(defn- place-dialog
  [activity-data actions action-name]
  (-> activity-data
      (update :actions merge actions)
      (update-in [:actions :script :data] conj {:type "action" :id action-name})
      (update-in [:metadata :tracks 0 :nodes] conj {:type "dialog" :action-id action-name})))

(defn- add-assets
  [activity-data assets]
  (-> activity-data
      (update :assets concat assets)))

(defn- add-hide-to-sequence
  [activity-data]
  (-> activity-data
      (update-in [:actions :script :data] conj {:type "action" :id "hide-book"})
      (assoc-in [:metadata :flipbook-pages :visible] false)
      (update-in [:metadata :tracks 0 :nodes] conj {:type "prompt" :text "Hide book"})))

(defn- add-show-to-sequence
  [activity-data]
  (-> activity-data
      (update-in [:actions :script :data] conj {:type "action" :id "show-book"})
      (assoc-in [:metadata :flipbook-pages :visible] true)
      (update-in [:metadata :tracks 0 :nodes] conj {:type "prompt" :text "Show book"})))

(defn- add-read-page-to-sequence
  [activity-data page-position action-name _]
  (let [read-page (if (even? page-position)
                    "read-right-page"
                    "read-left-page")]
    (-> activity-data
        (update-in [:actions :script :data] conj {:type "action" :id read-page})
        (update-in [:metadata :tracks 0 :nodes] conj {:type "dialog" :action-id action-name}))))

(defn- add-flip-page
  [activity-data]
  (-> activity-data
      (update-in [:actions :script :data] conj {:type "action" :id "next-page"})
      (update-in [:metadata :tracks 0 :nodes] conj {:type "prompt" :text "Flip the page"})))

(defn- add-dialog
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "dialog-" index)
        default-dialog (dialog/default (str "dialog-" index))
        add-hide? (get-in activity-data [:metadata :flipbook-pages :visible])]
    (cond-> activity-data
            add-hide? (add-hide-to-sequence)
            :always (increase-next-action-index)
            :always (place-dialog {(keyword action-name) default-dialog} action-name))))

(defn- add-question
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        next-action-name "script"
        action-name (str "question-" index)
        question-actions (question/create (:question-page args) {:suffix           index
                                                                 :action-name      action-name
                                                                 :next-action-name next-action-name})
        question-assets (question/get-assets (:question-page args))]
    (-> activity-data
        (increase-next-action-index)
        (place-question question-actions action-name)
        (add-assets question-assets))))

(defn- add-question-object
  [activity-data {:keys [question-page-object]}]
  (let [index (get-next-action-index activity-data)
        action-name (str "question-" index)
        object-name (str "question-" index)
        question-data (question-object/create
                        (form->question-data question-page-object)
                        {:suffix           index
                         :action-name      action-name
                         :object-name      object-name})]
    (-> activity-data
        (increase-next-action-index)
        (question-object/add-to-scene question-data))))

(defn- add-page-action
  [activity-data {:keys [type page-layout spread-layout image text]}]
  (let [constructors (case type
                       "page" (custom-page/constructors (keyword page-layout))
                       "spread" (custom-spread/constructors (keyword spread-layout)))]
    (reduce (fn [activity-data constructor]
              (let [add-flip-node-to-sequence? (-> (get-in activity-data [:objects :book :pages] []) count even?)
                    add-show? (not (get-in activity-data [:metadata :flipbook-pages :visible]))]
                (cond-> activity-data
                        add-show? (add-show-to-sequence)
                        add-flip-node-to-sequence? (add-flip-page)
                        :always (add-page
                                  constructor
                                  page-params
                                  {:image-src                (:src image)
                                   :text                     text
                                   :with-action?             true
                                   :with-page-number?        true
                                   :shift-from-end           1
                                   :on-text-animation-action add-read-page-to-sequence}))))
            activity-data
            constructors)))

(defn- apply-page-size
  [activity-data {:keys [width height padding background-color]}]
  (let [flip-button-size {:width  95
                          :height 95}]
    (-> activity-data
        (assoc-in [:objects :book-background :width] (* width 2))
        (assoc-in [:objects :book-background :height] height)
        (assoc-in [:objects :book-background :fill] background-color)
        (assoc-in [:objects :book :width] (* width 2))
        (assoc-in [:objects :book :height] height)
        (assoc-in [:objects :prev-page :x] padding)
        (assoc-in [:objects :prev-page :y] (-> (- height (:height flip-button-size)) (- padding)))
        (assoc-in [:objects :next-page :x] (-> (* width 2) (- padding (:width flip-button-size))))
        (assoc-in [:objects :next-page :y] (-> (- height (:height flip-button-size)) (- padding))))))

(defn- create-template
  [{:keys [authors illustrators cover-layout cover-image cover-title] :as props}]
  (-> template
      (characters/add-characters (:characters props))
      (update :scene-objects concat [["book-background"] ["book"]])
      (apply-page-size page-params)
      (add-page front-cover/create page-params {:layout                   (keyword cover-layout)
                                                :image-src                (:src cover-image)
                                                :title                    cover-title
                                                :authors                  authors
                                                :with-action?             true
                                                :on-text-animation-action add-read-page-to-sequence})
      (add-flip-page)
      (add-page generic-front/create page-params)
      (add-page credits/create page-params {:title                    cover-title
                                            :authors                  authors
                                            :illustrators             illustrators
                                            :on-text-animation-action add-read-page-to-sequence})
      (add-page back-cover/create page-params {:image-src    (:src cover-image)
                                               :authors      authors
                                               :illustrators illustrators})
      (assoc-in [:metadata :actions] (:actions metadata))
      (assoc-in [:metadata :saved-props :wizard] props)))

(defn- config-frontpage
  [activity-data args]
  (let [objects (-> (create-template args)
                    :objects
                    (dissoc :book)
                    (dissoc :layered-background))]
    (-> activity-data
        (update-in [:objects] merge objects))))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog (add-dialog activity-data args)
    :add-page (add-page-action activity-data args)
    :add-question (add-question activity-data args)
    :add-question-object (add-question-object activity-data args)
    :config-title (config-frontpage activity-data args)))

(core/register-template
  metadata
  create-template
  update-template)
