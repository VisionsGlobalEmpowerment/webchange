(ns webchange.game-changer.steps.select-character.index
  (:require
    [webchange.game-changer.steps.select-character.views :refer [get-character-option-path select-character]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]))

(def data {:title      "Select Character"
           :component  select-character
           :available? (fn [{:keys [data]}]
                         (-> data data->character-option some?))
           :passed?    (fn [{:keys [data]}]
                         (->> (get-character-option-path data)
                              (get-in data)
                              (empty?)
                              (not)))})
