(ns webchange.templates.library.flipbook.add-page
  (:require
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.utils.list :refer [insert-at-position]]
    [webchange.templates.library.flipbook.utils :refer [get-book-object-name]]))

(defn activity->front-cover-props
  [{:keys [authors cover-layout cover-image cover-title]}]
  {:layout       (keyword cover-layout)
   :image-src    (:src cover-image)
   :title        cover-title
   :authors      authors
   :with-action? true
   :removable?   false})

(defn activity->generic-front-props
  [_]
  {:removable? false})

(defn activity->credits-props
  [{:keys [authors illustrators cover-title]}]
  {:title        cover-title
   :authors      authors
   :illustrators illustrators
   :with-action? true
   :removable?   false})

(defn activity->back-cover-props
  [{:keys [authors illustrators cover-image]}]
  {:image-src    (:src cover-image)
   :authors      authors
   :illustrators illustrators
   :removable?   false})

(defn get-action-data
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
                                 :animation   "color"
                                 :fill        0x00B2FF
                                 :phrase-text text-value}]}]
   :phrase             action-name
   :phrase-description "Read page"})

(defn- add-text-animation-action
  [activity-data page-position {:keys [on-text-animation-action]} {:keys [name objects text-name action]}]
  (let [{:keys [name data]} (if (some? action)
                              action
                              (let [action-name (str name "-action")]
                                {:name action-name
                                 :data (get-action-data {:action-name action-name
                                                         :text-name   text-name
                                                         :text-value  (get-in objects [(keyword text-name) :text])})}))
        book-object-name (get-book-object-name activity-data)
        on-text-animation-action (or on-text-animation-action (fn [x & _] x))]
    (-> activity-data
        (assoc-in [:objects book-object-name :pages page-position :action] name)
        (assoc-in [:actions (keyword name)] data)
        (on-text-animation-action page-position name data))))

(defn- add-page-to-book
  [activity-data
   {:keys [with-action? shift-from-end removable? position]
    :or   {shift-from-end 0
           removable?     true}
    :as content-data}
   {:keys [name resources objects text-name action] :as page-data}]
  (let [book-object-name (get-book-object-name activity-data)
        new-page-position (if (some? position)
                            position
                            (let [current-pages-count (-> activity-data
                                                          (get-in [:objects book-object-name :pages] [])
                                                          (count))]
                              (- current-pages-count shift-from-end)))]
    (cond-> (-> activity-data
                (update :assets concat resources)
                (update :objects merge objects)
                (update-in [:objects book-object-name :pages] insert-at-position {:object name :text text-name :removable? removable?} new-page-position)
                (update-stages {:book-name book-object-name}))
            (and with-action?
                 (or (some? action)
                     (some? text-name))) (add-text-animation-action new-page-position content-data page-data))))

(defn- add-pages-to-book
  [activity-data content-data pages-data]
  (if (sequential? pages-data)
    (reduce (fn [activity-data page-data]
              (add-page-to-book activity-data content-data page-data))
            activity-data
            pages-data)
    (add-page-to-book activity-data content-data pages-data)))

(defn add-page
  ([activity-data page-constructor page-params]
   (add-page activity-data page-constructor page-params {}))
  ([activity-data page-constructor page-params content-data]
   (->> (page-constructor page-params content-data)
        (add-pages-to-book activity-data content-data))))
