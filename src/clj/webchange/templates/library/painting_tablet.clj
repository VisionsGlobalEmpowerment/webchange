(ns webchange.templates.library.painting-tablet
  (:require
    [webchange.templates.core :as core]))

(def m {:id          12
        :name        "Painting tablet"
        :tags        ["Independent Practice"]
        :description "Some description of painting tablet"})

(def t {:assets
                       [{:url "/raw/img/library/painting-tablet/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/brush.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/pencil.png", :size 10, :type "image"}
                        {:url "/raw/img/library/painting-tablet/eraser.png", :size 10, :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/library/painting-tablet/background.jpg"},
                        :brush
                                    {:type    "image",
                                     :x       -280,
                                     :y       410,
                                     :width   540,
                                     :height  140,
                                     :actions {:click {:id "set-current-tool", :on "click", :type "action", :params {:tool "brush"}}},
                                     :src     "/raw/img/library/painting-tablet/brush.png",
                                     :states  {:active {:x -180}, :inactive {:x -280}}},
                        :colors-palette
                                    {:type       "colors-palette",
                                     :x          400
                                     :height     150
                                     :scene-name "colors-palette",
                                     :actions    {:change {:id "set-current-color", :on "change", :type "action", :pick-event-param "color"}},
                                     :colors     ["#4479bb" "#92bd4a" "#ed91aa" "#fdc531" "#010101"]},
                        :eraser
                                    {:type    "image",
                                     :x       -118,
                                     :y       794,
                                     :width   380,
                                     :height  175,
                                     :actions {:click {:id "set-current-tool", :on "click", :type "action", :params {:tool "eraser"}}},
                                     :src     "/raw/img/library/painting-tablet/eraser.png",
                                     :states  {:active {:x -18}, :inactive {:x -118}}},
                        :felt-tip
                                    {:type    "image",
                                     :x       -316,
                                     :y       213,
                                     :width   590,
                                     :height  150,
                                     :actions {:click {:id "set-current-tool", :on "click", :type "action", :params {:tool "felt-tip"}}},
                                     :src     "/raw/img/library/painting-tablet/felt-tip.png",
                                     :states  {:active {:x -216}, :inactive {:x -316}}},
                        :painting-area
                                    {:type       "painting-area",
                                     :x          0,
                                     :y          0,
                                     :width      1920,
                                     :height     1080,
                                     :color      "#4479bb"
                                     :scene-name "painting-area",
                                     :var-name   "painting-tablet-image"},
                        :pencil
                                    {:type    "image",
                                     :x       -283,
                                     :y       602,
                                     :width   550,
                                     :height  135,
                                     :actions {:click {:id "set-current-tool", :on "click", :type "action", :params {:tool "pencil"}}},
                                     :src     "/raw/img/library/painting-tablet/pencil.png",
                                     :states  {:active {:x -183}, :inactive {:x -283}}}},
        :scene-objects [["background"] ["painting-area" "felt-tip" "brush" "pencil" "eraser" "colors-palette"]],
        :actions
                       {:set-current-color
                                         {:type "state", :target "painting-area", :from-params [{:param-property "color", :action-property "value"}]},
                        :set-current-tool
                                         {:type "sequence-data",
                                          :data
                                                [{:type "state", :target "painting-area", :from-params [{:param-property "tool", :action-property "value"}]}
                                                 {:id "inactive", :type "state", :from-var [{:var-name "current-tool", :action-property "target"}]}
                                                 {:id "active", :type "state", :from-params [{:param-property "tool", :action-property "target"}]}
                                                 {:type        "set-variable",
                                                  :var-name    "current-tool",
                                                  :from-params [{:param-property "tool", :action-property "var-value"}]}]},
                        :start
                                         {:type        "sequence-data",
                                          :data        [{:type "start-activity"}
                                                        {:type "parallel",
                                                         :data
                                                               [{:id "inactive", :type "state", :target "felt-tip"}
                                                                {:id "inactive", :type "state", :target "brush"}
                                                                {:id "inactive", :type "state", :target "pencil"}
                                                                {:id "inactive", :type "state", :target "eraser"}]}
                                                        {:type "action", :id "set-current-tool", :params {:tool "felt-tip"}}],
                                          :description "Initial action"},
                        :finish-activity {:type "finish-activity"}},
        :triggers      {:start {:on "start", :action "start"}, :finish {:on "back", :action "finish-activity"}},
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
