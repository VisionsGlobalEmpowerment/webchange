(ns webchange.templates.library.flipbook.template
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.custom-spread :as custom-spread]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]
    [webchange.templates.library.flipbook.page-number :refer [update-pages-numbers]]
    [webchange.templates.library.flipbook.remove-page :refer [remove-page]]
    [webchange.templates.library.flipbook.reorder-page :refer [move-page]]
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.utils.list :refer [insert-at-position]]
    [clojure.tools.logging :as log]))

(def metadata {:id          24
               :name        "flipbook"
               :tags        ["Book Creator"]
               :description "Some description of flipbook mechanics and covered skills"
               :lesson-sets []
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
                                            :max   3}}
               :actions     {:add-page {:title   "Add page",
                                        :options [{:key     :type
                                                   :type    "lookup"
                                                   :label   "Type"
                                                   :default "page"
                                                   :options [{:name "Page" :value "page"}
                                                             {:name    "Spread" :value "spread"
                                                              :enable? {:key   [:metadata :flipbook-pages :current-side]
                                                                        :state :equal
                                                                        :value "right"}}]}
                                                  {:key        :page-layout
                                                   :type       "lookup-image"
                                                   :label      "Page layout"
                                                   :image-size 1
                                                   :default    :text-big-at-bottom
                                                   :conditions [{:key   :type
                                                                 :state :in
                                                                 :value ["page"]}]
                                                   :options    [{:src   "/images/templates/page_layout/text_big_at_bottom.png"
                                                                 :value :text-big-at-bottom}
                                                                {:src   "/images/templates/page_layout/text_small_at_bottom.png"
                                                                 :value :text-small-at-bottom}
                                                                {:src   "/images/templates/page_layout/only_image.png"
                                                                 :value :image-only}
                                                                {:src   "/images/templates/page_layout/text_at_top.png"
                                                                 :value :text-at-top}
                                                                {:src   "/images/templates/page_layout/text_full_page.png"
                                                                 :value :text-only}
                                                                {:src   "/images/templates/page_layout/icons_16.png"
                                                                 :value :text-over-image-at-top}
                                                                {:src   "/images/templates/page_layout/icons_17.png"
                                                                 :value :text-over-image-at-bottom}]}
                                                  {:key        :spread-layout
                                                   :type       "lookup-image"
                                                   :label      "Spread layout"
                                                   :image-size 2
                                                   :default    :text-right-top
                                                   :conditions [{:key   :type
                                                                 :state :in
                                                                 :value ["spread"]}]
                                                   :options    [{:src   "/images/templates/page_layout/spread_text_right_top.png"
                                                                 :value :text-right-top}
                                                                {:src   "/images/templates/page_layout/spread_text_right_bottom.png"
                                                                 :value :text-right-bottom}
                                                                {:src   "/images/templates/page_layout/spread_text_left_top.png"
                                                                 :value :text-left-top}
                                                                {:src   "/images/templates/page_layout/spread_text_left_bottom.png"
                                                                 :value :text-left-bottom}]}
                                                  {:key        :text
                                                   :type       "string"
                                                   :label      "Text"
                                                   :conditions [{:key   :page-layout
                                                                 :state :not-in
                                                                 :value [:image-only]}]}
                                                  {:key        :image
                                                   :type       "image"
                                                   :label      "Image"
                                                   :conditions [{:key   :page-layout
                                                                 :state :not-in
                                                                 :value [:text-only]}]}]}}})

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xffffff
                  :border-color     0xdadada
                  :text-color       0x000000})

(defn- get-action-data
  [{:keys [action-name text-name text-value]}]
  {:type               "sequence-data"
   :editor-type        "dialog"
   :concept-var        "current-word"
   :data               [{:type "empty" :duration 0}
                        {:data        []
                         :type        "text-animation"
                         :audio       nil,
                         :target      text-name
                         :animation   "color"
                         :fill        0x00B2FF
                         :phrase-text text-value}]
   :phrase             action-name
   :phrase-description "Page Action"})

(defn- add-text-animation-action
  [activity-data page-position {:keys [name objects text-name action]}]
  (let [{:keys [name data]} (if (some? action)
                              action
                              (let [action-name (str name "-action")]
                                {:name action-name
                                 :data (get-action-data {:action-name action-name
                                                         :text-name   text-name
                                                         :text-value  (get-in objects [(keyword text-name) :text])})}))]
    (-> activity-data
        (assoc-in [:objects :book :pages page-position :action] name)
        (assoc-in [:actions (keyword name)] data))))

(defn- add-page-to-book
  [activity-data
   {:keys [with-action? shift-from-end removable?]
    :or   {shift-from-end 0
           removable?     true}}
   {:keys [name resources objects text-name action] :as page-data}]
  (let [book-object-name :book
        current-pages-count (count (get-in activity-data [:objects book-object-name :pages] []))
        new-page-position (- current-pages-count shift-from-end)]
    (cond-> (-> activity-data
                (update :assets concat resources)
                (update :objects merge objects)
                (update-in [:objects book-object-name :pages] insert-at-position {:object name :text text-name :removable? removable?} new-page-position)
                (update-stages {:book-name book-object-name}))
            (and with-action?
                 (or (some? action)
                     (some? text-name))) (add-text-animation-action new-page-position page-data))))

(defn- add-pages-to-book
  [activity-data content-data pages-data]
  (if (sequential? pages-data)
    (reduce (fn [activity-data page-data]
              (add-page-to-book activity-data content-data page-data))
            activity-data
            pages-data)
    (add-page-to-book activity-data content-data pages-data)))

(defn- add-page
  ([activity-data page-constructor]
   (add-page activity-data page-constructor {}))
  ([activity-data page-constructor content-data]
   (->> (page-constructor page-params content-data)
        (add-pages-to-book activity-data content-data))))

(defn create-activity
  [{:keys [authors illustrators cover-layout cover-image cover-title]}]
  (-> (get-template page-params)
      (add-page front-cover/create {:layout       (keyword cover-layout)
                                    :image-src    (:src cover-image)
                                    :title        cover-title
                                    :authors      authors
                                    :with-action? true
                                    :removable?   false})
      (add-page generic-front/create {:removable? false})
      (add-page credits/create {:title        cover-title
                                :authors      authors
                                :illustrators illustrators
                                :with-action? true
                                :removable?   false})
      (add-page back-cover/create {:image-src    (:src cover-image)
                                   :authors      authors
                                   :illustrators illustrators
                                   :removable?   false})
      (assoc-in [:metadata :actions] (:actions metadata))))

(defn add-page-handler
  [activity-data {:keys [type page-layout spread-layout image text]}]
  (let [[constructor layout] (case type
                               "page" [custom-page/create (keyword page-layout)]
                               "spread" [custom-spread/create (keyword spread-layout)])]
    (-> activity-data
        (add-page
          constructor
          {:page-type      layout
           :image-src      (:src image)
           :text           text
           :with-action?   true
           :shift-from-end 1})
        (update-pages-numbers page-params))))

(defn update-activity
  [activity-data props action]
  (case action
    "add-page" (add-page-handler activity-data props)
    "remove-page" (remove-page activity-data props page-params)
    "move-page" (move-page activity-data props page-params)))

(core/register-template
  metadata create-activity {:handler update-activity
                            :props   {:with-action? true}})
