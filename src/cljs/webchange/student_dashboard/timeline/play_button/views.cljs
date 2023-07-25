(ns webchange.student-dashboard.timeline.play-button.views
  (:require
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn play-button
  []
  (let [glow? (r/atom false)
        blink? (r/atom false)]
    (r/create-class
      {:display-name        "play-button"
       :component-did-mount (fn []
                              (js/setTimeout #(reset! glow? true) 5000)
                              (js/setTimeout #(reset! blink? true) 10000))
       :reagent-render
                            (fn [{:keys [on-click]}]
                              [:button {:on-click on-click
                                        :class-name (get-class-name {"play-button"   true
                                                                     "timeline-item" true
                                                                     "glow"          @glow?
                                                                     "blink"         @blink?})}])})))
