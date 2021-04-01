(ns webchange.templates.library.categorize.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background      {:type "background", :src "/raw/img/categorize/background.png"},
                                       :yellow-box
                                                        {:type       "image",
                                                         :x          75,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :src        "/raw/img/categorize/yellow_box.png",
                                                         :transition "yellow-box",
                                                         :states     {:highlighted     {:highlight true}
                                                                      :not-highlighted {:highlight false}},
                                                         },
                                       :blue-box        {:type       "image",
                                                         :x          375,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :transition "blue-box",
                                                         :src        "/raw/img/categorize/blue_box.png",
                                                         :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                         }
                                       :red-box         {:type       "image",
                                                         :x          675,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :transition "red-box",
                                                         :src        "/raw/img/categorize/red_box.png",
                                                         :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                         }
                                       :purple-box
                                                        {:type       "image",
                                                         :x          975,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :src        "/raw/img/categorize/purple_box.png",
                                                         :transition "purple-box",
                                                         :states     {:highlighted     {:highlight true}
                                                                      :not-highlighted {:highlight false}},
                                                         },
                                       :orange-box
                                                        {:type       "image",
                                                         :x          1275,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :src        "/raw/img/categorize/orange_box.png",
                                                         :transition "orange-box",
                                                         :states     {:highlighted     {:highlight true}
                                                                      :not-highlighted {:highlight false}},
                                                         },
                                       :green-box
                                                        {:type       "image",
                                                         :x          1575,
                                                         :y          810,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.65
                                                         :src        "/raw/img/categorize/green_box.png",
                                                         :transition "green-box",
                                                         :states     {:highlighted     {:highlight true}
                                                                      :not-highlighted {:highlight false}},
                                                         },
                                       :blue-crayon-1   {
                                                         :type       "image",
                                                         :x          184,
                                                         :y          228,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   -53,
                                                         :src        "/raw/img/categorize/blue_crayons.png",
                                                         :transition "blue-crayon-1",
                                                         :draggable  true,
                                                         :actions    {
                                                                      :drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "blue-color"
                                                                                            :crayon-color "blue"}
                                                                                   }
                                                                      :drag-end   {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "blue-crayon-1"
                                                                                            :init-position  {:x 184, :y 228, :duration 1}
                                                                                            :check-variable "blue-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}},
                                                         }
                                       :yellow-crayon-1 {
                                                         :type       "image",
                                                         :x          145,
                                                         :y          521,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   -129,
                                                         :src        "/raw/img/categorize/yellow_crayons.png",
                                                         :transition "yellow-crayon-1",
                                                         :draggable  true,
                                                         :actions    {
                                                                      :drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "yellow-color"
                                                                                            :crayon-color "yellow"}
                                                                                   }
                                                                      :drag-end   {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "yellow-crayon-1"
                                                                                            :init-position  {:x 145, :y 521, :duration 1}
                                                                                            :check-variable "yellow-box-selected"}}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :red-crayon-1    {
                                                         :type       "image",
                                                         :x          760,
                                                         :y          64,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   31,
                                                         :src        "/raw/img/categorize/red_crayons.png",
                                                         :transition "red-crayon-1",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "red-color"
                                                                                            :crayon-color "red"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "red-crayon-1"
                                                                                            :init-position  {:x 760, :y 64, :duration 1}
                                                                                            :check-variable "red-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :orange-crayon-1 {
                                                         :type       "image",
                                                         :x          134,
                                                         :y          178,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   37,
                                                         :src        "/raw/img/categorize/orange_crayons.png",
                                                         :transition "orange-crayon-1",
                                                         :draggable  true,
                                                         :actions    {
                                                                      :drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "orange-color"
                                                                                            :crayon-color "orange"}}
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "orange-crayon-1"
                                                                                            :init-position  {:x 134, :y 178, :duration 1}
                                                                                            :check-variable "orange-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}},
                                                         }
                                       :purple-crayon-1 {
                                                         :type       "image",
                                                         :x          145,
                                                         :y          521,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   -39,
                                                         :src        "/raw/img/categorize/purple_crayons.png",
                                                         :transition "purple-crayon-1",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "purple-color"
                                                                                            :crayon-color "purple"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "purple-crayon-1"
                                                                                            :init-position  {:x 145, :y 521, :duration 1}
                                                                                            :check-variable "purple-box-selected"}}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :green-crayon-1  {
                                                         :type       "image",
                                                         :x          760,
                                                         :y          64,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   121,
                                                         :src        "/raw/img/categorize/green_crayons.png",
                                                         :transition "green-crayon-1",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "green-color"
                                                                                            :crayon-color "green"}}
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "green-crayon-1"
                                                                                            :init-position  {:x 760, :y 64, :duration 1}
                                                                                            :check-variable "green-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }

                                       :blue-crayon-2   {
                                                         :type       "image",
                                                         :x          1406,
                                                         :y          456,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   125,
                                                         :src        "/raw/img/categorize/blue_crayons.png",
                                                         :transition "blue-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "blue-color"
                                                                                            :crayon-color "blue"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "blue-crayon-2"
                                                                                            :init-position  {:x 1406, :y 456, :duration 1}
                                                                                            :check-variable "blue-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :yellow-crayon-2 {
                                                         :type       "image",
                                                         :x          1071,
                                                         :y          279,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   75,
                                                         :src        "/raw/img/categorize/yellow_crayons.png",
                                                         :transition "yellow-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "yellow-color"
                                                                                            :crayon-color "yellow"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "yellow-crayon-2"
                                                                                            :init-position  {:x 1071, :y 279, :duration 1}
                                                                                            :check-variable "yellow-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :red-crayon-2    {
                                                         :type       "image",
                                                         :x          736,
                                                         :y          387,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   -69,
                                                         :src        "/raw/img/categorize/red_crayons.png",
                                                         :transition "red-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "red-color"
                                                                                            :crayon-color "red"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "red-crayon-2"
                                                                                            :init-position  {:x 736, :y 387, :duration 1}
                                                                                            :check-variable "red-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :orange-crayon-2 {
                                                         :type       "image",
                                                         :x          1406,
                                                         :y          456,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   215,
                                                         :src        "/raw/img/categorize/orange_crayons.png",
                                                         :transition "orange-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "orange-color"
                                                                                            :crayon-color "orange"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "orange-crayon-2"
                                                                                            :init-position  {:x 1406, :y 456, :duration 1}
                                                                                            :check-variable "orange-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :purple-crayon-2 {
                                                         :type       "image",
                                                         :x          1071,
                                                         :y          279,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   165,
                                                         :src        "/raw/img/categorize/purple_crayons.png",
                                                         :transition "purple-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "purple-color"
                                                                                            :crayon-color "purple"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "purple-crayon-2"
                                                                                            :init-position  {:x 1071, :y 279, :duration 1}
                                                                                            :check-variable "purple-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :green-crayon-2  {
                                                         :type       "image",
                                                         :x          736,
                                                         :y          387,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   25,
                                                         :src        "/raw/img/categorize/green_crayons.png",
                                                         :transition "red-crayon-2",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "green-color"
                                                                                            :crayon-color "green"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "green-crayon-2"
                                                                                            :init-position  {:x 736, :y 387, :duration 1}
                                                                                            :check-variable "green-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :blue-crayon-3   {
                                                         :type       "image",
                                                         :x          1753,
                                                         :y          144,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   45,
                                                         :src        "/raw/img/categorize/blue_crayons.png",
                                                         :transition "blue-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "blue-color"
                                                                                            :crayon-color "blue"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "blue-crayon-3"
                                                                                            :init-position  {:x 1753, :y 144, :duration 1}
                                                                                            :check-variable "blue-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :yellow-crayon-3 {
                                                         :type       "image",
                                                         :x          1033,
                                                         :y          558,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   -120,
                                                         :src        "/raw/img/categorize/yellow_crayons.png",
                                                         :transition "yellow-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "yellow-color"
                                                                                            :crayon-color "yellow"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "yellow-crayon-3"
                                                                                            :init-position  {:x 1033, :y 558, :duration 1}
                                                                                            :check-variable "yellow-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :red-crayon-3    {
                                                         :type       "image",
                                                         :x          1097,
                                                         :y          106,
                                                         :width      65,
                                                         :height     635,
                                                         :rotation   -52,
                                                         :src        "/raw/img/categorize/red_crayons.png",
                                                         :transition "red-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "red-color"
                                                                                            :crayon-color "red"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "red-crayon-3"
                                                                                            :init-position  {:x 1097, :y 106, :duration 1}
                                                                                            :check-variable "red-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :orange-crayon-3 {
                                                         :type       "image",
                                                         :x          1753,
                                                         :y          144,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   135,
                                                         :src        "/raw/img/categorize/orange_crayons.png",
                                                         :transition "orange-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "orange-color"
                                                                                            :crayon-color "orange"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "orange-crayon-3"
                                                                                            :init-position  {:x 1753, :y 144, :duration 1}
                                                                                            :check-variable "orange-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :purple-crayon-3 {
                                                         :type       "image",
                                                         :x          1033,
                                                         :y          558,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   -30,
                                                         :src        "/raw/img/categorize/purple_crayons.png",
                                                         :transition "purple-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "purple-color"
                                                                                            :crayon-color "purple"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "purple-crayon-3"
                                                                                            :init-position  {:x 1033, :y 558, :duration 1}
                                                                                            :check-variable "purple-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       :green-crayon-3  {
                                                         :type       "image",
                                                         :x          1097,
                                                         :y          106,
                                                         :width      635,
                                                         :height     65,
                                                         :rotation   30,
                                                         :src        "/raw/img/categorize/green_crayons.png",
                                                         :transition "green-crayon-3",
                                                         :draggable  true,
                                                         :actions    {:drag-start {:type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag"
                                                                                   :params {:say-color "green-color"
                                                                                            :crayon-color "green"}
                                                                                   }
                                                                      :drag-end
                                                                                  {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:target         "green-crayon-3"
                                                                                            :init-position  {:x 1097, :y 106, :duration 1}
                                                                                            :check-variable "green-box-selected"
                                                                                            }}},
                                                         :states     {:hide {:visible false}}
                                                         }
                                       },
                       :scene-objects [["background"]
                                       ["yellow-box" "blue-box" "red-box"]
                                       ["purple-box" "orange-box" "green-box"]
                                       ["blue-crayon-1" "yellow-crayon-1" "red-crayon-1"]
                                       ["orange-crayon-1" "purple-crayon-1" "green-crayon-1"]
                                       ["red-crayon-2" "blue-crayon-2" "yellow-crayon-2"]
                                       ["green-crayon-2" "orange-crayon-2" "purple-crayon-2"]
                                       ["yellow-crayon-3" "red-crayon-3" "blue-crayon-3"]
                                       ["green-crayon-3" "orange-crayon-3" "purple-crayon-3"]
                                       ],
                       :actions       {

                                       :crayon-in-right-box {:id          "hide",
                                                             :type        "state",
                                                             :from-params [{:action-property "target" :param-property "target"}]},
                                       :crayon-revert       {:type        "transition",
                                                             :from-params [{:action-property "transition-id" :param-property "target"}
                                                                           {:action-property "to" :param-property "init-position"}]
                                                             }
                                       :wrong-option        {:type "sequence-data",
                                                             :data [{:id "crayon-revert", :type "action"}
                                                                    {:id "wrong-answer", :type "action"}],}

                                       :correct-option      {:type "sequence-data",
                                                             :data [{:type "counter" :counter-action "increase" :counter-id "sorted-crayons"}
                                                                    {:id "crayon-in-right-box", :type "action"}
                                                                    {:id "correct-answer", :type "action"}
                                                                    {:type       "test-var-inequality"
                                                                     :var-name   "sorted-crayons",
                                                                     :value      18,
                                                                     :inequality ">=",
                                                                     :success    "finish-scene",
                                                                     :fail       "continue-sorting"
                                                                     }
                                                                    ]}
                                       :check-option        {:type      "test-var-list-at-least-one-true"
                                                             :var-names ["yellow-box-selected" "blue-box-selected" "red-box-selected"
                                                                         "purple-box-selected" "orange-box-selected" "green-box-selected"]
                                                             :success   "wrong-answer",
                                                             }
                                       :drag-crayon         {:type "sequence-data"
                                                             :data [
                                                                    {:type        "copy-variable",
                                                                     :var-name "current-selection-state"
                                                                     :from-params [{:param-property "check-variable", :action-property "from"}]}

                                                                    {:type "set-variable", :var-name "say", :var-value false}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                    {:type        "test-var-scalar",
                                                                     :success     "correct-option",
                                                                     :fail       "crayon-revert",
                                                                     :value       true,
                                                                     :var-name "current-selection-state"}]
                                                             }
                                       :highlight           {:type "sequence-data"
                                                             :data [{:type        "set-variable",
                                                                     :var-value   true
                                                                     :from-params [{:action-property "var-name" :param-property "check-variable"}]
                                                                     }
                                                                    {:type        "state"
                                                                     :id          "highlighted"
                                                                     :from-params [{:action-property "target" :param-property "transition"}]
                                                                     }
                                                                    {:type        "test-var-scalar",
                                                                     :success     "check-option",
                                                                     :value       false,
                                                                     :from-params [{:template "%-box-selected"
                                                                                    :action-property "var-name" :param-property "crayon-color"}]
                                                                     }
                                                                    ]
                                                             }
                                       :unhighlight         {:type "sequence-data"
                                                             :data [{:type        "set-variable",
                                                                     :var-value   false
                                                                     :from-params [{:action-property "var-name" :param-property "check-variable"}]
                                                                     }
                                                                    {:type        "state"
                                                                     :id          "not-highlighted"
                                                                     :from-params [{:action-property "target" :param-property "transition"}]}
                                                                    ]
                                                             }
                                       :next-check-collide            {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["yellow-box" "blue-box" "red-box"
                                                                                     "purple-box" "orange-box" "green-box"
                                                                                     ]
                                                                     :action-params [{:check-variable "yellow-box-selected"}
                                                                                     {:check-variable "blue-box-selected"}
                                                                                     {:check-variable "red-box-selected"}
                                                                                     {:check-variable "purple-box-selected"}
                                                                                     {:check-variable "orange-box-selected"}
                                                                                     {:check-variable "green-box-selected"}
                                                                                     ]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}
                                       :yellow-color        (dialog/default "Color yellow")
                                       :blue-color          (dialog/default "Color blue")
                                       :red-color           (dialog/default "Color red")
                                       :purple-color        (dialog/default "Color purple")
                                       :orange-color        (dialog/default "Color orange")
                                       :green-color         (dialog/default "Color green")
                                       :say-color           {:type "sequence-data"
                                                             :data [{:type "action" :from-params [{:action-property "id"
                                                                                                   :param-property  "say-color"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-say",
                                                                     :value    true,
                                                                     :var-name "say"}]}
                                       :next-say            {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "say-color"
                                                                     :interval 100}]}

                                       :start-drag          {:type "sequence-data"
                                                             :data [{:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "purple-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "orange-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "green-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "say", :var-value true}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                    {:id "next-say" :type "action"}
                                                                    {:id "next-check-collide" :type "action"}
                                                                    ]}
                                       :init-activity       {:type "sequence-data"
                                                             :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                                                    {:type "action" :id "intro"}]

                                                             }
                                       :intro               {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "intro",
                                                             :phrase-description "Introduce task"}
                                       :correct-answer      {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "correct-answer",
                                                             :phrase-description "correct answer"}
                                       :wrong-answer        {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "wrong-answer",
                                                             :phrase-description "wrong answer"}
                                       :finish-scene        {:type "sequence-data",
                                                             :data [{:type "action" :id "finish-dialog"}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                    {:type "action", :id "stop-activity"}
                                                                    ],
                                                             }
                                       :finish-dialog       {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "finish-dialog",
                                                             :phrase-description "finish dialog"}
                                       :continue-sorting    {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "continue-sorting",
                                                             :phrase-description "Continue sorting"}
                                       :stop-activity       {:type "stop-activity", :id "categorize"},

                                       },

                       :triggers
                                      {:back  {:on "back", :action "stop-activity"},
                                       :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart true
                                       :tracks    [
                                                   {:title "Round 1"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :intro}
                                                            {:type "prompt"
                                                             :text "Correct answer"}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer}
                                                            {:type "prompt"
                                                             :text "Wrong answer"}
                                                            {:type      "dialog"
                                                             :action-id :wrong-answer}
                                                            {:type "prompt"
                                                             :text "Continue sorting"}
                                                            {:type      "dialog"
                                                             :action-id :continue-sorting}
                                                            {:type "prompt"
                                                             :text "Finish dialog"}
                                                            {:type      "dialog"
                                                             :action-id :finish-dialog}
                                                            ]}
                                                   {:title "Round 1 - colors"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :yellow-color}
                                                            {:type      "dialog"
                                                             :action-id :blue-color}
                                                            {:type      "dialog"
                                                             :action-id :red-color}
                                                            {:type      "dialog"
                                                             :action-id :purple-color}
                                                            {:type      "dialog"
                                                             :action-id :orange-color}
                                                            {:type      "dialog"
                                                             :action-id :green-color}
                                                            ]}
                                                   ]
                                       },
                       })