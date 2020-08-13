(ns webchange.editor-v2.wizard.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.course :as state-course]
    [webchange.editor-v2.wizard.state.activity :as state-activity]))

(defn- course-info
  []
  (r/with-let [data (r/atom {})]
    (let [datasets @(re-frame/subscribe [::state-course/datasets-library])]
      [ui/card {:style {:margin      "12px"
                        :flex-shrink "0"}}
       [ui/card-content
        [ui/grid {:container   true
                  :justify     "space-between"
                  :spacing     24
                  :align-items "center"}
         [ui/grid {:item true :xs 7}
          [ui/text-field {:label         "Name"
                          :full-width    true
                          :variant       "outlined"
                          :default-value (:name @data)
                          :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]
         [ui/grid {:item true :xs 7}
          [ui/text-field {:label         "Language"
                          :full-width    true
                          :variant       "outlined"
                          :default-value (:lang @data)
                          :on-change     #(swap! data assoc :lang (-> % .-target .-value))}]]
         [ui/grid {:item true :xs 7}
          [ui/input-label "Concept list"]
          [ui/select {:value (:dataset-id @data)
                      :on-change #(swap! data assoc :dataset-id (-> % .-target .-value))
                      :style     {:min-width "150px"}}
           (for [dataset datasets]
             ^{:key (:id dataset)}
             [ui/menu-item {:value (:id dataset)} (:name dataset)])]]]]
       [ui/card-actions
        [ui/button {:color    "secondary"
                    :style    {:margin-left "auto"}
                    :on-click #(re-frame/dispatch [::state-course/create-course @data])}
         "Save"]]])))

(defn create-course-panel
  []
  (re-frame/dispatch [::state-course/load-datasets-library])
  [layout {:title "Course"}
   [:div {:style {:height         "100%"
                  :display        "flex"
                  :flex-direction "column"}}
    [course-info]]])

(defn- activity-info
  [course-id]
  (r/with-let [data (r/atom {})]
    (let [templates @(re-frame/subscribe [::state-activity/templates])
          skills @(re-frame/subscribe [::state-activity/skills])]
      [ui/card {:style {:margin      "12px"
                        :flex-shrink "0"}}
       [ui/card-content
        [ui/grid {:container   true
                  :justify     "space-between"
                  :spacing     24
                  :align-items "center"}
         [ui/grid {:item true :xs 7}
          [ui/text-field {:label         "Name"
                          :full-width    true
                          :variant       "outlined"
                          :default-value (:name @data)
                          :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]
         [ui/grid {:item true :xs 7}
          [ui/input-label "Template"]
          [ui/select {:value (:template-id @data)
                      :on-change #(swap! data assoc :template-id (-> % .-target .-value))
                      :style     {:min-width "150px"}}
           (for [template templates]
             ^{:key (:id template)}
             [ui/menu-item {:value (:id template)} (:name template)])]]]]
       [ui/card-actions
        [ui/button {:color    "secondary"
                    :style    {:margin-left "auto"}
                    :on-click #(re-frame/dispatch [::state-activity/create-activity course-id @data])}
         "Save"]]])))

(defn create-activity-panel
  [course-id]
  (re-frame/dispatch [::state-activity/load-templates])
  (re-frame/dispatch [::state-activity/load-skills])
  [layout {:title "Activity"}
   [:div {:style {:height         "100%"
                  :display        "flex"
                  :flex-direction "column"}}
    [activity-info course-id]]]))
