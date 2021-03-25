(ns webchange.editor-v2.sandbox.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.sandbox.state :as state]
    [webchange.ui-framework.components.index :refer [button dialog icon-button text-input]]))

;(defn- select-lesson-set-form
;  [{:keys [name]}]
;  (let [lesson-set-data (-> @(re-frame/subscribe [::state/lesson-set-data])
;                            (get name))
;        current-dataset-id (get lesson-set-data :dataset-id "")
;        current-dataset-items (get lesson-set-data :item-ids [])
;
;        datasets @(re-frame/subscribe [::editor-subs/course-datasets])
;        dataset-items (->> @(re-frame/subscribe [::editor-subs/course-dataset-items])
;                           (map second)
;                           (filter (fn [{:keys [dataset-id]}] (= dataset-id current-dataset-id))))]
;    [ui/grid {:container true
;              :spacing   16}
;     [ui/grid {:item  true :xs 4
;               :style {:display     "flex"
;                       :align-items "flex-end"}}
;      [ui/typography {:variant "subtitle1"
;                      :style   {:padding "10px"}} name]]
;     [ui/grid {:item true :xs 3}
;      [ui/form-control {:full-width true}
;       [ui/input-label "Dataset"]
;       [ui/select {:value     current-dataset-id
;                   :variant   "outlined"
;                   :on-change #(re-frame/dispatch [::state/set-lesson-set-data name (.. % -target -value)])}
;        (for [{:keys [id name]} datasets]
;          ^{:key id}
;          [ui/menu-item {:value id} name])]]]
;     [ui/grid {:item true :xs 5}
;      [ui/form-control {:full-width true}
;       [ui/input-label "Items"]
;       [ui/select {:value     current-dataset-items
;                   :variant   "outlined"
;                   :on-change #(re-frame/dispatch [::state/set-lesson-set-data name current-dataset-id (js->clj (.. % -target -value))])
;                   :multiple  true}
;        (for [{:keys [id name]} dataset-items]
;          ^{:key id}
;          [ui/menu-item {:value id} name])]]]]))

;(defn- select-lesson-sets-form
;  []
;  (let [lesson-sets (->> @(re-frame/subscribe [::state/lesson-sets])
;                         (map keyword))]
;    [ui/grid {:container true}
;     (for [lesson-set-name lesson-sets]
;       ^{:key lesson-set-name}
;       [ui/grid {:item true :xs 12}
;        [select-lesson-set-form {:name lesson-set-name}]])]))

(defn share-form
  []
  (let [link @(re-frame/subscribe [::state/link])
        copy-link (fn []
                    (doto (js/document.getElementById "share-link-text-input")
                      (.focus)
                      (.select))
                    (js/document.execCommand "copy"))]
    [:div.share-form
     [:div.link-row
      [text-input {:id    "share-link-text-input"
                   :value link}]
      [icon-button {:icon     "link"
                    :on-click copy-link
                    :variant  "outlined"}
       "Copy Link"]]
     [button {:href       link
              :class-name "open-link"}
      "Open Link"]]))

(defn share-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog
       {:title    "Share"
        :on-close close}
       [share-form]])))
