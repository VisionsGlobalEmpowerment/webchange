(ns webchange.templates.library.flipbook.cover-back
  (:require
    [webchange.templates.library.flipbook.display-names :refer [get-text-display-name]]
    [webchange.utils.scene-data :refer [generate-name rename-object]]))

(def page-name "page-cover-back")

(def resources ["/raw/img/flipbook/logo_2.png"])

(def template
  {:page-cover-back            {:type       "group"
                                :transition "page-cover-back"
                                :children   ["page-cover-back-background"
                                             "page-cover-back-image"
                                             "page-cover-back-license"]
                                :generated? true}
   :page-cover-back-background {:type   "rectangle"
                                :x      0
                                :y      0
                                :width  "---"
                                :height "---"
                                :fill   "---"}
   :page-cover-back-image      {:type      "image"
                                :x         "---"
                                :y         350
                                :src       "/raw/img/flipbook/logo_2.png"
                                :origin    {:type "center-center"}}
   :page-cover-back-license    {:type           "text"
                                :word-wrap      true
                                :vertical-align "top"
                                :font-size      24
                                :x              "---"
                                :y              700
                                :width          "---"
                                :fill           "---"
                                :text           "---"
                                :editable?      {:select true
                                                 :drag   true}
                                :placeholder    "Add attributions"
                                :metadata       {:display-name (get-text-display-name :cover-back "License")
                                                 :removable?   true}}})

(defn- apply-page-size
  [page-data {:keys [width height padding]}]
  (let [page-center (/ width 2)]
    (-> page-data
        (assoc-in [:page-cover-back-background :width] width)
        (assoc-in [:page-cover-back-background :height] height)
        (assoc-in [:page-cover-back-image :x] page-center)
        (assoc-in [:page-cover-back-license :x] (* 2 padding))
        (assoc-in [:page-cover-back-license :width] (- width (* 4 padding))))))

(defn- set-content
  [page-data {:keys [authors illustrators]}]
  (let [text (cond->
               "Written by __authors__.

Illustrated by __illustrators__."
               true (clojure.string/replace-first #"__authors__" (clojure.string/join ", " authors))
               (empty? illustrators) (clojure.string/replace-first #"Illustrated by __illustrators__." "")
               (not (empty? illustrators)) (clojure.string/replace-first #"__illustrators__" (clojure.string/join ", " illustrators)))]
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
  {:name      page-name
   :resources resources
   :objects   (-> template
                  (apply-page-size page-params)
                  (set-content content-params)
                  (set-colors page-params))})
