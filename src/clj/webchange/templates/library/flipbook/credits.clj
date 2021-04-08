(ns webchange.templates.library.flipbook.credits
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.library.flipbook.actions-utils :refer [create-dialog-action]]
    [webchange.utils.text :as text-utils]))

(def page-name "credits-page")

(def resources [])

(def template
  {:credits-page                   {:type       "group"
                                    :transition "credits-page"
                                    :children   ["credits-page-background"
                                                 "credits-page-title"
                                                 "credits-page-authors"
                                                 "credits-page-illustrators"]
                                    :generated? true}
   :credits-page-background        {:type     "group"
                                    :x        0
                                    :y        0
                                    :children ["credits-page-background-border"
                                               "credits-page-background-back"]}
   :credits-page-background-border {:type   "rectangle"
                                    :x      0
                                    :y      0
                                    :width  "---"
                                    :height "---"
                                    :fill   "---"}
   :credits-page-background-back   {:type   "rectangle"
                                    :x      "---"
                                    :y      "---"
                                    :width  "---"
                                    :height "---"
                                    :fill   "---"}
   :credits-page-title             {:type        "text"
                                    :x           "---"
                                    :y           200
                                    :fill        "black"
                                    :font-size   60
                                    :font-family "Lexend Deca"
                                    :align       "center"
                                    :text        "My Book"}})

(defn- get-action-data
  [{:keys [action-name texts-data]}]
  (create-dialog-action {:phrase             action-name
                         :phrase-description "Read credentials"
                         :dialog-actions     (map (fn [{:keys [name text]}]
                                                    [:text-animation {:target      name
                                                                      :phrase-text text}])
                                                  texts-data)}))

(defn- generate-list-block
  [{:keys [name label data label-width value-width vertical-step]}]
  (let [label-name (str name "-label")]
    (->> data
         (map-indexed vector)
         (reduce (fn [result [index text]]
                   (let [object-name (str name "-" index)]
                     (-> result
                         (update :texts-data conj {:text text
                                                   :name object-name})
                         (update-in [:data (keyword name) :children] conj object-name)
                         (assoc-in [:data (keyword object-name)] {:type        "text"
                                                                  :x           label-width
                                                                  :y           (* index vertical-step)
                                                                  :width       value-width
                                                                  :fill        "black"
                                                                  :font-size   32
                                                                  :font-family "Lexend Deca"
                                                                  :text        text
                                                                  :editable?   {:select true}
                                                                  :chunks      (text-utils/text->chunks text)}))))
                 {:texts-data [{:name label-name
                                :text label}]
                  :data       (-> {}
                                  (assoc (keyword name) {:type     "group"
                                                         :x        "---"
                                                         :y        "---"
                                                         :children [label-name]})
                                  (assoc (keyword label-name) {:type        "text"
                                                               :x           0
                                                               :y           0
                                                               :width       label-width
                                                               :fill        "black"
                                                               :font-size   32
                                                               :font-family "Lexend Deca"
                                                               :text        label
                                                               :editable?   {:select true}
                                                               :chunks      (text-utils/text->chunks label)}))}))))

(defn- generate-authors-block
  [{:keys [data]}]
  (let [block-params {:label-width   250
                      :value-width   250
                      :vertical-step 50}]
    (generate-list-block (merge block-params
                                {:name     "credits-page-authors"
                                 :label    "Written By"
                                 :data     data
                                 :position {:x "---"
                                            :y 400}}))))

(defn- generate-illustrators-block
  [{:keys [data]}]
  (let [block-params {:label-width   250
                      :value-width   250
                      :vertical-step 50}]
    (generate-list-block (merge block-params
                                {:name     "credits-page-illustrators"
                                 :label    "Illustrated By"
                                 :data     data
                                 :position {:x "---"
                                            :y 600}}))))

(defn- apply-page-size
  [page-data {:keys [width height padding]}]
  (let [page-center (/ width 2)
        credits-x 230]
    (-> page-data
        (assoc-in [:credits-page-background-border :width] width)
        (assoc-in [:credits-page-background-border :height] height)
        (assoc-in [:credits-page-background-back :x] padding)
        (assoc-in [:credits-page-background-back :y] padding)
        (assoc-in [:credits-page-background-back :width] (- width (* 2 padding)))
        (assoc-in [:credits-page-background-back :height] (- height (* 2 padding)))
        (assoc-in [:credits-page-title :x] page-center)
        (assoc-in [:credits-page-authors :x] credits-x)
        (assoc-in [:credits-page-authors :y] 400)
        (assoc-in [:credits-page-illustrators :x] credits-x)
        (assoc-in [:credits-page-illustrators :y] 600))))

(defn- set-content
  [page-data {:keys [title]}]
  (-> page-data
      (assoc-in [:credits-page-title :text] title)))

(defn- set-colors
  [page-data {:keys [background-color border-color]}]
  (-> page-data
      (assoc-in [:credits-page-background-back :fill] background-color)
      (assoc-in [:credits-page-background-border :fill] border-color)))

(defn create
  [page-params {:keys [authors illustrators] :as content-params}]
  (let [authors-data (generate-authors-block {:data authors})
        illustrators-data (generate-illustrators-block {:data illustrators})
        action-name "credentials-action"
        texts-data (concat (:texts-data authors-data) (:texts-data illustrators-data))]
    {:name      page-name
     :resources resources
     :action    {:name action-name
                 :data (get-action-data {:action-name action-name
                                         :texts-data  texts-data})}
     :objects   (-> (merge template
                           (:data authors-data)
                           (:data illustrators-data))
                    (apply-page-size page-params)
                    (set-content content-params)
                    (set-colors page-params))}))
