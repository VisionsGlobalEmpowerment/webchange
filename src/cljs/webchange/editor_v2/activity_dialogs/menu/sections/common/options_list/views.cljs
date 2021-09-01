(ns webchange.editor-v2.activity-dialogs.menu.sections.common.options-list.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.drag-and-drop :as utils]))

(defn- options-list-item
  [{:keys [data on-click get-drag-data]}]
  (r/with-let [el (atom nil)
               ;; Init dnd
               handle-drag-start #(->> (get-drag-data data)
                                       (utils/set-transfer-data %))
               init-dnd (fn [ref]
                          (reset! el ref)
                          (.addEventListener @el "dragstart" handle-drag-start))
               destroy-dnd (fn []
                             (when (some? @el)
                               (.removeEventListener @el "dragstart" handle-drag-start)))]
    (let [{:keys [text value selected?]} data
          handle-click (fn []
                         (when (fn? on-click)
                           (on-click value)))]
      [:li (cond-> {:class-name (get-class-name {"options-list-item" true
                                                 "selected"          selected?})
                    :on-click   handle-click}
                   (fn? get-drag-data) (-> (assoc :draggable true)
                                           (assoc :ref #(when (some? %) (init-dnd %)))))
       text])
    (finally
      (destroy-dnd))))

(defn options-list
  [{:keys [options option-key on-click get-drag-data]
    :or   {option-key :value}}]
  [:ul.options-list
   (for [option options]
     ^{:key (get option option-key)}
     [options-list-item {:data          option
                         :on-click      on-click
                         :get-drag-data get-drag-data}])])
