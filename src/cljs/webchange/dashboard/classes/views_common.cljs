(ns webchange.dashboard.classes.views-common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]))

(defn- class-form
  [props]
  [:form
   [ui/text-field
    {:label         "Class Name"
     :default-value (:name @props)
     :on-change     #(swap! props assoc :name (->> % .-target .-value))}]])

(defn class-modal
  [class-data {:keys [title modal-open handle-save handle-close]}]
  [ui/dialog
   {:open     modal-open
    :on-close #(handle-close)}
   [ui/dialog-title
    (case title
      :add "Add New Class"
      :edit "Edit Class"
      "")]
   [ui/dialog-content
    [class-form class-data]]
   [ui/dialog-actions
    [ui/button
     {:on-click handle-close}
     "Cancel"]
    [ui/button
     {:variant  "contained"
      :color    "primary"
      :on-click #(do (handle-save @class-data)
                     (handle-close))}
     "Save"]]])
