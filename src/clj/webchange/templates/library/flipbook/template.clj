(ns webchange.templates.library.flipbook.template
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.core :as core]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.custom-spread :as custom-spread]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]))

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
                                                   :default "spread"
                                                   :options [{:name "Page" :value "page"}
                                                             {:name "Spread" :value "spread"}]}
                                                  {:key        :layout
                                                   :type       "lookup-image"
                                                   :label      "Page layout"
                                                   :image-size 4
                                                   :options    [{:src   "/images/templates/page_layout/text_big_at_bottom.png"
                                                                 :value :text-big-at-bottom}
                                                                {:src   "/images/templates/page_layout/text_small_at_bottom.png"
                                                                 :value :text-small-at-bottom}
                                                                {:src   "/images/templates/page_layout/only_image.png"
                                                                 :value :image-only}
                                                                {:src   "/images/templates/page_layout/text_at_top.png"
                                                                 :value :text-at-top}
                                                                {:src   "/images/templates/page_layout/text_small_transparent.png"
                                                                 :value :text-small-transparent}
                                                                {:src   "/images/templates/page_layout/text_full_page.png"
                                                                 :value :text-only}]}
                                                  {:key        :text
                                                   :type       "string"
                                                   :label      "Text"
                                                   :default    "Page text example"
                                                   :conditions [{:key   :layout
                                                                 :state :not-in
                                                                 :value [:image-only]}]}
                                                  {:key        :image
                                                   :type       "image"
                                                   :label      "Image"
                                                   :conditions [{:key   :layout
                                                                 :state :not-in
                                                                 :value [:text-only]}]}]}}})

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xff9800
                  :border-color     0xe46a02
                  :text-color       0x000000})

(defn- get-action-data
  [{:keys [action-name text-name text-value]}]
  {:type               "sequence-data"
   :editor-type        "dialog"
   :concept-var        "current-word"
   :data               [{:type "sequence-data"
                         :data [{:type "empty" :duration 0}
                                {:data        []
                                 :type        "text-animation"
                                 :audio       nil,
                                 :target      text-name
                                 :animation   "bounce"
                                 :phrase-text text-value}]}]
   :phrase             action-name
   :phrase-description "Page Action"})

(defn- add-text-animation-action
  [activity-data page-position {:keys [name objects text-name]}]
  (let [action-name (str name "-action")
        action-data (get-action-data {:action-name action-name
                                      :text-name   text-name
                                      :text-value  (get-in objects [(keyword text-name) :text])})]
    (-> activity-data
        (assoc-in [:objects :book :pages page-position :action] action-name)
        (assoc-in [:actions (keyword action-name)] action-data))))

(defn- insert-to
  [list item position]
  (let [position (if (< position 0)
                   (+ position (count list))
                   position)
        [before after] (split-at position list)]
    (vec (concat before [item] after))))

(defn- add-page-to-book
  [activity-data {:keys [with-action?]} {:keys [name resources objects text-name] :as page-data}]
  (let [current-pages-count (count (get-in activity-data [:objects :book :pages] []))
        new-page-position (if with-action? (- current-pages-count 1) current-pages-count)]
    (cond-> (-> activity-data
                (update :assets concat resources)
                (update :objects merge objects)
                (update-in [:objects :book :pages] insert-to {:object name} new-page-position))
            (and with-action?
                 (some? text-name)) (add-text-animation-action new-page-position page-data))))

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
      (add-page front-cover/create {:layout    (keyword cover-layout)
                                    :image-src (:src cover-image)
                                    :title     cover-title
                                    :authors   authors})
      (add-page generic-front/create)
      (add-page credits/create {:title        cover-title
                                :authors      authors
                                :illustrators illustrators})
      (add-page back-cover/create {:image-src    (:src cover-image)
                                   :authors      authors
                                   :illustrators illustrators})
      (assoc-in [:metadata :actions] (:actions metadata))))

(defn update-activity
  [activity-data {:keys [type layout image text]}]
  (-> activity-data
      (add-page
        (case type
          "page" custom-page/create
          "spread" custom-spread/create)
        {:page-type    (keyword layout)
         :image-src    (:src image)
         :text         text
         :with-action? true})))

(core/register-template
  (:id metadata) metadata create-activity update-activity)
