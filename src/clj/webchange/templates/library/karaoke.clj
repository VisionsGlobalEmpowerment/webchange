(ns webchange.templates.library.karaoke
  (:require
    [webchange.templates.core :as core]))

(def m {:id          35
        :name        "Karaoke"
        :tags        ["listening comprehension" "rhyming" "express ideas verbally"]
        :description "Karaoke"
        :options     {:video       {:label "Video File"
                                    :type  "video"}
                      :video-range {:label       "Video Range"
                                    :type        "video-range"
                                    :video-param "video"}}})

(def t {:assets        []
        :objects       {}
        :scene-objects []
        :actions       {:start-scene     {:type "sequence" :data ["start-activity"]}
                        :stop-scene      {:type "sequence" :data ["stop-activity"]}

                        :start-activity  {:type "start-activity"}
                        :stop-activity   {:type "stop-activity"}
                        :finish-activity {:type "finish-activity"}}
        :triggers      {:stop  {:on "back" :action "stop-scene"}
                        :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
