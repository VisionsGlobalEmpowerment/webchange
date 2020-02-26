(ns webchange.editor.components.data-sets.views-edit-field-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.editor.common.insert-json-modal :refer [insert-json-modal]]
    [webchange.editor.components.data-sets.common :refer [item-field-types]]))

(defn- field-row
  [label control]
  [ui/grid {:container true
            :spacing   16
            :style     {:padding 4}}
   [ui/grid {:item true
             :xs   4}
    [ui/input-label {:style {:padding    7
                             :text-align "right"
                             :width      "100%"}}
     (str label ":")]]
   [ui/grid {:item true
             :xs   8}
    control]])

(defn- map->str
  [value]
  (if-not (nil? value)
    (-> value
         (clj->js)
         (js/JSON.stringify nil 2))
    value))

(defn- str->map
  [value]
  (->> value
       (.parse js/JSON)
       (js->clj)))

(defn edit-field-modal
  [{:keys [open value on-save on-cancel]}]
  (let [scenes @(re-frame/subscribe [::subs/course-scenes])]
    (r/with-let [field-data (r/atom (merge value
                                           {:template (-> value :template map->str)}))]
                [ui/dialog
                 {:open       open
                  :full-width true
                  :max-width  "sm"}
                 [ui/dialog-title "Dataset Field"]
                 [ui/dialog-content
                  [field-row "Name" [ui/text-field
                                     {:value     (or (:name @field-data) "")
                                      :on-change #(swap! field-data assoc :name (.. % -target -value))
                                      :style     {:width "100%"}}]]
                  [field-row "Type" [ui/select
                                     {:value     (or (:type @field-data) "")
                                      :on-change #(swap! field-data assoc :type (.. % -target -value))
                                      :style     {:width "100%"}}
                                     (for [{:keys [value text]} item-field-types]
                                       ^{:key value}
                                       [ui/menu-item {:value value} text])]]
                  [field-row "Used in scenes" [ui/select
                                               {:multiple  true
                                                :value     (or (:scenes @field-data) [])
                                                :on-change #(swap! field-data assoc :scenes (js->clj (.. % -target -value)))
                                                :style     {:width "100%"}
                                                :MenuProps {:style {:max-height 400}}}
                                               (for [scene scenes]
                                                 ^{:key scene}
                                                 [ui/menu-item {:value scene} scene])]]
                  [field-row "Template (JSON)" [ui/text-field
                                                {:multiline   true
                                                 :rows        10
                                                 :value       (or (:template @field-data) "")
                                                 :on-change   #(swap! field-data assoc :template (.. % -target -value))
                                                 :style       {:width "100%"}
                                                 :input-props {:style {:font-family "monospace"
                                                                       :font-size   "10px"
                                                                       :line-height "12px"}}}]]]
                 [ui/dialog-actions
                  [ui/button
                   {:color    "primary"
                    :on-click #(on-cancel)}
                   "Cancel"]
                  [ui/button
                   {:color    "secondary"
                    :variant  "contained"
                    :on-click #(on-save value (merge @field-data
                                                     {:template (-> @field-data :template str->map)}))}
                   "Save"]]])))
