(ns webchange.templates.library.interactive-read-aloud-import
  (:require
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]
    [webchange.templates.utils.characters :as characters]
    [webchange.course.core :as course]
    [webchange.question.create :as question-object]
    [webchange.question.get-question-data :refer [form->question-data]]))

(def metadata {:id          45
               :name        "Interactive Read Aloud (Import)"
               :tags        ["Direct Instruction - Animated Instructor"]
               :description "Model reading comprehension strategies with this activity. Begin with a mini-lesson, then reference a read aloud book of your choice while inserting explanations and pop-up questions throughout the lesson."

               :options     (concat [{:key   "characters"
                                      :label "Characters"
                                      :type  "characters"
                                      :max   4}
                                     {:key "book"
                                      :label "Book"
                                      :type "string"}])
               :actions     {:add-dialog   {:title   "Add dialogue",
                                            :options {:dialog {:label       "Dialogue"
                                                               :placeholder "Name the dialogue"
                                                               :type        "string"}}}
                             :add-question-object {:title   "Add question",
                                                   :options {:question-page-object {:label         "Question"
                                                                                    :type          "question-object"
                                                                                    :answers-label "Answers"
                                                                                    :max-answers   4}}}}})

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
                                                          {:type "flipbook-init" :target "book" :read false}
                                                          {:type "action" :id "dialog-main"}]
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

                               :next-page       {:type   "flipbook-flip-forward"
                                                 :target "book"
                                                 :read   false}

                               :prev-page       {:type   "flipbook-flip-backward"
                                                 :target "book"
                                                 :read   false}

                               :read-left-page  {:type "flipbook-read-left" :target "book"}
                               :read-right-page {:type "flipbook-read-right" :target "book"}

                               :dialog-main (dialog/default "Main")}
               :triggers
                              {:start {:on "start", :action "script"}}
               :metadata      {:tracks            [{:title "Sequence"
                                                    :nodes [{:type      "dialog"
                                                             :action-id "dialog-main"}]}]
                               :next-action-index 0
                               :stage-size        :contain
                               :available-actions [{:action "show-book"
                                                    :links  [{:type "object" :id "book"}]
                                                    :name   "Show book"}
                                                   {:action "hide-book"
                                                    :links  [{:type "object" :id "book"}]
                                                    :name   "Hide book"}
                                                   {:action "next-page"
                                                    :links  [{:type "object" :id "book"}]
                                                    :name   "Next page"}
                                                   {:action "prev-page"
                                                    :links  [{:type "object" :id "book"}]
                                                    :name   "Prev page"}]}})

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
      (update-in [:metadata :tracks 0 :nodes] conj {:type "question" :action-id action-name})
      (update-in [:metadata :available-actions] concat [{:action action-name
                                                         :name   (str "Ask " action-name)}])))

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

(defn- add-dialog
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "dialog-" index)
        default-dialog (dialog/default (str "dialog-" index))]
    (cond-> activity-data
            :always (increase-next-action-index)
            :always (place-dialog {(keyword action-name) default-dialog} action-name))))

(defn- add-question
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "question-" index)
        question-actions (question/create (:question-page args) {:suffix           index
                                                                 :action-name      action-name})
        question-assets (question/get-assets (:question-page args))]
    (-> activity-data
        (increase-next-action-index)
        (place-question question-actions action-name)
        (add-assets question-assets))))

(defn- add-question-object
  [activity-data {:keys [question-page-object]}]
  (let [index (get-next-action-index activity-data)
        next-action-name "script"
        action-name (str "question-" index)
        object-name (str "question-" index)
        question-data (question-object/create
                        (form->question-data question-page-object)
                        {:suffix           index
                         :action-name      action-name
                         :object-name      object-name
                         :next-action-name next-action-name})]
    (-> activity-data
        (increase-next-action-index)
        (question-object/add-to-scene question-data))))

(defn- apply-page-size
  [activity-data {:keys [width height background-color]}]
  (-> activity-data
      (assoc-in [:objects :book-background :width] (* width 2))
      (assoc-in [:objects :book-background :height] height)
      (assoc-in [:objects :book-background :fill] background-color)
      (assoc-in [:objects :book :width] (* width 2))
      (assoc-in [:objects :book :height] height)))

(defn- import-book
  [activity-data book-course-slug]
  (let [scene-slug "book"
        {:keys [assets objects]} (course/get-scene-latest-version book-course-slug scene-slug)]
    (-> activity-data
        (update :assets concat assets)
        (update :objects merge objects)
        (assoc-in [:objects :book :visible] false))))

(defn- create-template
  [props]
  (-> template
      (characters/add-characters (:characters props))
      (import-book (:book props))
      (update :scene-objects concat [["book-background"] ["book" "page-numbers"]])
      (apply-page-size page-params)
      (assoc-in [:metadata :actions] (:actions metadata))
      (assoc-in [:metadata :saved-props :wizard] props)))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog (add-dialog activity-data args)
    :add-question (add-question activity-data args)
    :add-question-object (add-question-object activity-data args)))

(core/register-template
  metadata
  create-template
  update-template)
