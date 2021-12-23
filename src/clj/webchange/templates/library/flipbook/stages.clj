(ns webchange.templates.library.flipbook.stages
  (:require
    [clojure.tools.logging :as log]))

(defn- index->page
  [index pages]
  (when (some? index)
    (some (fn [{:keys [idx] :as page}]
            (and (= idx index) page))
          pages)))

(defn- get-page-title
  [activity-data {:keys [text]}]
  (if (some? text)
    (get-in activity-data [:objects (keyword text) :text])
    ""))

(defn- get-stage-title
  [{:keys [activity left-page right-page stage-idx]}]
  (let [not-empty? (complement empty?)
        left-page-title (get-page-title activity left-page)
        right-page-title (get-page-title activity right-page)]
    (if (and (empty? left-page-title)
             (empty? right-page-title))
      (str "Stage " stage-idx)
      (cond-> ""
              (not-empty? left-page-title) (str left-page-title)
              (and (not-empty? left-page-title)
                   (not-empty? right-page-title)) (str " | ")
              (not-empty? right-page-title) (str right-page-title)))))

(defn- page-position->page-idx
  [page-position pages]
  (when (and (>= page-position 0)
             (< page-position (count pages)))
    (-> (nth pages page-position)
        (get :idx))))

(defn update-stages
  [activity-data {:keys [book-name]}]
  (let [origin-pages (->> (get-in activity-data [:objects book-name :pages])
                          (map-indexed (fn [idx page]
                                         (assoc page :idx idx))))
        omit-filler? (-> (count origin-pages)
                         (odd?))
        pages (if omit-filler?
                (filter #(not (:back-cover-filler? %)) origin-pages)
                origin-pages)
        stages-number (-> (count pages) (quot 2) (inc))
        stages (map (fn [stage-index]
                      (let [left-page-index (-> (* stage-index 2) (dec) (page-position->page-idx pages))
                            left-page (index->page left-page-index pages)
                            right-page-index (-> (* stage-index 2) (page-position->page-idx pages))
                            right-page (index->page right-page-index pages)
                            stage-title (get-stage-title {:left-page  left-page
                                                          :right-page right-page
                                                          :activity   activity-data
                                                          :stage-idx  stage-index})]
                        {:name      stage-title
                         :idx       stage-index
                         :pages-idx [left-page-index right-page-index]}))
                    (range stages-number))]
    (-> activity-data
        (assoc-in [:metadata :stages] stages)
        (assoc-in [:metadata :flipbook-pages :current-side] (if omit-filler? "right" "left")))))
