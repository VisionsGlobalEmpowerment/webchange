(ns webchange.templates.library.casa
  (:require
    [webchange.templates.core :as core]))

(def m {:id          1
        :name        "casa"
        :description "Some description of casa mechanics and covered skills"})

(def t {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}]
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/casa/background_casa.png"}
                                             :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}
                        :vera
                                            {:type   "animation",
                                             :x      1128,
                                             :y      960,
                                             :width  380,
                                             :height 537,
                                             :scale  {:x 3, :y 3},
                                             :anim   "idle",
                                             :name   "vera",
                                             :skin   "01 Vera_1"
                                             :speed  0.3,
                                             :start  true}}
        :scene-objects [["layered-background"] ["vera"]],
        :actions       {:main-dialog {:type               "animation-sequence",
                                      :data               [],
                                      :phrase             "intro",
                                      :phrase-description "Introduce activity",
                                      :target             "vera"
                                      :track              1}}
        :triggers
                       {:start {:on "start", :action "main-dialog"}}})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))

