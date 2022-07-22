(ns webchange.lesson-builder.tools.effects-add.character-movements.utils
  (:require
    [webchange.utils.animations :as animation-utils]))

(defn get-characters-with-movements
  [{:keys [activity-data characters animations]}]
  (->> characters
       (filter (fn [[_ {character-name :name}]]
                 (let [character-animations (some (fn [{:keys [animations name]}]
                                                    (and (= name character-name) animations))
                                                  animations)]
                   (some (fn [[_ {:keys [required-animations]}]]
                           (clojure.set/subset? (set required-animations)
                                                (set character-animations)))
                         animation-utils/action-animations))))
       (map (fn [[object-name]]
              (let [object-name (clojure.core/name object-name)]
                {:text  (animation-utils/object->display-name activity-data object-name)
                 :value object-name})))
       (sort-by :text)))
