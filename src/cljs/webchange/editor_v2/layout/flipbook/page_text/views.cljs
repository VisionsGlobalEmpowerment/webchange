(ns webchange.editor-v2.layout.flipbook.page-text.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.editor-v2.layout.flipbook.page-text.views-edit-animation :refer [edit-animation-button]]
    [webchange.editor-v2.layout.flipbook.page-text.views-font-size :refer [font-size-control]]
    [webchange.editor-v2.layout.flipbook.page-text.views-save-button :refer [save-button]]
    [webchange.editor-v2.layout.flipbook.page-text.views-text-control :refer [text-control]]))

(defn- get-styles
  []
  {:toolbar {:display         "flex"
             :align-items     "center"
             :justify-content "space-between"}})

(defn page-text-form
  [{:keys [action object phrase-action-path text]}]
  (r/with-let [id (-> (random-uuid) str)
               _ (re-frame/dispatch [::state/init id {:data               {:text      (get-in text [:data :text])
                                                                           :font-size (get-in text [:data :font-size])}
                                                      :text-object-name   (get-in text [:name])
                                                      :dialog-action-name (keyword action)
                                                      :phrase-action-path phrase-action-path}])
               styles (get-styles)]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item  true :xs 12
               :style (:toolbar styles)}
      [font-size-control {:id id}]
      [edit-animation-button {:id id}]
      [save-button {:id id}]]
     [ui/grid {:item true :xs 12}
      [text-control {:id id}]]]))
