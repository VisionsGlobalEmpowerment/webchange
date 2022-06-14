(ns webchange.editor-v2.creation-progress.views-full-info
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]
    [webchange.editor-v2.creation-progress.state :as state]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]))

(defn- block-wrapper
  [{:keys [title progress]}]
  (r/with-let [this (r/current-component)
               open? (r/atom false)]
    (let [progress-value (-> progress (* 100) (Math/round))]
      [ui/grid {:item true :xs 12}
       [ui/grid {:container true
                 :spacing   8}
        [ui/grid {:item true :xs 12}
         [ui/grid {:container true
                   :spacing   8
                   :on-click  #(swap! open? not)
                   :style     {:cursor "pointer"}}
          [ui/grid {:item true :xs 4}
           [ui/typography {:variant "subtitle1"} title]]
          [ui/grid {:item true :xs 1}
           (when-not (= progress-value 100)
             [warning-icon {:styles {:main {:font-size  "18px"
                                            :margin-top "5px"}}}])]
          [ui/grid {:item  true :xs 5
                    :style {:display     "flex"
                            :align-items "center"}}
           [ui/linear-progress {:variant "determinate"
                                :value   progress-value
                                :style   {:width "100%"}}]]
          [ui/grid {:item true :xs 1}
           [ui/typography {:variant "subtitle1"} progress-value "%"]]
          [ui/grid {:item  true :xs 1
                    :style {:justify-content "flex-end"
                            :display         "flex"}}
           [ui/typography (if @open?
                            [ic/expand-less]
                            [ic/expand-more])]]]]
        (into [ui/collapse {:unmount-on-exit true
                            :timeout         "auto"
                            :in              @open?
                            :style           {:width "100%"}}]
              (r/children this))]])))

