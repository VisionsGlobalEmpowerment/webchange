(ns webchange.templates.library.interactive-read-aloud
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [webchange.utils.text :as text-utils]
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.question :as question]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.core :as core]

    [webchange.templates.utils.characters :as characters]

    [webchange.templates.library.flipbook.template :refer [add-page add-page-options]]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.custom-spread :as custom-spread]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]
    [webchange.templates.library.flipbook.page-number :refer [add-page-number]]
    [webchange.templates.library.flipbook.stages :refer [update-stages]]))

(def metadata {:id          23
               :name        "Interactive Read Aloud"
               :tags        ["Direct Instruction - Animated Instructor"]
               :description "Users learn new reading skills through a mini-lesson from a teacher character. Then, the teacher reads a book aloud (these books should be high-quality and longer/more rigorous than what students could read on their own) and stops to help users practice the skills they just learned."
               :options     {:characters {:label "Characters"
                                          :type  "characters"
                                          :max   3}
                             :cover-layout {:label   "Cover layout"
                                            :type    "lookup-image"
                                            :options [{:name  "Title at top"
                                                       :value :title-top
                                                       :src   "/images/templates/cover_layout/title_at_top.png"}
                                                      {:name  "Title at bottom"
                                                       :value :title-bottom
                                                       :src   "/images/templates/cover_layout/title_at_bottom.png"}]}
                             :cover-image  {:label "Cover image"
                                            :type  "image"}
                             :cover-title  {:label "Title"
                                            :type  "string"}
                             :authors      {:label "Authors"
                                            :type  "strings-list"
                                            :max   3}
                             :illustrators {:label "Illustrators"
                                            :type  "strings-list"
                                            :max   3}}
               :actions     {:add-dialog   {:title   "Add dialog",
                                            :options {:dialog {:label "Dialog"
                                                               :type  "string"}}}
                             :add-page     {:title "Add page"
                                            :options add-page-options}
                             :add-question {:title   "Add question",
                                            :options {:question-page {:label         "Question"
                                                                      :type          "questions-no-image"
                                                                      :answers-label "Answers"
                                                                      :max-answers   5}}}
                             :config-title {:title "Frontpage"
                                            :options     {:cover-layout {:label   "Cover layout"
                                                                         :type    "lookup-image"
                                                                         :options [{:name  "Title at top"
                                                                                    :value :title-top
                                                                                    :src   "/images/templates/cover_layout/title_at_top.png"}
                                                                                   {:name  "Title at bottom"
                                                                                    :value :title-bottom
                                                                                    :src   "/images/templates/cover_layout/title_at_bottom.png"}]}
                                                          :cover-image  {:label "Cover image"
                                                                         :type  "image"}
                                                          :cover-title  {:label "Title"
                                                                         :type  "string"}
                                                          :authors      {:label "Authors"
                                                                         :type  "strings-list"
                                                                         :max   3}
                                                          :illustrators {:label "Illustrators"
                                                                         :type  "strings-list"
                                                                         :max   3}}}}})

