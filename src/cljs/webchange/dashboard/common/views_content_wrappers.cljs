(ns webchange.dashboard.common.views-content-wrappers
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(def padding 20)

(defn content-page
  [{:keys [title]} & children]
  [ui/grid
   {:container true
    :spacing   24
    :style     {:padding padding}}
   [ui/grid
    {:item true
     :xs 12}
    [ui/typography
     {:variant "h4"}
     title]]
   (for [child children]
     ^{:key child}
     [ui/grid
      {:item true
       :xs 12}
      [ui/paper
       {:style {:padding padding}}
       child]])])

(defn content-page-section
  [{:keys [title]} & children]
  [ui/grid
   {:container true
    :spacing 16}
   (when title
     [ui/grid
      {:item true
       :xs 12}
      [ui/typography
       {:variant "h6"}
       title]])
   (for [child children]
     ^{:key child}
     [ui/grid
      {:item true
       :xs 12}
      child])])
