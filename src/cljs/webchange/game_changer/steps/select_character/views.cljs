(ns webchange.game-changer.steps.select-character.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]
    [webchange.game-changer.steps.select-character.state :as state]
    [webchange.game-changer.steps.select-character.views-available-list :refer [available-characters-list]]
    [webchange.game-changer.steps.select-character.views-selected-list :refer [selected-characters-list]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.list :refer [remove-at-position replace-at-position]]))

(defn- add-character
  [list {:keys [name skeleton skin] :as data}]
  {:pre [(some? name)
         (some? skeleton)
         (some? skin)]}
  (swap! list conj (select-keys data [:name :skeleton :skin])))

(defn- remove-character
  [list idx]
  (reset! list (remove-at-position @list idx)))

(defn- change-character
  [list idx data]
  (reset! list (replace-at-position @list data idx)))

(defn get-character-option-path
  [data]
  (let [{option-key :key} (data->character-option data)]
    [:options (keyword option-key)]))

(defn select-character
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])

               open-available-characters? (r/atom false)

               {max-count :max} (data->character-option @data)
               characters-data (connect-data data (get-character-option-path @data) [])

               handle-add (fn [char]
                            (when (< (count @characters-data) max-count)
                              (reset! open-available-characters? false)
                              (add-character characters-data char)))
               handle-remove (fn [{:keys [idx]}]
                               (remove-character characters-data idx))
               handle-change (fn [{:keys [idx] :as character}]
                               (change-character characters-data idx (select-keys character [:name :skeleton :skin])))

               handle-open-available (fn []
                                       (reset! open-available-characters? true))]
    [:div.activity-characters
     [selected-characters-list {:data            @characters-data
                                :add-disabled?   (>= (count @characters-data) max-count)
                                :on-add-click    handle-open-available
                                :on-remove-click handle-remove
                                :on-change       handle-change}]
     (when @open-available-characters?
       [available-characters-list {:on-click handle-add}])]))
