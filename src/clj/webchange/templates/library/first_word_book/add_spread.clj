(ns webchange.templates.library.first-word-book.add-spread
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [webchange.templates.library.first-word-book.timeout :as timeout]
    [webchange.templates.utils.dialog :as dialog]
    [webchange.templates.utils.common :as common]))

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

(defn spread-idx->effect-name-highligh
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-highligth")))

(defn spread-idx->effect-name-unhighligh
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-unhighlight")))

(defn spread-idx->dialog-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-dialog")))

(defn spread-idx->text-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-text")))

(defn- spread-idx->image-name
  [spread-idx side]
  (-> (spread-idx->spread-prefix spread-idx side)
      (str "-image")))

(defn- word-chunks
  [word letter]
  (let [letter-position (str/index-of word letter)
        letter-length (count letter)]
    (case letter-position
      nil [{:start 0 :end (count word)
            :fill "#00B2FF"}]
      0 [{:start       0 :end letter-length
          :fill        "#ef545c"
          :font-weight "bold"}
         {:start letter-length :end "last"
          :fill "#00B2FF"}]
      [{:start 0 :end letter-position
        :fill "#00B2FF"}
       {:start letter-position :end (+ letter-position letter-length)
        :fill        "#ef545c"
        :font-weight "bold"}
       {:start (+ letter-position letter-length) :end "last"
        :fill "#00B2FF"}])))

(defn- add-effect
  [activity-data spread-idx side text letter]
  (let [text-highlight-name (spread-idx->effect-name-highligh spread-idx side)
        text-unhighlight-name (spread-idx->effect-name-unhighligh spread-idx side)]
    (if (seq letter)
      (let [text-object-name (spread-idx->text-name spread-idx side)
            highlight-action {:type        "set-attribute"
                              :target      text-object-name
                              :attr-name   "chunks"
                              :attr-value  (word-chunks text letter)}
            unhighlight-action {:type        "set-attribute"
                                :target      text-object-name
                                :attr-name   "chunks"
                                :attr-value  [{:start 0 :end (count text)}]}]
        (-> activity-data
            (assoc-in [:actions text-highlight-name] highlight-action)
            (assoc-in [:actions text-unhighlight-name] unhighlight-action)
            (common/remove-available-action text-highlight-name)
            (common/remove-available-action text-unhighlight-name)
            (common/add-available-action text-highlight-name (str "Highlight " letter " in " text))
            (common/add-available-action text-unhighlight-name (str "Unhighlight " letter " in " text))))
      (-> activity-data
          (common/remove-available-action text-highlight-name)
          (common/remove-available-action text-unhighlight-name)))))

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

(defn- add-effects
  [activity-data spread-idx {:keys [text-left text-right letter-left letter-right]}]
  (-> activity-data
      (add-effect spread-idx :left text-left letter-left)
      (add-effect spread-idx :right text-right letter-right)))

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
                            (timeout/get-action-name)
                            (spread-idx->dialog-name spread-idx :right)]}]
    (assoc-in activity-data [:actions action-name] action-data)))

(defn get-spread-info
  [spread-idx]
  {:object  (spread-idx->spread-name spread-idx)
   :actions [(spread-idx->spread-name spread-idx)
             (spread-idx->dialog-name spread-idx :left)
             (spread-idx->dialog-name spread-idx :right)]})

(defn create-spread
  [activity-data {id :id :as args}]
  (-> activity-data
      (add-spread-object id)
      (add-spread-action id)
      (add-dialogs id args)
      (add-texts id args)
      (add-images id args)
      (add-effects id args)))

(defn edit-spread
  [activity-data {id :id :as args}]
  (-> activity-data
      (add-texts id args)
      (add-images id args)
      (add-effects id args)))

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
        (create-spread (assoc args :id current-spread-idx)))))
