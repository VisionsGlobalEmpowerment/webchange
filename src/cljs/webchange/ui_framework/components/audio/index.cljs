(ns webchange.ui-framework.components.audio.index
  (:require
    [reagent.core :as r]
    [webchange.audio-utils.player :as utils]
    [webchange.ui-framework.components.icon-button.index :as icon-button]))



(defn component
  []
  (let [audio (atom nil)
        state (r/atom "stop")
        handle-play (fn []
                      (reset! state "play")
                      (when (some? @audio)
                        (utils/play @audio)))
        handle-stop (fn []
                      (reset! state "stop")
                      (when (some? @audio)
                        (utils/stop @audio)))
        handle-click (fn []
                       (if (= @state "stop")
                         (handle-play)
                         (handle-stop)))]
    (r/create-class
      {:display-name "wc-audio"
       :component-did-mount
                     (fn [this]
                       (let [{:keys [url]} (r/props this)]
                         (when (some? url)
                           (reset! audio (js/Audio. url)))))
       :component-did-update
                     (fn [this [_ old-props]]
                       (let [new-props (r/props this)]
                         (when-not (= (:url old-props) (:url new-props))
                           (handle-stop)
                           (reset! audio (js/Audio. (:url new-props))))))
       :component-will-unmount
                     (fn []
                       (handle-stop))
       :reagent-render
                     (fn []
                       [:div {:class-name "wc-audio"}
                        [icon-button/component {:icon     (if (= @state "stop") "play" "stop")
                                                :on-click handle-click}]])})))