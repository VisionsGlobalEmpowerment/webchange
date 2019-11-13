(ns webchange.editor.common.actions.action-forms.animation-sequence
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [soda-ash.core
     :refer [FormDropdown FormGroup Item ItemContent ItemGroup]
     :rename {FormDropdown form-dropdown
              FormGroup    form-group
              Item         item
              ItemContent  item-content
              ItemGroup    item-group}]
    [sodium.core :as na
     :refer [button divider dropdown-list form-input header icon]
     :rename {dropdown-list get-options-list}]
    [webchange.editor.events :as events]
    [webchange.editor.common.actions.events :as actions.events]
    [webchange.editor.form-elements :as f]
    [webchange.editor.form-elements.wavesurfer.wavesurfer :as ws]))

(defn animation-sequence-item
  [item on-change on-remove]
  (let [props (r/atom nil)]
    (fn [{:keys [start end anim] :as item} on-change on-remove]
      [item {}
       (if @props
         [item-content {}
          [form-input {:label         "start"
                       :default-value start
                       :on-change     #(swap! props assoc :start (-> %2 .-value js/parseFloat))
                       :inline?       true}]
          [form-input {:label         "end"
                       :default-value end
                       :on-change     #(swap! props assoc :end (-> %2 .-value js/parseFloat))
                       :inline?       true}]
          [form-input {:label         "anim"
                       :default-value anim
                       :on-change     #(swap! props assoc :anim (-> %2 .-value))
                       :inline?       true}]

          [button {:basic?   true
                   :content  "save"
                   :on-click #(do (on-change @props)
                                  (reset! props nil))}]]
         [item-content {}
          [:a (str "start: " start " end: " end " anim: " anim)]
          [:div {:style {:float "right"}}
           [icon {:name     "edit"
                  :link?    true
                  :on-click #(reset! props item)}]
           [icon {:name     "remove"
                  :link?    true
                  :on-click on-remove}]]])
       ])))

(defn animation-sequence-items
  [props]
  (let [data (:data @props)]
    [:div
     [item-group {}
      [item {}
       [item-content {}
        [header {:as      "h4"
                 :floated "left"
                 :content "Items"}]
        [:div {:style {:float "right"}}
         [icon {:name     "add"
                :link?    true
                :on-click #(swap! props update-in [:data] conj {})}]]]]

      (for [[idx item] (map-indexed (fn [idx itm] [idx itm]) data)]
        ^{:key (str (:start item))}
        [animation-sequence-item
         item
         (fn [item] (swap! props assoc-in [:data idx] item))
         (fn [] (swap! props update-in [:data] #(vec (concat (subvec % 0 idx) (subvec % (inc idx))))))])]]))

(defn animation-object-names [objects]
  (->> objects
       (map second)
       (map (fn [object] (or (:scene-name object) (:name object))))
       (remove nil?)))

(defn animation-sequence-panel
  [props {:keys [scene-id
                 scene-objects
                 show-upload-asset-form]}]
  (let [loading (re-frame/subscribe [:loading])
        target-options (get-options-list (animation-object-names scene-objects) identity identity)
        audio (:audio @props)
        data (:data @props)
        duration (:duration @props)
        offset (:offset @props)
        start (:start @props)
        target (:target @props)
        track (:track @props)]
    [:div
     [form-dropdown {:label         "Target"
                     :placeholder   "Target"
                     :search        true
                     :selection     true
                     :inline        true
                     :options       target-options
                     :default-value target
                     :on-change     #(swap! props assoc :target (.-value %2))}]
     [form-input {:label         "track"
                  :default-value track
                  :on-change     #(swap! props assoc :track (-> %2 .-value))
                  :inline?       true}]
     [form-input {:label     "offset"
                  :value     offset
                  :on-change #(swap! props assoc :offset (-> %2 .-value))
                  :inline?   true}]
     [divider {}]
     [form-group {}
      [f/audio-asset-dropdown props :audio scene-id]
      [button {:basic?   true
               :content  "Upload new"
               :on-click show-upload-asset-form}]]
     [form-input {:label     "start"
                  :value     start
                  :on-change #(swap! props assoc :start (-> %2 .-value))
                  :inline?   true}]
     [form-input {:label     "duration"
                  :value     duration
                  :on-change #(swap! props assoc :duration (-> %2 .-value))
                  :inline?   true}]
     [ws/animation-sequence-waveform-modal
      {:key           audio
       :start         start
       :end           (+ start duration)
       :sequence-data data}
      (fn [{:keys [start duration regions]}]
        (swap! props assoc :offset start)
        (swap! props assoc :start start)
        (swap! props assoc :duration duration)
        (swap! props assoc :data (->> regions
                                      (map #(assoc % :anim "talk"))
                                      vec)))]

     [na/button {:content  "LipSync"
                 :loading? (:get-talk-animation @loading)
                 :on-click #(do (re-frame/dispatch [::actions.events/store-selected-action @props])
                                (re-frame/dispatch [::events/detect-lip-sync @props]))}]
     [divider {}]
     [animation-sequence-items props]
     [divider {}]]))