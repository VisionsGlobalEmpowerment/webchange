(ns webchange.dashboard.students.students-list.views_list_filter
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.string :as s]
    [reagent.core :as r]))

(defn translate
  [path]
  (get-in {:class {:label     "Filter Classes"
                   :none-item "None"}}
          path))

(defn students-list-filter-presentation
  [{:keys [name class classes on-class-changed on-name-changed style]}]
  [ui/toolbar {:style style}
   [ui/grid
    {:container true
     :justify   "flex-end"}
    [ui/grid
     {:item true
      :xs   2}
     [ui/form-control
      {:full-width true}
      [ui/input-label (translate [:class :label])]
      [ui/select
       {:value     class
        :variant   "outlined"
        :on-change #(on-class-changed (->> % .-target .-value))}
       [ui/menu-item {:value nil} [:em (translate [:class :none-item])]]
       (for [{:keys [id name]} classes]
         ^{:key id}
         [ui/menu-item {:value id} name])]]]
    [ui/grid
     {:item true
      :xs   1}]
    [ui/grid
     {:item true
      :xs   3}
     [ui/form-control
      {:full-width true}
      [ui/text-field
       {:label      " "
        :value      name
        :on-change  #(on-name-changed (->> % .-target .-value))
        :InputProps {:start-adornment (r/as-element [ui/input-adornment {:position "start"} [ic/search]])
                     :end-adornment   (r/as-element [ui/input-adornment {:position "end"}
                                                     (if-not (s/blank? name)
                                                       [ui/icon-button {:on-click #(on-name-changed nil)} [ic/close]]
                                                       [:div])])}}]]]]])

(defn students-list-filter
  [{:keys [classes style]} filter]
  [students-list-filter-presentation
   {:name             (or (:name @filter) "")
    :class            (or (:class-id @filter) "")
    :classes          classes
    :on-class-changed #(swap! filter assoc :class-id %)
    :on-name-changed  #(swap! filter assoc :name (if (s/blank? %) nil %))
    :style            style}])
