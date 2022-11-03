(ns webchange.lesson-builder.tools.script.target-selector.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.script.target-selector.state :as state]
    [webchange.ui.index :as ui]))

(defn- target-option
  [{:keys [display-name text value on-click]}]
  (let [handle-click #(on-click value)]
    [:div {:class-name "target-selector--option"
           :on-click   handle-click}
     (if (some? display-name)
       display-name
       text)]))

(defn target-selector
  [{:keys [class-name value on-change type]
    :or   {type :character}}]
  (r/with-let [expanded? (r/atom false)
               toggle-expanded #(swap! expanded? not)]
    (let [current-value-data @(re-frame/subscribe [::state/current-value-data type value])
          target-options @(re-frame/subscribe [::state/target-options type value])
          handle-option-click #(when (fn? on-change)
                                 (toggle-expanded)
                                 (on-change %))]
      [:div {:class-name (ui/get-class-name {"component--target-selector" true
                                             class-name                   (some? class-name)})}

       [:div {:class-name (ui/get-class-name {"target-selector--current-value"        true
                                              "target-selector--current-value--empty" (nil? current-value-data)})
              :on-click   toggle-expanded}
        [ui/icon {:icon       "caret-down"
                  :class-name (ui/get-class-name {"target-selector--expand-icon"         true
                                                  "target-selector--expand-icon--active" @expanded?})}]
        (or
         (get current-value-data :display-name)
         (get current-value-data :text)
         "Select target")]
       (when @expanded?
         [:div {:class-name (ui/get-class-name {"target-selector--options" true})}
          (for [{:keys [value] :as option} target-options]
            ^{:key value}
            [target-option (merge option
                                  {:on-click handle-option-click})])])])))
