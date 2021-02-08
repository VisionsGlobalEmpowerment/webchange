(ns webchange.editor-v2.layout.flipbook.views-skeleton)

(defn- get-styles
  []
  (let [padding 16]
    {:main-container        {:height         "100%"
                             :display        "flex"
                             :flex-direction "column"}

     :top-panel             {:display         "flex"
                             :flex-grow       0
                             :flex-shrink     0
                             :height          "350px"
                             :justify-content "space-between"
                             :padding-bottom  (* padding 2)
                             :width           "100%"}
     :top-panel--left-side  {:flex-grow     1
                             :padding-right padding
                             :max-width     "300px"}
     :top-panel--right-side {:display       "flex"
                             :flex-grow     0
                             :padding-right padding}

     :middle-panel          {:display        "flex"
                             :flex-grow      1
                             :flex-shrink    1
                             :padding-bottom (* padding 2)
                             :width          "100%"}

     :diagram               {:display   "flex"
                             :flex-grow 1
                             :height    "100%"}}))

(defn- top-panel
  [{:keys [top-left-component top-right-component]}]
  (let [styles (get-styles)]
    [:div {:style (:top-panel styles)}
     [:div {:style (:top-panel--left-side styles)}
      top-left-component]
     [:div {:style (:top-panel--right-side styles)}
      top-right-component]]))

(defn- middle-panel
  [{:keys [middle-component]}]
  (let [styles (get-styles)]
    [:div {:style (:middle-panel styles)}
     middle-component]))

(defn- diagram
  [{:keys [bottom-component]}]
  (let [styles (get-styles)]
    [:div {:style (:diagram styles)}
     bottom-component]))

(defn skeleton
  [props]
  (let [styles (get-styles)]
    [:div {:style (:main-container styles)}
     [top-panel props]
     [middle-panel props]
     [diagram props]]))
