(ns webchange.editor-v2.layout.flipbook.page-form.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.events :as editor]
    [webchange.editor-v2.layout.flipbook.page-form.state :as page-form]
    [webchange.editor-v2.layout.flipbook.page-text.views :refer [page-text-form]]))

(defn- remove-button
  [{:keys [stage page-side]}]
  (let [handle-click #(re-frame/dispatch [::page-form/remove-page {:stage     stage
                                                                   :page-side page-side}])]
    [ui/button {:on-click handle-click
                :style    {:margin-top "16px"
                           :position   "absolute"
                           :right      0}}
     "Remove"]))

(defn- reorder-control
  [{:keys [stage page-side]}]
  (let [handle-click #(re-frame/dispatch [::page-form/move-page {:stage     stage
                                                                 :page-side page-side
                                                                 :target    %}])]
    [:div
     [ui/tooltip {:title "Move page backward"}
      [ui/icon-button {:on-click #(handle-click :backward)
                       :style    {:padding "8px"}}
       [ic/arrow-back {:style {:font-size "16px"}}]]]
     [ui/tooltip {:title "Move page forward"}
      [ui/icon-button {:on-click #(handle-click :forward)
                       :style    {:padding     "8px"
                                  :margin-left "8px"}}
       [ic/arrow-forward {:style {:font-size "16px"}}]]]]))

(defn- open-action-dialog-button
  [{:keys [action-name]}]
  (let [window-params {:components {:description  {:hide? true}
                                    :node-options {:hide? true}
                                    :target       {:hide? true}}}
        handle-click (fn []
                       (re-frame/dispatch [::editor/show-translator-form-by-id action-name window-params]))]
    [ui/button {:on-click handle-click
                :style    {:margin-left "8px"}}
     "Edit Action"]))

(defn page-form
  [{:keys [stage page-side]}]
  (let [{:keys [removable? text action] :as page-data} @(re-frame/subscribe [::page-form/page-text-data stage page-side])]
    [:div {:style {:position "relative"}}
     (when (some? page-data)
       [ui/typography {:align   "center"
                       :variant "h6"
                       :style   {:margin-bottom "16px"}}
        (-> (clojure.core/name page-side) (clojure.string/capitalize) (str " Page"))])
     (when (some? text)
       ^{:key (str stage "-" page-side)}
       [page-text-form page-data])
     [:div {:style {:display    "flex"
                    :margin-top "16px"
                    :position   "absolute"
                    :left       0}}
      (when (some? page-data)
        [reorder-control {:stage     stage
                          :page-side page-side}])
      (when (and (not (some? text))
                 (some? action))
        [open-action-dialog-button {:action-name (keyword action)}])]
     (when removable?
       [remove-button {:stage     stage
                       :page-side page-side}])]))
