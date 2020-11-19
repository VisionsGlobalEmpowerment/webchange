(ns webchange.editor-v2.diagram-utils.modes.phrases.widget-data
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :refer [join]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :refer [validate-dialog-action]]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.events :as ee]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram-utils.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [object-node?
                                                               phrase-node?
                                                               trigger-node?]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.utils :refer [str->caption]]
    [webchange.editor-v2.dialog.state.window :as dialog-window]
    [webchange.editor-v2.translator.state.window :as translator-window]))

(defn get-node-color
  [node-data]
  (cond
    (phrase-node? node-data) (get-in colors [:action :phrase])
    (object-node? node-data) (get-in colors [:object :default])
    (trigger-node? node-data) (get-in colors [:global-object :default])
    :else (:default colors)))

(defn- get-styles
  []
  {:header-description {:margin             "0"
                        :padding            "5px"
                        :text-align         "center"
                        :max-width          "200px"
                        :overflow           "hidden"
                        :text-overflow      "ellipsis"
                        :-webkit-line-clamp 2
                        :-webkit-box-orient "vertical"
                        :display            "-webkit-box"
                        :height             "50px"}
   :dialog-node        {:width            "180px"
                        :height           "64px"
                        :margin-top       "-12px"
                        :background-color "#6BC784"
                        :cursor           "pointer"}
   :track-label        {:height           "40px"
                        :border-radius    "0"
                        :display          "flex"
                        :flex-direction   "column"
                        :justify-content  "center"
                        :align-items      "center"
                        :padding          "3px"
                        :background-color "#156874"
                        :white-space      "nowrap"}
   :prompt             {:width         "180px"
                        :top           "-20px"
                        :border        "none"
                        :border-radius "20px"
                        :cursor        "default"}
   :prompt-text        {:margin          "5px"
                        :height          "66px"
                        :overflow        "auto"
                        :text-align      "center"
                        :font-size       "18px"
                        :font-style      "italic"
                        :color           "#404040"
                        :display         "flex"
                        :align-items     "center"
                        :justify-content "center"
                        :padding-bottom  "5px"}})

(defn- phrase-header
  [node-data]
  (let [phrase (-> node-data (get-in [:data :phrase]))
        description (or (get-in node-data [:data :phrase-description-translated])
                        (get-in node-data [:data :phrase-description]))
        styles (get-styles)]
    (if-not (nil? description)
      [:h3 {:style (:header-description styles)}
       description]
      [:div
       [:p {:style (:header-description styles)}
        "! NO DESCRIPTION !"]
       [:p {:style (:header-description styles)}
        phrase]])))

(defn- not-phrase-header
  [node-data]
  (let [styles (get-styles)]
    [:h3 {:style (:header-description styles)}
     (str->caption (:name node-data))]))

(defn- header
  [node-data]
  (if (phrase-node? node-data)
    [phrase-header node-data]
    [not-phrase-header node-data]))

(defn- get-dialog-texts
  [node-data]
  (->> (:data node-data)
       (map (fn [{:keys [type phrase-text] :as data}]
              (case type
                "action" "**Concept Text**"
                "sequence-data" (get-dialog-texts data)
                "animation-sequence" phrase-text
                nil)))
       (flatten)
       (remove nil?)))

(defn- dialog-wrapper-tooltip
  [{:keys [node open?]}]
  (let [this (r/current-component)
        dialog-text (get-dialog-texts (:data node))
        translator-open? @(re-frame/subscribe [::translator-window/modal-state])
        dialog-open? @(re-frame/subscribe [::dialog-window/modal-state])]
    (into [ui/tooltip {:open      (and open?
                                       (not (or translator-open? dialog-open?)))
                       :placement "left"
                       :title     (r/as-element [:ul {:style {:padding-left 0
                                                              :list-style   "none"
                                                              :font-size    "14px"}}
                                                 (for [[idx text] (map-indexed vector dialog-text)]
                                                   ^{:key idx}
                                                   [:li {:style {:margin-bottom "8px"}} text])])}]
          (r/children this))))

(defn- dialog-wrapper
  [{:keys [node-data this]}]
  (let [valid-node? (validate-dialog-action (:data node-data))
        current-node? (->> @(re-frame/subscribe [::translator-form.actions/current-dialog-action-data])
                           (= (:data node-data)))
        styles (get-styles)]
    [dialog-wrapper-tooltip {:open? current-node?
                             :node  node-data}
     [:div {:on-click        (fn []
                               (re-frame/dispatch [::translator-form.actions/set-current-dialog-action (if current-node? nil node-data)]))
            :on-double-click (fn []
                               (if (= "dialog" (get-in node-data [:data :editor-type]))
                                 (re-frame/dispatch [::ee/show-dialog-translator-form node-data])
                                 (re-frame/dispatch [::ee/show-translator-form node-data])))
            :style           (merge custom-wrapper/node-style
                                    (:dialog-node styles))}
      (when-not valid-node?
        [warning-icon {:styles {:main {:position "absolute"
                                       :bottom   "2px"}}}])
      (into [:div] (r/children this))]]))

(defn- track-wrapper
  [{:keys [this]}]
  (let [styles (get-styles)]
    (into [:div {:style (merge custom-wrapper/node-style
                               (:track-label styles))}]
          (r/children this))))

(defn- prompt-wrapper
  [{:keys [node-data]}]
  (let [styles (get-styles)]
    [:div {:style (merge custom-wrapper/node-style
                         (:prompt styles))}
     [:style
      "body ::-webkit-scrollbar {-webkit-appearance: none;  width: 6px;  height: 10px;}"]
     [:div {:style (:prompt-text styles)}
      (:text node-data)]]))

(defn wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)]
    (case (:type node-data)
      "dialog" [dialog-wrapper {:node-data node-data
                                :this      this}]
      "track" [track-wrapper {:this this}]
      "prompt" [prompt-wrapper {:node-data node-data}]
      [:div "Unhandled type"])))

(defn get-widget-data
  []
  {:header                header
   :wrapper               wrapper
   :get-node-custom-color get-node-color})
