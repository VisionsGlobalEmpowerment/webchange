(ns webchange.ui.components.dialog.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.overlay.views :refer [focus-overlay loading-overlay]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn dialog
  [{:keys [actions
           class-name
           class-name-actions
           class-name-content
           class-name-overlay
           on-close
           title]}]
  [focus-overlay {:class-name class-name-overlay}
   [:div {:class-name (get-class-name {"bbs--dialog" true
                                       class-name    (some? class-name)})}
    (when (some? title)
      [:div.header
       [:h1 title]
       (when (fn? on-close)
         [button {:icon     "close"
                  :color    "blue-1"
                  :on-click on-close}])])
    (->> (r/current-component)
         (r/children)
         (into [:div {:class-name (get-class-name {"bbs--dialog--content" true
                                                   class-name-content     (some? class-name-content)})}]))
    (when (some? actions)
      [:div {:class-name (get-class-name {"bbs--dialog--actions" true
                                          class-name-actions     (some? class-name-actions)})}
       actions])]])

(defn confirm
  [{:keys [class-name class-name-content class-name-overlay confirm-text cancel-text loading? on-confirm on-cancel open? title]
    :or   {confirm-text "Yes"
           cancel-text  "Cancel"}}]
  (when open?
    (into [dialog
           {:title              title
            :class-name         (get-class-name {"bbs--confirm-window" true
                                                 class-name            (some? class-name)})
            :class-name-actions (str "bbs--dialog--actions--" (cond-> 0
                                                                      (fn? on-confirm) (inc)
                                                                      (fn? on-cancel) (inc)))
            :class-name-content class-name-content
            :class-name-overlay class-name-overlay
            :actions            (when-not loading?
                                  [:<>
                                   (when (fn? on-cancel)
                                     [button {:color    "blue-1"
                                              :on-click on-cancel}
                                      cancel-text])
                                   (when (fn? on-confirm)
                                     [button {:on-click on-confirm}
                                      confirm-text])
                                   ])}]
          (if-not loading?
            (->> (r/current-component)
                 (r/children))
            [[focus-overlay]]))))
