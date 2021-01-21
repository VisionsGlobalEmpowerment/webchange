(ns webchange.templates.library.flipbook.template
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.front-cover :as front-cover]
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
                                        :options [{:key        :layout
                                                   :type       "lookup-image"
                                                   :label      "Cover layout"
                                                   :image-size 4
                                                   :options    [{:src   "/images/templates/page_layout/text_big_at_bottom.png"
                                                                 :value :text-big-at-bottom}
                                                                {:src   "/images/templates/page_layout/text_small_at_bottom.png"
                                                                 :value :text-small-at-bottom}
                                                                {:src   "/images/templates/page_layout/only_image.png"
                                                                 :value :only-image}
                                                                {:src   "/images/templates/page_layout/text_at_top.png"
                                                                 :value :text-at-top}
                                                                {:src   "/images/templates/page_layout/text_full_page.png"
                                                                 :value :only-text}]}
                                                  {:key        :text
                                                   :type       "string"
                                                   :label      "Text"
                                                   :conditions [{:key   :layout
                                                                 :state :not-in
                                                                 :value [:only-image]}]}
                                                  {:key        :image
                                                   :type       "image"
                                                   :label      "Image"
                                                   :conditions [{:key   :layout
                                                                 :state :not-in
                                                                 :value [:only-text]}]}]}}})

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xff9800
                  :border-color     0xe46a02
                  :text-color       0x000000})

(defn- add-page-to-book
  [activity-data {:keys [name resources objects]}]
  (-> activity-data
      (update :assets concat resources)
      (update :objects merge objects)
      (update-in [:objects :book :pages] conj name)))

(defn- add-page
  ([activity-data page-constructor]
   (add-page activity-data page-constructor {}))
  ([activity-data page-constructor content-data]
   (->> (page-constructor page-params content-data)
        (add-page-to-book activity-data))))

(defn create-activity
  [{:keys [authors illustrators cover-layout cover-image cover-title] :as props}]
  (let []
    (-> (get-template page-params)
        (add-page front-cover/create {:layout    (keyword cover-layout)
                                      :image-src (:src cover-image)
                                      :title     cover-title
                                      :authors   authors})
        (add-page generic-front/create)
        (add-page credits/create {:title        cover-title
                                  :authors      authors
                                  :illustrators illustrators})
        (assoc-in [:metadata :actions] (:actions metadata)))))

(defn update-activity
  [activity-data {:keys [layout image text]}]
  (-> activity-data
      (add-page custom-page/create {:page-type (keyword layout)
                                    :image-src (:src image)
                                    :text      text})))

(core/register-template
  (:id metadata) metadata create-activity update-activity)
