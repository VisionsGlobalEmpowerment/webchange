(ns webchange.templates.library.flipbook.template
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.core :as core]
    [webchange.templates.library.flipbook.front-cover :as front-cover]))

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

(def template {:assets        [{:url "/raw/img/flipbook/next-page.png" :size 1 :type "image"}
                               {:url "/raw/img/flipbook/prev-page.png" :size 1 :type "image"}]
               :objects       {:book      {:type         "flipbook"
                                           :transition   "book"
                                           :x            0
                                           :y            0
                                           :width        1920
                                           :height       1080
                                           :pages        []
                                           :prev-control "prev-page"
                                           :next-control "next-page"}
                               :prev-page {:type    "image"
                                           :x       120
                                           :y       950
                                           :width   474
                                           :height  474
                                           :scale-x 0.2
                                           :scale-y 0.2
                                           :src     "/raw/img/flipbook/prev-page.png"
                                           :actions {:click {:id "prev-page-click" :on "click" :type "action"}}}
                               :next-page {:type    "image"
                                           :x       1700
                                           :y       950
                                           :width   474
                                           :height  474
                                           :scale-x 0.2
                                           :scale-y 0.2
                                           :src     "/raw/img/flipbook/next-page.png"
                                           :actions {:click {:id "next-page-click" :on "click" :type "action"}}}}
               :scene-objects [["book"] ["prev-page" "next-page"]]
               :actions       {:start-scene     {:type "sequence-data"
                                                 :data [{:type   "flipbook-init"
                                                         :target "book"}]}
                               :prev-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-backward"
                                                         :target "book"}]}
                               :next-page-click {:type "sequence-data"
                                                 :data [{:type   "flipbook-flip-forward"
                                                         :target "book"}]}}
               :triggers      {:start {:on     "start"
                                       :action "start-scene"}}
               :metadata      {:autostart true}
               :audio         {}})

(defn- add-page-to-book
  [activity-data page-name objects]
  (-> activity-data
      (update :objects merge objects)
      (update-in [:objects :book :pages] conj page-name)))

(defn create-activity
  [{:keys [authors cover-layout cover-image cover-title] :as props}]
  (log/debug ">> create-activity")
  (log/debug props)
  (let [front-cover-page (front-cover/create {:layout    (keyword cover-layout)
                                              :image-src (:src cover-image)
                                              :title     cover-title
                                              :authors   authors})]
    (-> template
        (add-page-to-book (:name front-cover-page) (:objects front-cover-page))
        (assoc-in [:metadata :actions] (:actions metadata)))))

(defn- get-page-name
  []
  (->> (java.util.UUID/randomUUID) (.toString) (str "page-")))

(defn- page-name->image-name
  [page-name]
  (-> page-name (str "-image")))

(defn- page-name->text-name
  [page-name]
  (-> page-name (str "-text")))

(defn- create-page
  [{:keys [page-type image-src text]}]
  (let [page-name (get-page-name)
        image-name (page-name->image-name page-name)
        text-name (page-name->text-name page-name)
        markup-params {:width   960
                       :height  1080
                       :padding 50}
        default-props {:group {:type       "group"
                               :transition ""
                               :children   [""]}
                       :text  {:type           "text"
                               :word-wrap      true
                               :vertical-align "top"
                               :fill           "white"
                               :text           text}
                       :image {:type   "image"
                               :x      0
                               :y      0
                               :width  (:width markup-params)
                               :height (:height markup-params)
                               :src    image-src}}
        current-props (case page-type
                        :text-big-at-bottom (-> default-props
                                                (update :group merge {:transition page-name
                                                                      :children   [image-name text-name]})
                                                (update :text merge {:x     (:padding markup-params)
                                                                     :y     (* (:height markup-params) 0.6)
                                                                     :width (- (:width markup-params) (* (:padding markup-params) 2))}))
                        :text-small-at-bottom (-> default-props
                                                  (update :group merge {:transition page-name
                                                                        :children   [image-name text-name]})
                                                  (update :text merge {:x     (:padding markup-params)
                                                                       :y     (* (:height markup-params) 0.8)
                                                                       :width (- (:width markup-params) (* (:padding markup-params) 2))}))
                        :only-image (-> default-props
                                        (update :group merge {:transition page-name
                                                              :children   [image-name]})
                                        (dissoc :text))
                        :text-at-top (-> default-props
                                         (update :group merge {:transition page-name
                                                               :children   [image-name text-name]})
                                         (update :text merge {:x     (:padding markup-params)
                                                              :y     (:padding markup-params)
                                                              :width (- (:width markup-params) (* (:padding markup-params) 2))}))
                        :only-text (-> default-props
                                       (update :group merge {:transition page-name
                                                             :children   [text-name]})
                                       (update :text merge {:x     (:padding markup-params)
                                                            :y     (:padding markup-params)
                                                            :width (- (:width markup-params) (* (:padding markup-params) 2))})
                                       (dissoc :image)))]
    {:name    page-name
     :objects (cond-> (assoc {} (keyword page-name) (:group current-props))
                      (some? (:image current-props)) (assoc (keyword image-name) (:image current-props))
                      (some? (:text current-props)) (assoc (keyword text-name) (:text current-props)))}))

(defn update-activity
  [activity-data {:keys [layout image text]}]
  (let [page (create-page {:page-type (keyword layout)
                           :image-src (:src image)
                           :text      text})]
    (-> activity-data
        (update :objects merge (:objects page))
        (update-in [:objects :book :pages] conj (:name page)))))

(core/register-template
  (:id metadata) metadata create-activity update-activity)
