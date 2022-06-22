(ns webchange.ui.components.text-area.views
  (:require
    [webchange.ui.components.input-error.views :refer [input-error]]
    [webchange.ui.components.input-label.views :refer [input-label]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn text-area
  [{:keys [class-name
           disabled?
           error
           id
           label
           on-change
           placeholder
           required?
           rows
           value
           variant]
    :or   {disabled?   false
           placeholder ""
           on-change   #()
           rows        3}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))
        has-label? (some? label)
        id (or id (when has-label? (random-uuid)))]
    [:div {:class-name       (get-class-name (-> {"bbs--text-area" true}
                                                 (assoc (str "variant-" variant) true)
                                                 (assoc class-name (some? class-name))))
           :data-wrapper-for id}
     [:div {:class-name "bbs--text-area-wrapper--header"}
      (when has-label?
        [input-label {:for       id
                      :required? required?}
         label])
      (when (some? error)
        [input-error error])]
     [:textarea (cond-> {:value       value
                         :on-change   handle-change
                         :placeholder placeholder
                         :disabled    disabled?
                         :rows        rows}
                        (some? id) (assoc :id id))]]))
