(ns webchange.templates.library.first-word-book.add-spread
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(defn- spread-idx->spread-prefix
  ([spread-idx]
   (spread-idx->spread-prefix spread-idx nil))
  ([spread-idx side]
   (cond-> (str "spread-" spread-idx)
           (some? side) (str "-" (clojure.core/name side)))))

(defn- spread-idx->spread-name
  [spread-idx]
  (spread-idx->spread-prefix spread-idx))

(defn- spread-idx->page-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-page")))

(defn- spread-idx->page-display-name
  [spread-idx side]
  (str "Spread " spread-idx
       " - "
       (case side
         :left "Left"
         :right "Right") " page"))

(defn spread-idx->dialog-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-dialog")))

(defn- spread-idx->text-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-text")))

(defn- spread-idx->image-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-image")))

(defn- add-dialog
  [activity-data spread-idx side text]
  (let [text-object-name (spread-idx->text-name spread-idx side)
        inner-action {:type        "text-animation"
                      :target      text-object-name
                      :phrase-text text
                      :animation   "color"
                      :fill        45823
                      :audio       nil
                      :data        []}

        dialog-name (-> (spread-idx->dialog-name spread-idx side) (keyword))
        dialog-data (-> (spread-idx->page-display-name spread-idx side)
                        (dialog/default {:inner-action-data inner-action})
                        (assoc :tags ["dialog"]
                               :unique-tag "page-dialog"))]
    (-> activity-data
        (assoc-in [:actions dialog-name] dialog-data)
        (update-in [:metadata :tracks 0 :nodes] conj {:type      "dialog"
                                                      :action-id dialog-name}))))

(defn- add-text
  [activity-data spread-idx side text]
  (let [text-name (-> (spread-idx->text-name spread-idx side) (keyword))
        text-data {:type           "text"
                   :text           text
                   :chunks         [{:start 0 :end (count text)}]
                   :x              (case side
                                     :left 300
                                     :right 950)
                   :y              700
                   :align          "center"
                   :vertical-align "top"
                   :width          600
                   :height         200
                   :word-wrap      true
                   :font-family    "Tabschool"
                   :font-size      98
                   :fill           "black"
                   :metadata       {:display-name (str "Spread " spread-idx " - " (case side :left "Left" :right "Right"))
                                    :page-idx     spread-idx
                                    :text-idx     (case side :left 0 :right 1)}
                   :editable?      {:select true}}]
    (assoc-in activity-data [:objects text-name] text-data)))

(defn- add-image
  [activity-data spread-idx side {:keys [src]}]
  (let [image-name (-> (spread-idx->image-name spread-idx side) (keyword))
        image-data {:type       "image"
                    :src        src
                    :x          (case side
                                  :left 420
                                  :right 1050)
                    :y          270
                    :width      400
                    :height     400
                    :image-size "contain"
                    :editable?  {:select true}}]
    (assoc-in activity-data [:objects image-name] image-data)))

(defn- add-dialogs
  [activity-data spread-idx {:keys [text-left text-right]}]
  (-> activity-data
      (add-dialog spread-idx :left text-left)
      (add-dialog spread-idx :right text-right)))

(defn- add-texts
  [activity-data spread-idx {:keys [text-left text-right]}]
  (-> activity-data
      (add-text spread-idx :left text-left)
      (add-text spread-idx :right text-right)))

(defn- add-images
  [activity-data spread-idx {:keys [image-left image-right]}]
  (-> activity-data
      (add-image spread-idx :left image-left)
      (add-image spread-idx :right image-right)))

(defn- add-page
  [activity-data spread-idx page-side]
  (let [page-name (-> (spread-idx->page-name spread-idx page-side) (keyword))
        page-data {:type     "group"
                   :children [(spread-idx->text-name spread-idx page-side)
                              (spread-idx->image-name spread-idx page-side)]
                   :metadata {:display-name (spread-idx->page-display-name spread-idx page-side)
                              :objects-tree {:show?      true
                                             :actions    ["edit"]
                                             :sort-order (-> spread-idx (* 10) (+ (if (= page-side :left) 0 1)))}}}]
    (-> activity-data
        (assoc-in [:objects page-name] page-data))))

(defn- add-spread-object
  [activity-data spread-idx]
  (let [spread-name (spread-idx->spread-name spread-idx)
        spread-data {:type     "group"
                     :visible  false
                     :children [(spread-idx->page-name spread-idx :left)
                                (spread-idx->page-name spread-idx :right)]}]
    (-> activity-data
        (add-page spread-idx :left)
        (add-page spread-idx :right)
        (assoc-in [:objects (keyword spread-name)] spread-data)
        (update-in [:scene-objects 1] #(-> (conj % spread-name) (vec))))))

(defn- add-spread-action
  [activity-data spread-idx]
  (let [action-name (-> (spread-idx->spread-name spread-idx) (keyword))
        action-data {:type "sequence"
                     :data [(spread-idx->dialog-name spread-idx :left)
                            (spread-idx->dialog-name spread-idx :right)]}]
    (assoc-in activity-data [:actions action-name] action-data)))

(defn add-spread
  [activity-data args]
  {:pre [(string? (:text-left args))
         (string? (:text-right args))
         (string? (get-in args [:image-left :src]))
         (string? (get-in args [:image-left :src]))]}
  (let [current-spread-idx (-> activity-data (get-in [:metadata :last-spread-idx]) (inc))]
    (-> activity-data
        (assoc-in [:metadata :last-spread-idx] current-spread-idx)
        (update-in [:actions :set-total-spreads-number :var-value] inc)
        (add-spread-object current-spread-idx)
        (add-spread-action current-spread-idx)
        (add-dialogs current-spread-idx args)
        (add-texts current-spread-idx args)
        (add-images current-spread-idx args))))
