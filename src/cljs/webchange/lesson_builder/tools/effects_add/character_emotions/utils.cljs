(ns webchange.lesson-builder.tools.effects-add.character-emotions.utils
  (:require
    [webchange.utils.animations :as animation-utils]
    [webchange.utils.scene-action-data :as action-utils]))

(defn get-characters-with-emotions
  [{:keys [activity-data characters animations]}]
  (->> characters
       (filter (fn [[_ {character-name :name}]]
                 (let [character-animations (some (fn [{:keys [animations name]}]
                                                    (and (= name character-name) animations))
                                                  animations)]
                   (some animation-utils/emotion-animation? character-animations))))
       (map (fn [[object-name]]
              (let [object-name (clojure.core/name object-name)]
                {:text  (animation-utils/object->display-name activity-data object-name)
                 :value object-name})))
       (sort-by :text)))

(defn get-character-emotions
  [{:keys [character characters animations]}]
  (let [character-name (some (fn [[object-name {:keys [name]}]]
                               (and (= object-name (keyword character)) name))
                             characters)]
    (->> (some (fn [{:keys [animations name]}]
                 (and (= name character-name) animations))
               animations)
         (filter animation-utils/emotion-animation?)
         (map (fn [emotion]
                {:text  (action-utils/animation->display-name emotion)
                 :value emotion}))
         (sort-by :text))))
