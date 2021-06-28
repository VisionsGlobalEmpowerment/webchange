(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn section-block
  [{:keys [title open?] :or {open? true}}]
  (r/with-let [show-body? (r/atom open?)
               handle-header-click #(swap! show-body? not)]
    [:div.section-block
     [:div {:class-name (get-class-name {"section-block-header" true
                                         "expanded"             @show-body?})
            :on-click   handle-header-click}
      [icon {:class-name "section-block-header-icon"
             :icon       (if @show-body?
                           "expand-arrow-down"
                           "expand-arrow-right")}]

      title]
     (when @show-body?
       (into [:div.section-block-body]
             (-> (r/current-component) (r/children))))]))
