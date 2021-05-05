(ns webchange.game-changer.steps.select-character.views
  (:require
    [reagent.core :as r]
    [webchange.characters.library :refer [characters]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.list :refer [remove-at-position remove-by-predicate]]))

(defn- characters-list-item
  [{:keys [name on-click preview selected?] :as item}]
  (let [handle-click #(on-click item)]
    [:div.character-item-placeholder
     [:div {:class-name (get-class-name {"character-item-card" true
                                         "selected"            selected?})
            :on-click   handle-click}
      (if (some? preview)
        [:img {:src        preview
               :class-name "preview"}]
        [:div.placeholder])
      [:div.title name]]]))

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
     [:div.characters-list
      (-> (for [[key data] characters]
            ^{:key key}
            [characters-list-item (merge data
                                         {:value     key
                                          :selected? (some (fn [{:keys [skeleton]}] (= skeleton key)) @characters-data)
                                          :on-click  handle-item-click})])
          (doall))]
     (when (> (count characters) max-count)
       [:div.max-count-message
        "Max characters number: " max-count])]))
