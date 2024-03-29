(ns webchange.lesson-builder.tools.object-form.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.state :as state]
    [webchange.lesson-builder.widgets.object-form.views :as o]
    [webchange.ui.index :as ui]))

(defn- object-panel
  [{:keys [class-name target]}]
  (let [object-data @(re-frame/subscribe [::state/object-data target])
        handle-change #(re-frame/dispatch [::state/change-object target %])
        add-asset #(re-frame/dispatch [::state/add-asset %])
        handle-apply-to-all #(re-frame/dispatch [::state/apply-to-all target])]
    [o/object-form {:data            object-data
                    :class-name      class-name
                    :on-change       handle-change
                    :add-asset       add-asset
                    :on-apply-to-all handle-apply-to-all}]))

(defn- group-panel
  [{:keys [target group]}]
  (re-frame/dispatch [::state/init-group target group])
  (fn [{:keys [class-name group]}]
    [:div {:class-name (ui/get-class-name {"group-panel" true
                                           class-name    (some? class-name)})}
     (for [child (:children group)]
       [object-panel {:target (keyword child)}])]))

(defn- panels
  [target]
  (r/with-let []
    (let [object @(re-frame/subscribe [::state/object-data])
          can-remove? @(re-frame/subscribe [::state/can-remove?])
          group? (= "group" (:type object))]
      [:div {:class-name "tool--object-form"}
       [:h1 "Edit"]
       (if group?
         [group-panel {:target     target
                       :group      object
                       :class-name "object-form--form"}]
         [object-panel {:target     target
                        :class-name "object-form--form"}])
       [:div.object-form-actions]
       (when can-remove?
         [ui/button {:class-name "object-form--apply"
                     :color      "blue-1"
                     :on-click   #(re-frame/dispatch [::state/remove target])}
          "Remove"])
       [ui/button {:class-name "object-form--apply"
                   :on-click   #(re-frame/dispatch [::state/apply target])}
        "Apply"]])
    (finally
      (re-frame/dispatch [::state/confirm-close target]))))

(defn object-form
  []
  (let [target @(re-frame/subscribe [::state/target])]
    ^{:key target}
    [panels target]))
