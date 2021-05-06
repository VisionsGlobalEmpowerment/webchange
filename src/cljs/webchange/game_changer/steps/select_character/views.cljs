(ns webchange.game-changer.steps.select-character.views
  (:require
    [reagent.core :as r]
    [webchange.characters.library :refer [characters]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]
    [webchange.game-changer.steps.select-character.views-available-list :refer [available-characters-list]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.list :refer [remove-at-position remove-by-predicate]]))

(defn- add-character
  [list name skeleton]
  (swap! list conj {:name     name
                    :skeleton skeleton}))

(defn- remove-character
  [list skeleton]
  (reset! list (remove-by-predicate @list #(= skeleton (:skeleton %)))))

(defn- remove-first-character
  [list]
  (reset! list (remove-at-position @list 0)))

(defn get-character-option-path
  [data]
  (let [{option-key :key} (data->character-option data)]
    [:options (keyword option-key)]))

(defn select-character
  [{:keys [data]}]
  (r/with-let [{max-count :max} (data->character-option @data)
               characters-data (connect-data data (get-character-option-path @data) [])
               handle-item-click (fn [{:keys [name selected? value]}]
                                   (when (= (count @characters-data) max-count)
                                     (remove-first-character characters-data))
                                   (if-not selected?
                                     (add-character characters-data name value)
                                     (remove-character characters-data value)))]
    [:div
     [available-characters-list]]))
