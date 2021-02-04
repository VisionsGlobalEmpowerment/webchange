(ns webchange.editor-v2.scenes-crossing.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.scenes-crossing.state :as state]
    [webchange.editor-v2.scenes-crossing.state-locations :as state-locations]
    [webchange.editor-v2.scenes-crossing.views-locations :refer [location-settings]]
    [webchange.routes :refer [redirect-to]]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- scenes-list
  []
  (let [scenes-list @(re-frame/subscribe [::state/course-scenes])
        current-scene @(re-frame/subscribe [::state/current-scene])
        handle-item-click #(re-frame/dispatch [::state/set-current-scene %])]
    [ui/menu-list {:style {:height   "100%"
                           :overflow "auto"}}
     (for [{:keys [id name] :as scene-data} scenes-list]
       ^{:key id}
       [ui/menu-item {:selected (= id (:id current-scene))
                      :on-click #(handle-item-click scene-data)}
        name])]))

(defn- scene-out-select
  [{:keys [value object]}]
  (let [scene-out-options @(re-frame/subscribe [::state/scene-out-options])
        handle-change #(re-frame/dispatch [::state/change-scene-out object (.. % -target -value)])]
    [:div {:style {:display        "inline-block"
                   :vertical-align "top"}}
     [ui/select {:value     value
                 :variant   "outlined"
                 :on-change handle-change
                 :style     {:width "200px"}}
      (for [{:keys [id name]} scene-out-options]
        ^{:key id}
        [ui/menu-item {:value id} name])]]))

(defn- scene-out-label
  [{:keys [title style]
    :or   {style {}}}]
  [ui/typography {:style (merge {:color   (get-in-theme [:palette :text :secondary])
                                 :display "inline-block"}
                                style)}
   title])

(defn- scene-out-row
  [{:keys [object scene]}]
  (let [has-locations? @(re-frame/subscribe [::state-locations/has-locations? scene])
        handle-init-locations #(re-frame/dispatch [::state-locations/init-scene-locations scene])]
    [:div {:style {:margin-bottom "8px"}}
     [:div {:style {:display "inline-block"
                    :width   "300px"}}
      [ui/typography {:variant "body1"
                      :style   {:padding "6px 16px"}}
       (clojure.string/capitalize object)]
      (when has-locations?
        [location-settings {:scene-id scene}])]
     [scene-out-select {:value  scene
                        :object object}]
     (when-not has-locations?
       [ui/tooltip {:title "Init locations"}
        [ui/icon-button {:on-click handle-init-locations
                         :style    {:padding "7px"}}
         [ic/flip-to-front]]])]))

(defn- scene-outs
  []
  (let [scene-outs-data @(re-frame/subscribe [::state/scene-outs])]
    [ui/paper {:style {:box-sizing "content-box"
                       :padding    "16px"
                       :width      "540px"}}
     [:div
      [scene-out-label {:title "Object name"
                        :style {:padding "6px 16px"
                                :width   "300px"}}]
      [scene-out-label {:title "Scene"}]]
     [ui/divider {:style {:margin "8px 0 16px 0"}}]
     (for [{:keys [object scene]} scene-outs-data]
       ^{:key object}
       [scene-out-row {:object object
                       :scene  scene}])]))

(defn scenes-crossing
  [{:keys [course-id]}]
  (r/with-let [_ (re-frame/dispatch [::state/init course-id])]
    [layout {:breadcrumbs [{:text     "Course"
                            :on-click #(redirect-to :course-editor-v2 :id course-id)}
                           {:text "Crossings"}]}
     [ui/grid {:container true :spacing 40
               :style     {:height "100%"}}
      [ui/grid {:item  true :xs 3
                :style {:height "100%"}}
       [ui/paper {:style {:height "100%"}}
        [scenes-list]]]
      [ui/grid {:item true :xs 9}
       [ui/grid {:container true :spacing 16}
        [ui/grid {:item true :xs 12}
         [scene-outs]]]]]]))
