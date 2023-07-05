(ns webchange.utils.animations
  (:require
    [clojure.string :as s]
    [webchange.utils.scene-data :as activity-data-utils]))

(def action-animations {"go-to"   {:title               "Go to"
                                   :required-animations ["walk"]}
                        "pick-up" {:title               "Pick up item"
                                   :required-animations ["walk" "pick-up_item"]
                                   :target-type         ["image"]}
                        "put"     {:title               "Put item"
                                   :required-animations ["walk" "put_item"]
                                   :target-type         ["anchor"]}
                        "give"    {:title                      "Give item"
                                   :required-animations        ["walk" "give_item"]
                                   :target-type                ["animation"]
                                   :target-required-animations ["take_item"]}})

(defn ->display-name
  [object-name]
  (-> (or object-name "")
      (s/replace "-" " ")
      (s/capitalize)))

(defn- object->display-name
  [activity-data object-name]
  (let [{:keys [scene-name]} (activity-data-utils/get-scene-object activity-data object-name)]
    (-> (or scene-name object-name)
        (->display-name))))

(defn action->display-name
  [{:keys [activity-data character target action]}]
  (str (object->display-name activity-data character)
       " "
       (case action
         "give" "gives"
         "go-to" "goes to"
         "pick-up" "picks up"
         "put" "puts"
         "->")
       " "
       (object->display-name activity-data target)))

(defn emotion-animation?
  [animation-name]
  (s/starts-with? animation-name "emotion_"))
