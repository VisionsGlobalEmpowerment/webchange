(ns webchange.course.walk)

(defn text-lines
  [{:keys [objects]}]
  (let [lines (->> objects
                   (filter #(-> % second :type (= "text")))
                   (map (fn [[k v]] {:type :object
                                     :path [:objects k]
                                     :text (:text v)})))]
    lines))

(defn- walk-dialog
  [path {:keys [type data] :as action}]
  (case type
    "parallel" (->> data
                    (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d)))
                    flatten)
    "sequence-data" (->> data
                         (map-indexed (fn [i d] (walk-dialog (concat path [:data i]) d)))
                         flatten)
    "animation-sequence" [{:type :action
                           :path path
                           :text (:phrase-text action)}]
    nil))


(defn dialog-lines
  [{:keys [actions]}]
  (let [dialogs (->> actions
                     (filter #(-> % second :editor-type (= "dialog"))))]
    (->> dialogs
         (mapcat (fn [[key action]] (walk-dialog [:actions key] action)))
         (remove nil?))))
