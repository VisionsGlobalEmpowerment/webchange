(ns webchange.editor-v2.scene.translation-steps
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scene-metadata.translator :as translator-metadata]
    [webchange.ui.theme :refer [get-in-theme]]))

(def steps [:change-background
            :select-skin
            :fill-dialogs])

(defn get-step-state
  [step-name]
  (let [passed-steps (or (:data @(re-frame/subscribe [::translator-metadata/translation-steps])) [])
        current-step (->> steps
                          (filter (fn [step]
                                    (->> passed-steps (some #{step}) not)))
                          (first))]
    (cond
      (= step-name current-step) :current
      (some #{step-name} passed-steps) :passed
      :else :on-going)))

;; Events

(defn- set-step-complete
  [step-name]
  (re-frame/dispatch [::translator-metadata/add-translation-step step-name]))

(defn set-change-background-complete
  []
  (set-step-complete :change-background))

(defn set-select-skin-complete
  []
  (set-step-complete :select-skin))

;(defn set-fill-dialogs-complete
;  []
;  (set-step-complete :fill-dialogs))

;; Messages Views

(defn- get-styles
  []
  (let [icon-style {:color        (get-in-theme [:palette :text :primary])
                    :font-size    "14px"
                    :margin-right "8px"}]
    {:message         {:color (get-in-theme [:palette :text :primary])}
     :current-message {:color (get-in-theme [:palette :primary :main])}
     :message-wrapper {:display     "flex"
                       :align-items "center"}
     :complete-icon   icon-style
     :current-icon    (merge icon-style
                             {:color        (get-in-theme [:palette :primary :main])
                              :cursor       "pointer"
                              :font-size    "21px"
                              :margin-left  "-4px"
                              :margin-right "4px"})
     :on-going-icon   icon-style}))

(defn- step-icon
  [{:keys [state on-click]}]
  (let [styles (get-styles)]
    (case state
      :current [ui/tooltip {:title     "Click to finish the step manually"
                            :placement "top"}
                [ic/all-out {:style    (:current-icon styles)
                             :on-click on-click}]]
      :passed [ic/check-circle {:style (:complete-icon styles)}]
      [ic/panorama-fish-eye {:style (:on-going-icon styles)}])))

(defn- step-component
  [{:keys [step title]}]
  (let [step-state (get-step-state step)
        styles (get-styles)]
    [:div {:style (:message-wrapper styles)}
     [step-icon {:state    step-state
                 :on-click #(set-step-complete step)}]
     [ui/typography {:style (if (= step-state :current)
                              (:current-message styles)
                              (:message styles))}
      title]]))

(defn change-background
  []
  [step-component {:step  :change-background
                   :title "Change background of your activity:"}])

(defn select-skin
  []
  [step-component {:step  :select-skin
                   :title "Select skins on characters:"}])

(defn fill-dialogs
  []
  [step-component {:step  :fill-dialogs
                   :title "Fill out the dialogue cues:"}])
