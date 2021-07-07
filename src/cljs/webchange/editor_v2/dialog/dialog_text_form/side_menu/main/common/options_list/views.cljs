(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views
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
                          (.addEventListener @el "dragstart" handle-drag-start))]
    (let [{:keys [text value selected?]} data
          handle-click #(on-click value)]
      [:li (cond-> {:class-name (get-class-name {"options-list-item" true
                                                 "selected"          selected?})
                    :on-click   handle-click}
                   (fn? get-drag-data) (-> (assoc :draggable true)
                                           (assoc :ref #(when (some? %) (init-dnd %)))))
       text])
    (finally
      (.removeEventListener @el "dragstart" handle-drag-start))))

(defn options-list
  [{:keys [options on-click get-drag-data]}]
  [:ul.options-list
   (for [{:keys [value] :as option} options]
     ^{:key value}
     [options-list-item {:data          option
                         :on-click      on-click
                         :get-drag-data get-drag-data}])])
