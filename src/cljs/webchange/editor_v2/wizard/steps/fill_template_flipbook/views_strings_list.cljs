(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-strings-list
  (:require
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-control-row :refer [control-row]]
    [webchange.ui-framework.components.index :refer [button icon-button text-input]]
    [webchange.utils.list :refer [remove-at-position replace-at-position]]))

(defn strings-list
  [{:keys [data error-message label max]
    :or   {max 5}}]
  (let [handle-add-click (fn [] (swap! data conj ""))
        handle-input-change (fn [idx value]
                              (swap! data replace-at-position idx value))
        handle-remove-click (fn [idx]
                              (if (> (count @data) 1)
                                (swap! data remove-at-position idx)
                                (reset! data [""])))]
    [:div.strings-list
     (doall (for [[idx value] (map-indexed vector @data)]
              ^{:key idx}
              [control-row {:label         label
                            :error-message (when (= idx 0) error-message)
                            :control       [:div.controls-container
                                            [:div.input-container
                                             [text-input {:value     value
                                                          :on-change #(handle-input-change idx %)}]
                                             [icon-button {:icon       "remove"
                                                           :class-name "remove-button"
                                                           :on-click   #(handle-remove-click idx)}]]
                                            (when (and (= idx 0)
                                                       (< (count @data) max))
                                              [button {:variant    "rectangle"
                                                       :class-name "add-button"
                                                       :on-click   handle-add-click}
                                               "+ Add"])]}]))]))
