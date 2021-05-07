(ns webchange.game-changer.steps.select-character.views
  (:require
    [reagent.core :as r]
    [webchange.characters.library :refer [characters]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]
    [webchange.game-changer.steps.select-character.views-available-list :refer [available-characters-list]]
    [webchange.game-changer.steps.select-character.views-selected-list :refer [selected-characters-list]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.list :refer [remove-at-position remove-by-predicate]]))

(defn- add-character
  [list {:keys [name skeleton skin] :as data}]
  {:pre [(some? name)
         (some? skeleton)
         (some? skin)]}
  (swap! list conj (select-keys data [:name :skeleton :skin])))

(defn- remove-character
  [list idx]
  (reset! list (remove-at-position @list idx)))

(defn get-character-option-path
  [data]
  (let [{option-key :key} (data->character-option data)]
    [:options (keyword option-key)]))

(defn select-character
  [{:keys [data]}]
  (r/with-let [open-available-characters? (r/atom false)

               {max-count :max} (data->character-option @data)
               characters-data                              ;(connect-data data (get-character-option-path @data) [])
               (r/atom [{:name     "Child Cow 5"
                         :skeleton :vera
                         :skin     "01 Vera_1"
                         :preview  "/images/characters/child.png"}
                        {:name     "Child Girl 6"
                         :skeleton :vera
                         :skin     "girl"
                         :preview  nil}
                        {:name     "Adult Cow 7"
                         :skeleton :senoravaca
                         :skin     "vaca"
                         :preview  nil}])
               handle-add (fn [char]
                            (reset! open-available-characters? false)
                            (add-character characters-data char))
               handle-remove (fn [{:keys [idx]}]
                               (remove-character characters-data idx))

               handle-open-available (fn []
                                       (reset! open-available-characters? true))
               ]
    [:div.activity-characters
     [selected-characters-list {:data            @characters-data
                                :on-add-click    handle-open-available
                                :on-remove-click handle-remove}]
     (when @open-available-characters?
       [available-characters-list {:on-click handle-add}])]))
