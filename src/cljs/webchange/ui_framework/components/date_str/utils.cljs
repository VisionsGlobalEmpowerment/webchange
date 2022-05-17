(ns webchange.ui-framework.components.date-str.utils)

(defn leap-year?
  [year]
  (or (and (= (mod year 4) 0)
           (not= (mod year 100) 0))
      (= (mod year 400) 0)))

(defn get-min-day
  [month year]
  (if (and (some? month)
           (some? year))
    (let [short-month? (or (and (< month 7)
                                (= (mod month 2) 0))
                           (and (> month 8)
                                (= (mod month 2) 1)))]
      (if short-month?
        (if (= month 2)
          (if (leap-year? year)
            29
            28)
          30)
        31))
    31))

(defn fix-date
  [{:keys [day month year]}]
  (let [month (when (some? month)
                (min month 12))
        day (when (some? day)
              (-> (get-min-day month year)
                  (min day)))]
    {:day   day
     :month month
     :year  year}))

(defn add-leading-zero
  [number]
  (when (number? number)
    (str (if (< number 10) "0" "") number)))

(defn to-iso-format
  [date]
  (let [parse-int (fn [value]
                    (when (some? value) (int value)))
        day (parse-int (:day date))
        month (parse-int (:month date))
        year (parse-int (:year date))]
    (when (and (number? day)
               (number? month)
               (number? year))
      (str year
           "-"
           (add-leading-zero month)
           "-"
           (add-leading-zero day)))))

(defn parse-date
  [date-str {:keys [blocks]}]
  "Parse string to date:
   - date-str - e.g. '2014-09-19' or '12/31/2022'
   - blocks - expected day, month and year sequence, e.g. ['y' 'm' 'd'] or ['m' 'd' 'y']
   Result: map of :day, :month and :year with string value if parsed, nil otherwise.
   "
  (let [patterns {"y" "\\d{1,4}"
                  "m" "\\d{1,2}"
                  "d" "\\d{1,2}"}
        pattern (->> blocks
                     (reduce (fn [pattern block]
                               (str pattern
                                    "("
                                    (get patterns block)
                                    ")?"))
                             ""))
        prepared-date-str (-> date-str
                              (clojure.string/replace #"\D" "")
                              (subs 0 8))
        matches (-> (re-pattern pattern)
                    (re-matches prepared-date-str))
        get-value (fn [key blocks matches]
                    (when (some? matches)
                      (if-let [str-value (->> (.indexOf blocks key)
                                              (inc)
                                              (nth matches))]
                        str-value)))]
    {:day   (get-value "d" blocks matches)
     :month (get-value "m" blocks matches)
     :year  (get-value "y" blocks matches)}))

(defn apply-mask
  [{:keys [day month year]} {:keys [blocks delimiter]}]
  (let [values (map #(get {"d" day
                           "m" month
                           "y" year} %)
                    blocks)]
    (->> (map vector blocks values)
         (map-indexed (fn [idx [block value]]
                        (let [last-value? (-> values count dec (= idx))
                              add-delimiter? (if last-value?
                                               false
                                               (= (count value) (get {"d" 2
                                                                      "m" 2
                                                                      "y" 4} block)))]
                          (str value (if add-delimiter? delimiter "")))))
         (apply str))))
