(ns webchange.lesson-builder.tools.template-options.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.state :as state]
    [webchange.lesson-builder.tools.template-options.first-words-book-spreads.views :as first-words-book-spreads]
    [webchange.lesson-builder.widgets.select-image.views :refer [choose-image-overlay]]
    [webchange.lesson-builder.widgets.select-image.state :as choose-image-state]
    [webchange.ui.index :as ui]))

(defn- note-field
  [{:keys [text]}]
  [:div.note
   [ui/icon {:icon "info"}]
   text])

(declare option-field)

(defn- group-field
  [{:keys [label children]}]
  [:div.group
   [:h3.group-label label]
   (for [child children]
     [option-field child])])

(defn- string-field
  [{:keys [label key placeholder]}]
  (let [default-value @(re-frame/subscribe [::state/saved-field-value key])]
    [ui/input {:label label
               :placeholder placeholder
               :default-value default-value
               :required? true
               :on-change (state/handle-change-field key)}]))

(defn- option-field
  [{:keys [type] :as props}]
  (case (keyword type)
    :note [note-field props]
    :group [group-field props]
    :string [string-field props]
    :first-words-book-spreads [first-words-book-spreads/field props]))

(defn- options-panel
  []
  (let [options @(re-frame/subscribe [::state/options])]
    [:<>
     (for [option options]
       [option-field option])]))

(defn template-options
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [overlay @(re-frame/subscribe [::choose-image-state/show-choose-image?])]
      (if overlay
        [:div.widget--template-options
         [:h1 "Choose Image"]
         [choose-image-overlay]]
        [:div.widget--template-options
         [:h1 "Template Options"]
         [options-panel]
         [ui/button {:class-name "template-options-apply"
                     :on-click #(re-frame/dispatch [::state/apply])}
          "Apply"]]))))
