(ns webchange.lesson-builder.widgets.design-actions.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.design-actions.state :as state]
    [webchange.ui.index :as ui]

    [webchange.lesson-builder.tools.effects-add.views :as effects-add-views]
    [webchange.lesson-builder.widgets.activity-actions.views :as activity-actions-views]
    [webchange.lesson-builder.tools.question-options.views :as question-options-views]))

(def content-components {:activity-actions activity-actions-views/activity-actions
                         :effects-add      effects-add-views/effects-add
                         :question-options question-options-views/question-options})

(defn- action-item
  [{:keys [content icon text] :as props}]
  (let [active? (and (some? content)
                     @(re-frame/subscribe [::state/active-menu-item? content]))
        handle-click #(re-frame/dispatch [::state/handle-item-click props])]
    [:div {:class-name "design-action"}
     [:div {:class-name (ui/get-class-name {"design-action--header"         true
                                            "design-action--header--active" active?})
            :on-click   handle-click}
      [ui/icon {:icon       icon
                :class-name "design-action--icon"}]
      [:div.design-action--name
       text]
      [ui/icon {:icon       (if active? "caret-down" "caret-right")
                :class-name "design-action--expand"}]]
     (if-let [child-component (get content-components content)]
       (when active?
         [:div.design-action--content
          [child-component]]))]))

(defn design-actions
  []
  (let [actions @(re-frame/subscribe [::state/actions])]
    [:div.widget--design-actions
     (for [{:keys [id] :as action} actions]
       ^{:key id}
       [action-item action])]))
