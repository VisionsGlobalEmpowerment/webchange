(ns webchange.templates.library.flipbook.cover-back
  (:require
    [webchange.utils.scene-data :refer [generate-name rename-object]]))

(def page-name "page-cover-back")

(def resources ["/raw/img/flipbook/logo.png"])

(def template
  {:page-cover-back                   {:type       "group"
                                       :transition "page-cover-back"
                                       :children   ["page-cover-back-background-border"
                                                    "page-cover-back-background"
                                                    "page-cover-back-image"
                                                    "page-cover-back-license"
                                                    "page-cover-back-attributions"]
                                       :generated? true}
   :page-cover-back-background        {:type   "rectangle"
                                       :x      "---"
                                       :y      "---"
                                       :width  "---"
                                       :height "---"
                                       :fill   "---"}
   :page-cover-back-background-border {:type   "rectangle"
                                       :x      0
                                       :y      0
                                       :width  "---"
                                       :height "---"
                                       :fill   "---"}
   :page-cover-back-image             {:type       "image"
                                       :image-size "contain"
                                       :x          "---"
                                       :y          "---"
                                       :width      "---"
                                       :height     "---"
                                       :src        "/raw/img/flipbook/logo.png"
                                       :origin     {:type "center-center"}}
   :page-cover-back-license           {:type           "text"
                                       :word-wrap      true
                                       :vertical-align "top"
                                       :font-size      24
                                       :x              "---"
                                       :y              700
                                       :width          "---"
                                       :fill           "---"
                                       :text           "---"}
   :page-cover-back-attributions      {:type     "group"
                                       :x        "---"
                                       :y        "---"
                                       :children []}})

(def attr-template
  {:page-attr       {:type     "group"
                     :children ["page-attr-label" "page-attr-value"]
                     :x        0
                     :y        "---"}
   :page-attr-label {:type           "text"
                     :vertical-align "top"
                     :font-size      24
                     :x              0
                     :y              0
                     :fill           "---"
                     :text           "---"}
   :page-attr-value {:type           "text"
                     :vertical-align "top"
                     :font-size      24
                     :x              "---"
                     :y              0
                     :fill           "---"
                     :text           "---"}})


(defn- get-attribute-group
  [{:keys [y label value label-width]} {:keys [text-color]}]
  (-> attr-template
      (assoc-in [:page-attr :y] y)
      (assoc-in [:page-attr-label :fill] text-color)
      (assoc-in [:page-attr-label :text] label)
      (assoc-in [:page-attr-value :x] label-width)
      (assoc-in [:page-attr-value :fill] text-color)
      (assoc-in [:page-attr-value :text] value)))

(defn- generate-attributions
  "[{:label
     :value}]"
  [attributions {:keys [attribution-label-width vertical-step] :as page-params}]
  (->> attributions
       (map-indexed vector)
       (reduce (fn [result [idx attribute]]
                 (let [attr-name (generate-name)
                       attr-data (-> (merge attribute
                                            {:y           (* idx vertical-step)
                                             :label-width attribution-label-width})
                                     (get-attribute-group page-params)
                                     (rename-object "page-attr" attr-name))]
                   (-> result
                       (update :names conj attr-name)
                       (update :objects merge attr-data))))
               {:names   []
                :objects {}})))

(defn- set-attributions
  [page-data page-params attributions]
  (let [{:keys [names objects]} (generate-attributions attributions page-params)]
    (-> page-data
        (assoc-in [:page-cover-back-attributions :children] names)
        (merge objects))))

(defn- apply-page-size
  [page-data {:keys [width height padding attributions-height attributions-width]}]
  (let [images-size (- width (* 4 padding))
        page-center (/ width 2)]
    (-> page-data
        (assoc-in [:page-cover-back-background-border :width] width)
        (assoc-in [:page-cover-back-background-border :height] height)
        (assoc-in [:page-cover-back-background :x] padding)
        (assoc-in [:page-cover-back-background :y] padding)
        (assoc-in [:page-cover-back-background :width] (- width (* 2 padding)))
        (assoc-in [:page-cover-back-background :height] (- height (* 2 padding)))
        (assoc-in [:page-cover-back-image :x] page-center)
        (assoc-in [:page-cover-back-image :y] (+ padding (/ images-size 2)))
        (assoc-in [:page-cover-back-image :width] images-size)
        (assoc-in [:page-cover-back-image :height] images-size)
        (assoc-in [:page-cover-back-attributions :x] (- (/ width 2) (/ attributions-width 2)))
        (assoc-in [:page-cover-back-attributions :y] (- height padding attributions-height padding))
        (assoc-in [:page-cover-back-license :x] (* 2 padding))
        (assoc-in [:page-cover-back-license :width] (- width (* 4 padding))))))

(defn- set-content
  [page-data {:keys [authors illustrators]}]
  (let [text (->
               "Written by __authors__.
               Illustrated by __illustrators__.

               All rights reserved."
               (clojure.string/replace-first #"__authors__" (clojure.string/join ", " authors))
               (clojure.string/replace-first #"__illustrators__" (clojure.string/join ", " illustrators)))]
    (-> page-data
        (assoc-in [:page-cover-back-license :text] text))))

(defn- set-colors
  [page-data {:keys [text-color background-color border-color]}]
  (-> page-data
      (assoc-in [:page-cover-back-background :fill] background-color)
      (assoc-in [:page-cover-back-background-border :fill] border-color)
      (assoc-in [:page-cover-back-license :fill] text-color)))

(defn create
  [page-params content-params]
  (let [attributions []
        attributions-height (* 40 (count attributions))
        attributions-width 100
        attribution-label-width 130
        vertical-step 40]
    {:name      page-name
     :resources resources
     :objects   (-> template
                    (set-attributions (merge page-params
                                             {:attribution-label-width attribution-label-width
                                              :vertical-step           vertical-step})
                                      attributions)
                    (apply-page-size (merge page-params
                                            {:attributions-height attributions-height
                                             :attributions-width  attributions-width}))
                    (set-content content-params)
                    (set-colors page-params))}))
