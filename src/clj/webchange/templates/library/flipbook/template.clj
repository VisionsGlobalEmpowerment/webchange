(ns webchange.templates.library.flipbook.template
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.library.flipbook.activity-template :refer [get-template]]
    [webchange.templates.library.flipbook.add-page :refer [add-page] :as page]
    [webchange.templates.library.flipbook.credits :as credits]
    [webchange.templates.library.flipbook.custom-page :as custom-page]
    [webchange.templates.library.flipbook.custom-spread :as custom-spread]
    [webchange.templates.library.flipbook.cover-back :as back-cover]
    [webchange.templates.library.flipbook.cover-front :as front-cover]
    [webchange.templates.library.flipbook.generic-front :as generic-front]
    [webchange.templates.library.flipbook.remake-covers :refer [remake-covers]]
    [webchange.templates.library.flipbook.remove-page :refer [remove-page]]
    [webchange.templates.library.flipbook.reorder-page :refer [move-page]]
    [clojure.tools.logging :as log]))

(def book-options [{:key         :cover-layout
                    :label       "Cover layout"
                    :type        "lookup-image"
                    :description "Cover layout"
                    :options     [{:name  "Title at top"
                                   :value :title-top
                                   :src   "/images/templates/cover_layout/title_at_top.svg"}
                                  {:name  "Title at bottom"
                                   :value :title-bottom
                                   :src   "/images/templates/cover_layout/title_at_bottom.svg"}]}
                   {:key         :cover-title
                    :label       "Title"
                    :placeholder "Type title here"
                    :description "Cover title"
                    :type        "string"}
                   {:key         :cover-image
                    :label       "Cover image"
                    :description "Cover image"
                    :type        "image"}
                   {:key   :authors
                    :label "Authors"
                    :type  "strings-list"
                    :max   3}
                   {:key       :illustrators
                    :label     "Illustrators"
                    :type      "strings-list"
                    :optional? true
                    :max       3}])

(def page-options [{:key     :type
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
                                  :value [:text-only]}]}])

(def metadata {:id          24
               :name        "flipbook"
               :tags        ["Book Creator"]
               :description "Some description of flipbook mechanics and covered skills"
               :lesson-sets []
               :options     book-options
               :actions     {:add-page      {:title   "Add page",
                                             :options page-options}
                             :remake-covers {:title         "Remake covers",
                                             :options       book-options
                                             :default-props :wizard}}})

(def page-params {:width            960
                  :height           1080
                  :padding          50
                  :background-color 0xffffff
                  :border-color     0xdadada
                  :text-color       0x000000})

(defn create-activity
  [props]
  (-> (get-template page-params)
      (add-page front-cover/create page-params (page/activity->front-cover-props props))
      (add-page generic-front/create page-params (page/activity->generic-front-props props))
      (add-page credits/create page-params (page/activity->credits-props props))
      (add-page back-cover/create page-params (page/activity->back-cover-props props))
      (assoc-in [:metadata :actions] (:actions metadata))
      (assoc-in [:metadata :saved-props :wizard] props)))

(defn add-page-handler
  [activity-data {:keys [type page-layout spread-layout image text]}]
  (let [constructors (case type
                       "page" (custom-page/constructors (keyword page-layout))
                       "spread" (custom-spread/constructors (keyword spread-layout)))]
    (reduce (fn [activity-data constructor]
              (add-page activity-data constructor page-params {:image-src      (:src image)
                                                               :text           text
                                                               :with-action?   true
                                                               :shift-from-end 1}))
            activity-data
            constructors)))

(defn update-activity
  [activity-data {action-name :action-name :as props}]
  (case action-name
    "add-page" (add-page-handler activity-data props)
    "remove-page" (remove-page activity-data props page-params)
    "move-page" (move-page activity-data props page-params)
    "remake-covers" (-> (remake-covers activity-data props page-params)
                        (assoc-in [:metadata :saved-props :wizard] props))))

(core/register-template
  metadata create-activity update-activity)
