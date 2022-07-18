(ns webchange.lesson-builder.tools.script.target-selector.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.script.target-selector.state :as state]
    [webchange.ui.index :as ui]))

(defn- target-option
  [{:keys [text value on-click]}]
  (let [handle-click #(on-click value)]
    [:div {:class-name "target-selector--option"
           :on-click   handle-click}
     text]))

(defn target-selector
  [{:keys [class-name value on-change]}]
  (r/with-let [expanded? (r/atom false)
               toggle-expanded #(swap! expanded? not)]
    (let [current-value @(re-frame/subscribe [::state/current-value value])
          target-options @(re-frame/subscribe [::state/target-options value])
          handle-option-click #(when (fn? on-change)
                                 (toggle-expanded)
                                 (on-change %))]
      [:div {:class-name (ui/get-class-name {"component--target-selector" true
                                             class-name                   (some? class-name)})}

       [:div {:class-name "target-selector--current-value"
              :on-click   toggle-expanded}
        [ui/icon {:icon       "caret-down"
                  :class-name (ui/get-class-name {"target-selector--expand-icon"         true
                                                  "target-selector--expand-icon--active" @expanded?})}]
        (:text current-value)]
       (when @expanded?
         [:div {:class-name (ui/get-class-name {"target-selector--options" true})}
          (for [{:keys [value] :as option} target-options]
            ^{:key value}
            [target-option (merge option
                                  {:on-click handle-option-click})])])])))
