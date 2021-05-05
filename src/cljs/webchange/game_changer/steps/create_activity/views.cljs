(ns webchange.game-changer.steps.create-activity.views
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [label text-input]]))

(defn- generate-language
  []
  "English")

(defn- generate-course-name
  [data]
  (let [template-name (get-in data [:template :name])]
    (-> (str template-name " Course")
        (->Camel_Snake_Case template-name)
        (clojure.string/replace #"_" " "))))

(defn- generate-activity-name
  [data]
  (let [template-name (get-in data [:template :name])]
    (-> (str template-name " Activity")
        (->Camel_Snake_Case template-name)
        (clojure.string/replace #"_" " "))))

(defn- get-language
  [data]
  (get-in @data [:lang]))

(defn- get-course-name
  [data]
  (get-in @data [:course :name]))

(defn- get-activity-name
  [data]
  (get-in @data [:activity :name]))

(defn create-activity
  [{:keys [data]}]
  (r/with-let [handle-language-change (fn [value] (swap! data assoc :lang value))
               handle-course-name-change (fn [value] (swap! data assoc-in [:course :name] value))
               handle-activity-name-change (fn [value] (swap! data assoc-in [:activity :name] value))

               _ (when-not (some? (get-language data))
                   (handle-language-change (generate-language)))
               _ (when-not (some? (get-course-name data))
                   (handle-course-name-change (generate-course-name @data)))
               _ (when-not (some? (get-activity-name data))
                   (handle-activity-name-change (generate-activity-name @data)))]
    [:div.create-activity-from
     [label {:class-name "label"} "Activity Name"]
     [text-input {:value       (get-activity-name data)
                  :on-change   handle-activity-name-change
                  :placeholder "Enter Activity Name"}]

     [label {:class-name "label"} "Language"]
     [text-input {:value       (get-language data)
                  :on-change   handle-language-change
                  :placeholder "Enter Language"}]]))