(defn- concept-actions
  [{:keys [fields on-edit-click]}]
  [ui/grid {:container true
            :spacing   8}
   [ui/grid {:item true :xs 12} [ui/typography "Actions:"]]
   [:ul {:style {:list-style   "disc"
                 :color        "white"
                 :padding-left "44px"}}
    (for [{:keys [name action]} fields]
      ^{:key name}
      [ui/grid {:item true :xs 12}
       [:li
        [ui/grid {:container true
                  :spacing   8}
         [ui/grid {:item true :xs 10}
          [ui/typography {:style {:font-size "10px"}} (:title action)]]
         [ui/grid {:item true :xs 2}
          [ui/button {:size     "small"
                      :style    {:padding 0}
                      :on-click #(on-edit-click (:id action))}
           "Edit"]]]]])]])

(defn- concept-resources
  [{:keys [fields on-edit-click]}]
  [ui/grid {:container true
            :spacing   8}
   [ui/grid {:item true :xs 10}
    [ui/typography "Resources:"]
    [:ul {:style {:list-style "disc"
                  :color      "white"}}
     (for [{:keys [name]} fields]
       ^{:key name}
       [:li [ui/typography {:style {:font-size "10px"}} name]])]]
   [ui/grid {:item  true :xs 2
             :style {:display     "flex"
                     :align-items "center"
                     :margin-left "-25px"}}
    [ui/button {:size     "small"
                :on-click on-edit-click}
     "Edit"]]])

(defn- concepts-info
  [{:keys [data]}]
  (let [{:keys [progress warnings]} data
        handle-edit-concept-click (fn [concept-id]
                                    (re-frame/dispatch [::state/show-translation-short-progress])
                                    (re-frame/dispatch [::state/edit-concept concept-id]))
        handle-edit-action-click (fn [action-id]
                                   (re-frame/dispatch [::state/show-translation-short-progress])
                                   (re-frame/dispatch [::state/edit-action action-id]))]
    [block-wrapper {:title    "Concepts"
                    :progress progress}
     [ui/list
      (for [[{:keys [id name]} fields] (sort #(compare (-> %1 first :name) (-> %2 first :name)) warnings)]
        (let [actions-fields (filter (fn [{:keys [type]}] (= type :action)) fields)
              resources-fields (filter (fn [{:keys [type]}] (= type :resource)) fields)
              has-warnings? (or (not (empty? resources-fields))
                                (not (empty? actions-fields)))]
          (when has-warnings?
            ^{:key id}
            [ui/list-item
             [ui/grid {:container true
                       :spacing   8
                       :justify   "space-between"}
              [ui/grid {:item true :xs 2} [ui/typography name]]
              [ui/grid {:item true :xs 10}
               (when-not (empty? resources-fields)
                 [concept-resources {:fields        resources-fields
                                     :on-edit-click #(handle-edit-concept-click id)}])
               (when-not (empty? actions-fields)
                 [concept-actions {:fields        actions-fields
                                   :on-edit-click handle-edit-action-click}])]]])))]]))

(defn- dialogs-info
  [{:keys [data]}]
  (let [{:keys [progress warnings]} data
        handle-edit-action-click (fn [action-id]
                                   (re-frame/dispatch [::state/show-translation-short-progress])
                                   (re-frame/dispatch [::state/edit-action action-id]))]
    [block-wrapper {:title    "Dialogs"
                    :progress progress}
     [ui/list
      (for [[{:keys [id label]}] warnings]
        ^{:key id}
        [ui/list-item
         [ui/grid {:container true
                   :spacing   8
                   :justify   "space-between"}
          [ui/grid {:item true :xs 10} [ui/typography label]]
          [ui/grid {:item true :xs 2}
           [ui/button {:size     "small"
                      :style    {:padding 0}
                      :on-click #(handle-edit-action-click id)}
           "Edit"]]]])]]))

(defn- full-info-form
  []
  (let [progress-data @(re-frame/subscribe [::state/translation-progress])
        progress-value (-> (:progress progress-data) (* 100) (Math/round))
        handle-hide-click (fn [] (re-frame/dispatch [::state/show-translation-short-progress]))]
    [ui/grid {:container true
              :spacing   8
              :justify   "space-between"}
     ;; Header
     [ui/grid {:item true :xs 5}
      [ui/typography {:variant "h6"} "Activity Completion"]]
     [ui/grid {:item  true :xs 5
               :style {:display     "flex"
                       :align-items "center"}}
      [ui/linear-progress {:variant "determinate"
                           :value   progress-value
                           :style   {:width "100%"}}]]
     [ui/grid {:item true :xs 1}
      [ui/typography {:variant "h6"} progress-value "%"]]
     [ui/grid {:item  true :xs 1
               :style {:display     "flex"
                       :align-items "center"}}
      (when-not (= progress-value 100)
        [warning-icon])]
     [ui/grid {:item true :xs 12} [ui/divider]]

     ;; Content
     [ui/grid {:item  true :xs 12
               :style {:max-height "500px"
                       :overflow   "auto"}}
      (for [{:keys [entity] :as data} (:overall-data progress-data)]
        ^{:key entity}
        [:div {:style {:width "100%"}}
         (case entity
           :concepts [concepts-info {:data data}]
           :dialogs [dialogs-info {:data data}]
           [:div "Unknown entity data"])])]

     ;; Footer
     [ui/grid {:item true :xs 12} [ui/divider]]
     [ui/grid {:item  true :xs 12
               :style {:text-align "right"}}
      [ui/button {:on-click handle-hide-click}
       "Hide"]]]))

(defn full-info
  []
  (let [{:keys [mode show?]} @(re-frame/subscribe [::state/window-state])]
    [ui/snackbar {:open          (and show? (= mode :full-info))
                  :anchor-origin {:vertical   "bottom"
                                  :horizontal "right"}}
     [ui/snackbar-content {:style   {:background-color (get-in-theme [:palette :background :paper])}
                           :message (r/as-element [full-info-form])}]]))
