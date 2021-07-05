(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn section-block
  [{:keys [open?] :or {open? true}}]
  (let [show-body? (r/atom open?)
        handle-header-click #(swap! show-body? not)]
    (r/create-class
      {:display-name "section-block"

       :component-did-update
                     (fn [this [_ old-props]]
                       (let [new-props (r/props this)]
                         (when-not (= (:open? old-props) (:open? new-props))
                           (reset! show-body? (:open? new-props)))))

       :reagent-render
                     (fn [{:keys [title]}]
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
                                (-> (r/current-component) (r/children))))])})))
