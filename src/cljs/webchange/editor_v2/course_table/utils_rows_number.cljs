(ns webchange.editor-v2.course-table.utils-rows-number)

(defn- get-element-height
  ([el]
   (get-element-height el {}))
  ([el {:keys [without-padding]
        :or   {without-padding false}}]
   (let [style (.getComputedStyle js/window el nil)
         ->int #(.parseInt js/Number %)
         get-prop #(.getPropertyValue style %)]
     (cond-> (-> "height" get-prop ->int)
             without-padding (-> (- (-> "padding-top" get-prop ->int))
                                 (- (-> "padding-bottom" get-prop ->int)))))))

(defn- get-content-height
  [container-el]
  (let [header (.querySelector container-el "thead")
        footer (.querySelector container-el ".footer")]
    (when (some? header)
      (let [content-height (get-element-height container-el {:without-padding true})
            header-height (get-element-height header)
            footer-height (get-element-height footer)]
        (- content-height header-height footer-height)))))

(defn get-row-height
  [content-el]
  (let [content-row (.querySelector content-el "tbody > tr")]
    (when (some? content-row)
      (get-element-height content-row))))

(defn get-rows-number
  [container-el row-height]
  (-> (get-content-height container-el)
      (/ row-height)
      (Math/ceil)))

(defn get-actual-rows-number
  [container-el]
  (let [content-height (get-content-height container-el)
        rows-number-threshold 50]
    (loop [rows-number 0
           total-height 0
           [row-height & rest-rows] (->> (.querySelectorAll container-el "tbody tr")
                                         (.from js/Array)
                                         (map (fn [row] (get-element-height row))))]
      (cond
        (> rows-number rows-number-threshold) nil
        (or (not (some? row-height))
            (> (+ total-height row-height) content-height)) rows-number
        :else (recur (inc rows-number)
                     (+ total-height row-height)
                     rest-rows)))))
