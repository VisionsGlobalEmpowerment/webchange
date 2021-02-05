(ns webchange.templates.library.flipbook.stages)

(defn- check-index
  [index range-length]
  (when (and (>= index 0)
             (< index range-length))
    index))

(defn- index->page
  [index pages]
  (when (some? index)
    (nth pages index)))

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

(defn update-stages
  [activity-data {:keys [book-name]}]
  (let [pages (get-in activity-data [:objects book-name :pages])
        stages-number (-> (count pages) (quot 2) (inc))
        stages (map (fn [stage-index]
                      (let [left-page-index (-> (* stage-index 2) (dec) (check-index (count pages)))
                            left-page (index->page left-page-index pages)
                            right-page-index (-> (* stage-index 2) (check-index (count pages)))
                            right-page (index->page right-page-index pages)
                            stage-title (get-stage-title {:left-page  left-page
                                                          :right-page right-page
                                                          :activity   activity-data
                                                          :stage-idx  stage-index})]
                        {:name      stage-title
                         :idx       stage-index
                         :pages-idx [left-page-index right-page-index]}))
                    (range stages-number))]
    (assoc-in activity-data [:metadata :stages] stages)))
