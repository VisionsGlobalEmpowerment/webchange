(ns webchange.lesson-builder.tools.template-options.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.state :as state]
    [webchange.lesson-builder.tools.template-options.first-words-book-spreads.views :as first-words-book-spreads]
    [webchange.lesson-builder.tools.template-options.slide-riddle-rounds.views :as slide-riddle-rounds]
    [webchange.lesson-builder.tools.template-options.sandbox-digging-rounds.views :as sandbox-digging-rounds]
    [webchange.lesson-builder.tools.template-options.onset-rime-rounds.views :as onset-rime-rounds]
    [webchange.lesson-builder.tools.template-options.select-book.views :as select-book]
    [webchange.lesson-builder.tools.template-options.rhyming-sides.views :as rhyming-sides]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image choose-image-overlay]]
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

(defn- image-field
  [{:keys [label key placeholder]}]
  (let [value @(re-frame/subscribe [::state/field-value key])]
    [select-image {:label label
                   :value (:src value)
                   :on-change #(re-frame/dispatch [::state/set-field key {:src (:url %)}])}]))

(defn- letter-lookup-field
  [{:keys [key label placeholder]}]
  (let [value @(re-frame/subscribe [::state/field-value key])
        letter-options @(re-frame/subscribe [::state/letter-options])]
    [ui/select {:label label
                :value value
                :required? true
                :options letter-options
                :on-change (state/handle-change-field key)}]))

(defn- lookup-field
  [{:keys [key label options placeholder]}]
  (let [value @(re-frame/subscribe [::state/field-value key])]
    [:div.lookup-field
     [ui/input-label
      label]
     [ui/select {:value value
                 :required? true
                 :options options
                 :placeholder placeholder
                 :on-change (state/handle-change-field key)}]]))

(defn- update-template-field
  [{:keys [label]}]
  [ui/button {:class-name "update-template"
              :on-click #(re-frame/dispatch [::state/update-template])}
   label])

(defn- option-field
  [{:keys [type] :as props}]
  (case (keyword type)
    :note [note-field props]
    :group [group-field props]
    :string [string-field props]
    :update-template [update-template-field props]
    :letter-lookup [letter-lookup-field props]
    :image [image-field props]
    :lookup [lookup-field props]
    :select-book [select-book/field props]
    :first-words-book-spreads [first-words-book-spreads/field props]
    :slide-riddle-rounds [slide-riddle-rounds/field props]
    :sandbox-digging-rounds [sandbox-digging-rounds/field props]
    :rhyming-sides [rhyming-sides/field props]
    :onset-rime-rounds [onset-rime-rounds/field props]))

(defn- options-panel
  []
  (let [options @(re-frame/subscribe [::state/options])]
    [:<>
     (for [option options]
       [option-field option])]))

(def overlay-components {:choose-image choose-image-overlay
                         :rhyming-side rhyming-sides/overlay})

(defn- overlays-widget
  []
  (let [opened-overlays @(re-frame/subscribe [::state/opened-overlays])]
    [:<>
     (for [{:keys [key label hidden]} opened-overlays]
       ^{:key key}
       (let [component (get overlay-components key)]
         [:div {:class-name (ui/get-class-name {"widget--template-options" true
                                                "widget--template-options-hidden" hidden})}
          [:h1 label]
          [component]]))]))

(defn- template-options-widget
  []
  (let [overlays-opened? (-> @(re-frame/subscribe [::state/opened-overlays]) seq)]
    [:div {:class-name (ui/get-class-name {"widget--template-options" true
                                           "widget--template-options-hidden" overlays-opened?})}
     [:h1 "Template Options"]
     [options-panel]
     [ui/button {:class-name "template-options-apply"
                 :on-click #(re-frame/dispatch [::state/apply])}
      "Apply"]]))

(defn template-options
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [:<>
     [template-options-widget]
     [overlays-widget]]))
