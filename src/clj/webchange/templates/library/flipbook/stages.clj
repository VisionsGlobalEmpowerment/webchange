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
  [{:keys [stage-idx]}]
  (str "Spread " stage-idx))

(defn- page-position->page-idx
  [page-position pages]
  (when (and (>= page-position 0)
             (< page-position (count pages)))
    (-> (nth pages page-position)
        (get :idx))))

(defn update-stages
  [activity-data {:keys [book-name]}]
  (let [pages (->> (get-in activity-data [:objects book-name :pages])
                   (map-indexed (fn [idx page]
                                  (assoc page :idx idx))))
        last-page-odd? (-> (count pages) (odd?))
        stages-number (-> (count pages) (quot 2) (inc))
        stages (map (fn [stage-index]
                      (let [left-page-index (-> (* stage-index 2) (dec) (page-position->page-idx pages))
                            right-page-index (-> (* stage-index 2) (page-position->page-idx pages))
                            stage-title (get-stage-title {:stage-idx  stage-index})]
                        {:name      stage-title
                         :idx       stage-index
                         :pages-idx [left-page-index right-page-index]}))
                    (range stages-number))]
    (-> activity-data
        (assoc-in [:metadata :stages] stages)
        (assoc-in [:metadata :flipbook-pages :current-side] (if last-page-odd? "left" "right")))))
