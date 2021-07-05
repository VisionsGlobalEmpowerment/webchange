(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-common
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn target-control
  [{:keys [value options on-change]}]
  [menu {:class-name "targets-menu"
         :el         (r/as-element [:span.target-value
                                    (if-not (empty? value) value "select target")])
         :items      (->> options
                          (map (fn [{:keys [text value]}]
                                 {:text     text
                                  :on-click #(on-change value)})))}])

(defn text-control
  [{:keys [on-change on-enter-press on-ctrl-enter-press]}]
  (let [ref (atom nil)
        handle-key-down (fn [e]
                          (when (= (.-key e) "Enter")
                            (.preventDefault e)
                            (if (.-ctrlKey e)
                              (on-ctrl-enter-press)
                              (on-enter-press))))]
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :component-did-mount
                     (fn [this]
                       (let [{:keys [editable?]} (r/props this)]
                         (when editable?
                           (.addEventListener @ref "keydown" handle-key-down false))))
       :component-will-unmount
                     (fn []
                       (.removeEventListener @ref "keydown" handle-key-down))
       :reagent-render
                     (fn [{:keys [value editable?]}]
                       (let [handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (on-change new-value)))]
                         [:span (cond-> {:class-name (get-class-name {"text"          true
                                                                      "text-disabled" (not editable?)})
                                         :ref        #(when (some? %) (reset! ref %))}
                                        editable? (merge {:on-input                          handle-change
                                                          :content-editable                  true
                                                          :suppress-content-editable-warning true}))
                          value]))})))
