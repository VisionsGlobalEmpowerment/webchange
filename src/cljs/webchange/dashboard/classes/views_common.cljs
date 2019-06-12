(ns webchange.dashboard.classes.views-common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

(defn- class-form
  [props]
  [:form
   [ui/text-field {:hint-text     "Class Name"
                   :default-value (:name @props)
                   :on-change     #(swap! props assoc :name (->> % .-target .-value))}]])

(defn class-modal
  [class-data {:keys [title modal-open handle-save handle-close]}]
  [ui/dialog {:title            (case title
                                  :add "Add New Class"
                                  :edit "Edit Class"
                                  "")
              :actions          [(r/as-element [ui/button {:default  true
                                                                :on-click handle-close} "Cancel"])
                                 (r/as-element [ui/button {:primary  true
                                                                  :on-click #(do (handle-save @class-data)
                                                                                 (handle-close))} "Save"])]
              :modal            false
              :content-style    {:width "370px"}
              :open             modal-open
              :on-request-close #(handle-close)}
   [class-form class-data]])