(def empty-audio {:audio "" :start 0 :duration  0 :animation "color" :fill 0x00B2FF :data []})
(def template {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                               {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                               {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}

                               {:url "/raw/flip_page/front.png" :size 1 :type "image"}

                               {:url "/raw/img/flipbook/next-page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/prev-page.png" :size 1 :type "image"}]
               :objects       {:layered-background {:type       "layered-background"
                                                    :background {:src "/raw/img/casa/background_casa.png"}
                                                    :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                                    :surface    {:src "/raw/img/casa/surface_casa.png"}}

                               :book-background {:type   "rectangle"
                                                 :x      0
                                                 :y      0
                                                 :width  "---"
                                                 :height "---"
                                                 :fill   "---"
                                                 :visible false}

                               :book      {:type         "flipbook"
                                           :transition   "book"
                                           :x            0
                                           :y            0
                                           :width        1920
                                           :height       1080
                                           :pages        []
                                           :prev-control "prev-page"
                                           :next-control "next-page"
                                           :visible false}
                               :prev-page {:type    "image"
                                           :x       "---"
                                           :y       "---"
                                           :width   95
                                           :height  95
                                           :src     "/raw/img/flipbook/prev-page.png"
                                           :actions {:click {:id "prev-page-click" :on "click" :type "action"}}
                                           :visible false}
                               :next-page {:type    "image"
                                           :x       "---"
                                           :y       "---"
                                           :width   95
                                           :height  95
                                           :src     "/raw/img/flipbook/next-page.png"
                                           :actions {:click {:id "next-page-click" :on "click" :type "action"}}
                                           :visible false}}
               :scene-objects [["layered-background"] ["book-background"] ["book"] ["prev-page" "next-page"]]
               :actions       {:script {:type   "workflow"
                                        :data   [{:type "action" :id "dialog-intro"}
                                                 {:type "action" :id "front-page"}]
                                        :on-end "finish"}
                               :finish
                                       {:type "finish-activity"}
                               :show-book
                                       {:type "parallel"
                                        :data [{:type "set-attribute" :attr-name "visible", :attr-value true :target "book-background"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value true :target "book"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value true :target "prev-page"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value true :target "next-page"}]}
                               :hide-book
                                       {:type "parallel"
                                        :data [{:type "set-attribute" :attr-name "visible", :attr-value false :target "book-background"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value false :target "book"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value false :target "prev-page"}
                                               {:type "set-attribute" :attr-name "visible", :attr-value false :target "next-page"}]}
                               :next-page
                                       {:type "sequence-data"
                                        :data [{:type     "set-attribute" :attr-name "visible", :attr-value false
                                                :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                               {:type "counter", :counter-id "current-page", :counter-action "increase"}
                                               {:type     "set-attribute" :attr-name "visible", :attr-value true
                                                :from-var [{:template "group-%", :var-name "current-page", :action-property "target"}]}
                                               {:type     "action"
                                                :from-var [{:template "dialog-%-page", :var-name "current-page", :action-property "id"}]}]}
                               :dialog-intro
                                       {:type               "sequence-data",
                                        :editor-type        "dialog",
                                        :concept-var        "current-word",
                                        :data               [{:type "sequence-data"
                                                              :data [{:type "empty" :duration 0}
                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                        :phrase             "instruction",
                                        :phrase-description "Instruction"}

                               :dialog-2-front-page
                                       {:type               "sequence-data",
                                        :editor-type        "dialog",
                                        :concept-var        "current-word",
                                        :data               [{:type "sequence-data"
                                                              :data [{:type "empty" :duration 0}
                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                        :phrase             "frontpage",
                                        :phrase-description "Frontpage"}
                               :front-page
                                       {:type "sequence-data"
                                        :data [{:type "action" :id "show-book"}
                                               {:type "action" :id "dialog-2-front-page"}]}}
               :metadata {:next-action-index 0
                          :stage-size     :contain
                          :stages         []
                          :flipbook-name  "book"
                          :flipbook-pages {:total        0
                                           :current-side "right"}}})

(def animations {:vera       {:width  380,
                              :height 537,
                              :scale  {:x 1, :y 1},
                              :speed  0.5
                              :meshes true
                              :name   "vera"
                              :skin   "01 Vera_1"}
                 :senoravaca {:width  351,
                              :height 717,
                              :scale  {:x 1, :y 1}
                              :speed  0.5
                              :meshes true
                              :name   "senoravaca"
                              :skin   "vaca"}
                 :mari       {:width  910,
                              :height 601,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name   "mari"
                              :skin   "01 mari"}})

(def character-positions
  [{:x 428
    :y 960}
   {:x 928
    :y 960}
   {:x 1428
    :y 960}])

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
      (update-in [:actions :script :data] conj {:type "action" :id action-name :workflow-user-input true})))

(defn- place-dialog
  [activity-data actions action-name]
  (-> activity-data
      (update :actions merge actions)
      (update-in [:actions :script :data] conj {:type "action" :id action-name})))

(defn- add-assets
  [activity-data assets]
  (-> activity-data
      (update :assets concat assets)))

(defn- add-dialog
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        action-name (str "dialog-" index)
        default-dialog (dialog/default (str "dialog-" index))]
    (-> activity-data
        (increase-next-action-index)
        (place-dialog {(keyword action-name) default-dialog} action-name))))

(defn- add-question
  [activity-data args]
  (let [index (get-next-action-index activity-data)
        next-action-name "script"
        action-name (str "question-" index)
        question-actions (question/create (:question-page args) {:suffix index
                                                                 :action-name action-name
                                                                 :next-action-name next-action-name})
        question-assets (question/get-assets (:question-page args))]
    (-> activity-data
        (increase-next-action-index)
        (place-question question-actions action-name)
        (add-assets question-assets))))

(defn- add-page-action
  [activity-data {:keys [type page-layout spread-layout image text]}]
  (let [[constructor layout] (case type
                               "page" [custom-page/create (keyword page-layout)]
                               "spread" [custom-spread/create (keyword spread-layout)])]
    (-> activity-data
        (add-page
          constructor
          {:page-type         layout
           :image-src         (:src image)
           :text              text
           :with-action?      true
           :with-page-number? true
           :shift-from-end    1}))))

(defn- config-frontpage
  [activity-data args]
  activity-data)

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

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xffffff
                  :border-color     0xdadada
                  :text-color       0x000000})

(defn- create-template
  [{:keys [authors illustrators cover-layout cover-image cover-title] :as args}]
  (-> template
      (characters/add-characters (:characters args) character-positions animations)
      (apply-page-size page-params)
      (add-page front-cover/create {:layout       (keyword cover-layout)
                                    :image-src    (:src cover-image)
                                    :title        cover-title
                                    :authors      authors
                                    :with-action? true})
      (add-page generic-front/create)
      (add-page credits/create {:title        cover-title
                                :authors      authors
                                :illustrators illustrators})
      (add-page back-cover/create {:image-src    (:src cover-image)
                                   :authors      authors
                                   :illustrators illustrators})
      (assoc-in [:metadata :actions] (:actions metadata))
      (assoc-in [:metadata :history] [{:type :create :args args}])))

(defn- update-template
  [activity-data {action-name :action-name :as args}]
  (case (keyword action-name)
    :add-dialog (add-dialog activity-data args)
    :add-page (add-page-action activity-data args)
    :add-question (add-question activity-data args)
    :config-title (config-frontpage activity-data args)))

(core/register-template
  metadata
  create-template
  update-template)
