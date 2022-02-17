(ns webchange.ui-framework.components.date.utils)

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
  [{:keys [day month year]}]
  (when (and (number? day)
             (number? month)
             (number? year))
    (str year
         "-"
         (add-leading-zero month)
         "-"
         (add-leading-zero day))))

(defn parse-date
  [date-str {:keys [blocks delimiter]}]
  "Parse string to date:
   - date-str - e.g. '2014-09-19' or '12/31/2022'
   - blocks - expected day, month and year sequence, e.g. ['y' 'm' 'd'] or ['m' 'd' 'y']
   - delimiter - blocks delimiter, e.g. '-' or '/'
   Result: map of :day, :month and :year with integer value if parsed, nil otherwise.
   "
  (let [patterns {"y" "\\d{1,4}"
                  "m" "\\d{1,2}"
                  "d" "\\d{1,2}"}
        pattern (->> blocks
                     (map-indexed vector)
                     (reduce (fn [pattern [idx block]]
                               (str pattern
                                    (if (= idx 0) "" (str delimiter "?"))
                                    "("
                                    (get patterns block)
                                    ")?"))
                             ""))
        matches (-> (re-pattern pattern)
                    (re-matches date-str))

        get-value (fn [key blocks matches]
                    (when (some? matches)
                      (if-let [str-value (->> (.indexOf blocks key)
                                              (inc)
                                              (nth matches))]
                        (int str-value))))]
    {:day   (get-value "d" blocks matches)
     :month (get-value "m" blocks matches)
     :year  (get-value "y" blocks matches)}))

(defn apply-mask
  [{:keys [day month year]} {:keys [blocks delimiter]}]
  (let [values (map #(get {"d" day
                           "m" month
                           "y" year} %)
                    blocks)]
    (->> values
         (map-indexed (fn [idx value]
                        (let [small-value? (< value 10)
                              has-next-value? (some? (nth values (inc idx) nil))
                              last-value? (= idx (dec (count values)))]
                          (str
                            value
                            (if has-next-value?
                              delimiter
                              (if (or last-value?
                                      (and small-value?
                                           (not last-value?)))
                                ""
                                delimiter))))))
         (apply str))))
