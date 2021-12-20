(ns webchange.templates.library.flipbook.add-empty-page
  (:require
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.utils.flipbook :as f]
    [webchange.utils.list :refer [insert-at-position]]))

(defn update-current-side
  [activity-data]
  (update-in activity-data [:metadata :flipbook-pages :current-side] #(if (= % "right") "left" "right")))

(defn- get-background-data
  [{:keys [width height background-color]}]
  {:type   "rectangle"
   :x      0
   :y      0
   :width  width
   :height height
   :fill   background-color})

(defn- add-to-flipbook-pages
  [activity-data page-record]
  (let [flipbook-name (-> activity-data f/get-book-object-name keyword)
        pages-data (f/get-pages-data activity-data)
        page-position (cond-> (-> pages-data count dec)
                              (some :back-cover-filler? pages-data) (dec))]
    (update-in activity-data
               [:objects flipbook-name :pages]
               insert-at-position
               page-record
               page-position)))

(defn add-empty-page
  [activity-data {:keys [page-params]}]
  (let [flipbook-name (f/get-book-object-name activity-data)
        next-page-id (get-in activity-data [:metadata :next-page-id] 0)

        page-action-name (str "page-" next-page-id "-action")
        page-action-data {:type               "sequence-data"
                          :data               []
                          :concept-var        "current-word"
                          :editor-type        "dialog"
                          :phrase             (str "page-" next-page-id "-action")
                          :phrase-description "Read page"}

        page-object-name (str "page-" next-page-id)
        page-background-name (str page-object-name "-background")

        page-object-data {:type     "group",
                          :children [page-background-name]}
        page-background-data (get-background-data page-params)

        page-record {:action     page-action-name
                     :object     page-object-name
                     :removable? true}]
    (-> activity-data
        (add-to-flipbook-pages page-record)
        (update :objects merge {(keyword page-object-name)     page-object-data
                                (keyword page-background-name) page-background-data})
        (update :actions assoc (keyword page-action-name) page-action-data)
        (update-stages {:book-name flipbook-name})
        (update-current-side)
        (update-in [:metadata :next-page-id] inc))))
