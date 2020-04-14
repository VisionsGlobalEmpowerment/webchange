(ns webchange.editor-v2.translator.translator-form.audio-assets.views-audio-wave
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.form-elements.wavesurfer.wave-form :refer [audio-wave-form]]
    [webchange.editor-v2.layout.confirm.views :refer [with-confirmation]]
    [webchange.editor-v2.translator.translator-form.audio-assets.events :as assets-events]))

(defn- capitalize-words
  [s]
  (->> (s/split (str s) #"\b")
       (map s/capitalize)
       s/join))

(defn- target->caption
  [target]
  (-> target
      (s/replace "-" " ")
      capitalize-words))

(defn- get-styles
  []
  {:block-wrapper          {:margin-bottom "8px"}
   :block-wrapper-selected {:border        "solid 1px #00c0ff"
                            :margin-bottom "8px"}
   :block-header           {:display         "flex"
                            :justify-content "space-between"
                            :padding         "0 0 8px 0"}

   :menu-button            {:padding "8px"}
   :menu-item-icon         {:font-size "18px"}

   :info-form              {:display         "flex"
                            :justify-content "space-between"
                            :width           "100%"}
   :form-button            {:margin-left "8px"
                            :padding     "10px"}
   :alias-form             {:width "170px"}
   :target-form            {:width        "130px"
                            :margin-right "16px"}
   :target-form-label      {:top "-16px"}
   :target-form-input      {:margin 0}})

(defn- audio-info
  [{:keys [alias target]}]
  [:div
   (when-not (nil? target)
     [ui/chip {:label target
               :style {:margin  "0 10px 0 0"
                       :padding "0"}}])
   [ui/typography {:variant "subtitle2"
                   :color   "default"
                   :style   {:display "inline-block"}} alias]])

(defn- audio-info-form
  [{:keys [alias target available-targets on-save on-cancel]}]
  (r/with-let [current-data (r/atom {:alias  alias
                                     :target target})]
              (let [targets (map name available-targets)
                    styles (get-styles)]
                [:div {:style (:info-form styles)}
                 [:div
                  [ui/form-control {:style (:target-form styles)}
                   [ui/input-label {:html-for "target"
                                    :style    (:target-form-label styles)}
                    "Target"]
                   [ui/select {:value       (or (:target @current-data) "")
                               :on-change   #(swap! current-data assoc :target (-> % .-target .-value))
                               :input-props {:id "target"}
                               :style       (:target-form-input styles)}
                    (for [target targets]
                      ^{:key target}
                      [ui/menu-item {:value target} (target->caption target)])]]
                  [ui/form-control {:style (:alias-form styles)}
                   [ui/text-field {:label     "Alias"
                                   :value     (or (:alias @current-data) "")
                                   :on-change #(swap! current-data assoc :alias (-> % .-target .-value))
                                   :variant   "outlined"}]]]
                 [:div
                  [ui/tooltip {:title "Cancel" :placement "top"}
                   [ui/icon-button
                    {:style    (:form-button styles)
                     :on-click on-cancel}
                    [ic/clear {:style (:menu-item-icon styles)}]]]
                  [ui/tooltip {:title "Save" :placement "top"}
                   [ui/icon-button
                    {:style    (:form-button styles)
                     :on-click #(on-save @current-data)}
                    [ic/done {:style (:menu-item-icon styles)}]]]]])))



(defn- audio-menu
  [{:keys [on-edit on-delete]}]
  (r/with-let [menu-anchor (r/atom nil)]
              (let [handle-edit #(do (reset! menu-anchor nil) (on-edit))
                    handle-delete #(do (reset! menu-anchor nil) (on-delete))
                    handle-cancel #(reset! menu-anchor nil)
                    styles (get-styles)]
                [:div
                 [ui/icon-button
                  {:style    (:menu-button styles)
                   :on-click #(reset! menu-anchor (.-currentTarget %))}
                  [ic/more-horiz {:style (:menu-item-icon styles)}]]
                 [ui/menu
                  {:anchor-el @menu-anchor
                   :open      (boolean @menu-anchor)
                   :on-close  #(reset! menu-anchor nil)}
                  [ui/menu-item {:on-click handle-edit}
                   [ui/list-item-icon
                    [ic/edit {:style (:menu-item-icon styles)}]]
                   "Edit Data"]
                  [with-confirmation {:message    "Remove audio asset from scene?"
                                      :on-confirm handle-delete
                                      :on-cancel  handle-cancel}
                   [ui/menu-item
                    [ui/list-item-icon
                     [ic/delete {:style (:menu-item-icon styles)}]]
                    "Delete"]]]])))

(defn- header
  [{:keys [alias target available-targets selected? on-change-data on-delete]}]
  (r/with-let [edit-state? (r/atom false)]
              (let [styles (get-styles)
                    handle-edit #(reset! edit-state? true)
                    handle-save #(do (reset! edit-state? false)
                                     (on-change-data %))
                    handle-cancel #(reset! edit-state? false)
                    handle-delete on-delete]
                [:div {:style (:block-header styles)}
                 (if @edit-state?
                   [audio-info-form {:alias             alias
                                     :target            target
                                     :available-targets available-targets
                                     :on-save           handle-save
                                     :on-cancel         handle-cancel}]
                   [audio-info {:alias             alias
                                :target            target
                                :available-targets available-targets
                                :on-save           handle-save}])

                 (when (and selected?
                            (not @edit-state?))
                   [audio-menu {:on-edit   handle-edit
                                :on-delete handle-delete}])])))

(defn audio-wave
  [{:keys [key alias start duration selected? target]}
   {:keys [current-key targets on-change-region]}]
  (r/with-let [handle-change-data (fn [data] (re-frame/dispatch [::assets-events/patch-asset key data]))
               handle-change-region (fn [region] (on-change-region key region))
               handle-delete (fn [] (re-frame/dispatch [::assets-events/delete-asset key]))
               on-select (fn []
                           (reset! current-key key)
                           (on-change-region key))
               audio-data {:key   key
                           :start (or start 0)
                           :end   (+ start duration)}
               styles (get-styles)]
              (let [form-params {:height         64
                                 :on-change      handle-change-region
                                 :show-controls? selected?}]
                [ui/card {:on-click on-select
                          :style    (if selected?
                                      (:block-wrapper-selected styles)
                                      (:block-wrapper styles))}
                 [ui/card-content
                  [header {:alias             (or alias key)
                           :target            target
                           :available-targets targets
                           :selected?         selected?
                           :on-change-data    handle-change-data
                           :on-delete         handle-delete}]
                  [audio-wave-form audio-data form-params]]])))
