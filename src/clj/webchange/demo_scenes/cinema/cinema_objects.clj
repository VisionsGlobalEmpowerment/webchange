(ns webchange.demo-scenes.cinema.cinema-objects)

(def cinema-objects
  {:background     {:type "background"
                    :src  "/raw/img/cinema/background.jpg"}
   :screen-overlay {:type   "image"
                    :src    "/raw/img/cinema/screen-off.png"
                    :width  1238
                    :height 678
                    :x      342
                    :y      109
                    :states {:hidden  {:type "transparent"}
                             :visible {:type "image"}}}
   :mari           {:type       "animation"
                    :name       "mari"
                    :transition "mari"
                    :anim       "idle"
                    :start      true
                    :speed      0.35
                    :x          1613
                    :y          785
                    :width      473
                    :height     511
                    :scale-y    0.3
                    :scale-x    0.3}
   :senora-vaca    {:type    "animation"
                    :name    "senoravaca"
                    :anim    "idle"
                    :start   true
                    :speed   0.3
                    :x       263
                    :y       915
                    :width   351
                    :height  717
                    :scale-y 0.5
                    :scale-x 0.5}
   :card-a         {:type     "group"
                    :x        350
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-a-image" "card-a-text"]
                    :states   {:normal  {:x 350 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 335 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-a-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/squirrel.png"
                    :origin {:type "center-top"}}
   :card-a-text    {:type           "text"
                    :text           "A"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-b         {:type     "group"
                    :x        475
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-b-image" "card-b-text"]
                    :states   {:normal  {:x 475 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 450 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-b-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/baby.png"
                    :origin {:type "center-top"}}
   :card-b-text    {:type           "text"
                    :text           "B"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-c         {:type     "group"
                    :x        600
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-c-image" "card-c-text"]
                    :states   {:normal  {:x 600 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 585 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-c-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/home.png"
                    :origin {:type "center-top"}}
   :card-c-text    {:type           "text"
                    :text           "C"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-ch        {:type     "group"
                    :x        725
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-ch-image" "card-ch-text"]
                    :states   {:normal  {:x 725 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 710 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-ch-image  {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/chocolate.png"
                    :origin {:type "center-top"}}
   :card-ch-text   {:type           "text"
                    :text           "CH"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-d         {:type     "group"
                    :x        850
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-d-image" "card-d-text"]
                    :states   {:normal  {:x 850 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 835 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-d-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/diamond.png"
                    :origin {:type "center-top"}}
   :card-d-text    {:type           "text"
                    :text           "D"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-e         {:type     "group"
                    :x        975
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-e-image" "card-e-text"]
                    :states   {:normal  {:x 975 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 960 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-e-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/elephant.png"
                    :origin {:type "center-top"}}
   :card-e-text    {:type           "text"
                    :text           "E"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-f         {:type     "group"
                    :x        1100
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-f-image" "card-f-text"]
                    :states   {:normal  {:x 1100 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 1085 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-f-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/sunflower.png"
                    :origin {:type "center-top"}}
   :card-f-text    {:type           "text"
                    :text           "F"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-g         {:type     "group"
                    :x        1225
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-g-image" "card-g-text"]
                    :states   {:normal  {:x 1225 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 1210 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-g-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/cat.png"
                    :origin {:type "center-top"}}
   :card-g-text    {:type           "text"
                    :text           "G"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-h         {:type     "group"
                    :x        1350
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-h-image" "card-h-text"]
                    :states   {:normal  {:x 1350 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 1335 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-h-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/leaf.png"
                    :origin {:type "center-top"}}
   :card-h-text    {:type           "text"
                    :text           "H"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-i         {:type     "group"
                    :x        1475
                    :y        120
                    :width    100
                    :height   164
                    :children ["card-i-image" "card-i-text"]
                    :states   {:normal  {:x 1475 :y 120 :scale-x 1 :scale-y 1}
                               :pointed {:x 1460 :y 95 :scale-x 1.3 :scale-y 1.3}}}
   :card-i-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/magnet.png"
                    :origin {:type "center-top"}}
   :card-i-text    {:type           "text"
                    :text           "I"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-j         {:type     "group"
                    :x        350
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-j-image" "card-j-text"]
                    :states   {:normal  {:x 350 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 335 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-j-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/garden.png"
                    :origin {:type "center-top"}}
   :card-j-text    {:type           "text"
                    :text           "J"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-k         {:type     "group"
                    :x        475
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-k-image" "card-k-text"]
                    :states   {:normal  {:x 475 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 460 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-k-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/kimono.png"
                    :origin {:type "center-top"}}
   :card-k-text    {:type           "text"
                    :text           "K"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-l         {:type     "group"
                    :x        600
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-l-image" "card-l-text"]
                    :states   {:normal  {:x 600 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 585 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-l-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/lion.png"
                    :origin {:type "center-top"}}
   :card-l-text    {:type           "text"
                    :text           "L"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-ll        {:type     "group"
                    :x        725
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-ll-image" "card-ll-text"]
                    :states   {:normal  {:x 725 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 710 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-ll-image  {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/wrench.png"
                    :origin {:type "center-top"}}
   :card-ll-text   {:type           "text"
                    :text           "LL"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-m         {:type     "group"
                    :x        850
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-m-image" "card-m-text"]
                    :states   {:normal  {:x 850 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 835 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-m-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/hand.png"
                    :origin {:type "center-top"}}
   :card-m-text    {:type           "text"
                    :text           "M"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"}
   :card-n         {:type     "group"
                    :x        975
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-n-image" "card-n-text"]
                    :states   {:normal  {:x 975 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 960 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-n-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/boy.png"
                    :origin {:type "center-top"}}
   :card-n-text    {:type           "text"
                    :text           "N"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-ñ         {:type     "group"
                    :x        1100
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-ñ-image" "card-ñ-text"]
                    :states   {:normal  {:x 1100 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 1085 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-ñ-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/ostrich.png"
                    :origin {:type "center-top"}}
   :card-ñ-text    {:type           "text"
                    :text           "Ñ"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-o         {:type     "group"
                    :x        1225
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-o-image" "card-o-text"]
                    :states   {:normal  {:x 1225 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 1210 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-o-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/bear.png"
                    :origin {:type "center-top"}}
   :card-o-text    {:type           "text"
                    :text           "O"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-p         {:type     "group"
                    :x        1350
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-p-image" "card-p-text"]
                    :states   {:normal  {:x 1350 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 1335 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-p-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/ball.png"
                    :origin {:type "center-top"}}
   :card-p-text    {:type           "text"
                    :text           "P"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-q         {:type     "group"
                    :x        1475
                    :y        320
                    :width    100
                    :height   164
                    :children ["card-q-image" "card-q-text"]
                    :states   {:normal  {:x 1475 :y 320 :scale-x 1 :scale-y 1}
                               :pointed {:x 1460 :y 295 :scale-x 1.3 :scale-y 1.3}}}
   :card-q-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/cheese.png"
                    :origin {:type "center-top"}}
   :card-q-text    {:type           "text"
                    :text           "Q"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-r         {:type     "group"
                    :x        350
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-r-image" "card-r-text"]
                    :states   {:normal  {:x 350 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 335 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-r-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/frog.png"
                    :origin {:type "center-top"}}
   :card-r-text    {:type           "text"
                    :text           "R"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-rr        {:type     "group"
                    :x        475
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-rr-text"]
                    :states   {:normal  {:x 475 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 460 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   ;:card-rr-image   {:type   "image"
   ;                 :x      50
   ;                 :y      50
   ;                 :width  100
   ;                 :height 100
   ;                 :src    "/raw/img/elements/frog.png"
   ;                 :origin {:type "center-top"}}
   :card-rr-text   {:type           "text"
                    :text           "RR"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-s         {:type     "group"
                    :x        600
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-s-text"]
                    :states   {:normal  {:x 600 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 585 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   ;:card-s-image   {:type   "image"
   ;                 :x      50
   ;                 :y      50
   ;                 :width  100
   ;                 :height 100
   ;                 :src    "/raw/img/elements/frog.png"
   ;                 :origin {:type "center-top"}}
   :card-s-text    {:type           "text"
                    :text           "S"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-t         {:type     "group"
                    :x        725
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-t-image" "card-t-text"]
                    :states   {:normal  {:x 725 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 710 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-t-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/tomato.png"
                    :origin {:type "center-top"}}
   :card-t-text    {:type           "text"
                    :text           "T"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-u         {:type     "group"
                    :x        850
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-u-image" "card-u-text"]
                    :states   {:normal  {:x 850 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 835 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-u-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/grapes.png"
                    :origin {:type "center-top"}}
   :card-u-text    {:type           "text"
                    :text           "U"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-v         {:type     "group"
                    :x        975
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-v-text"]
                    :states   {:normal  {:x 975 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 960 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   ;:card-v-image   {:type   "image"
   ;                 :x      50
   ;                 :y      50
   ;                 :width  100
   ;                 :height 100
   ;                 :src    "/raw/img/elements/grapes.png"
   ;                 :origin {:type "center-top"}}
   :card-v-text    {:type           "text"
                    :text           "V"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-w         {:type     "group"
                    :x        1100
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-w-image" "card-w-text"]
                    :states   {:normal  {:x 1100 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 1085 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-w-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/website.png"
                    :origin {:type "center-top"}}
   :card-w-text    {:type           "text"
                    :text           "W"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-x         {:type     "group"
                    :x        1225
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-x-image" "card-x-text"]
                    :states   {:normal  {:x 1225 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 1210 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-x-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/xylophone.png"
                    :origin {:type "center-top"}}
   :card-x-text    {:type           "text"
                    :text           "X"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-y         {:type     "group"
                    :x        1350
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-y-image" "card-y-text"]
                    :states   {:normal  {:x 1350 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 1335 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-y-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/yoyo.png"
                    :origin {:type "center-top"}}
   :card-y-text    {:type           "text"
                    :text           "Y"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}
   :card-z         {:type     "group"
                    :x        1475
                    :y        520
                    :width    100
                    :height   164
                    :children ["card-z-image" "card-z-text"]
                    :states   {:normal  {:x 1475 :y 520 :scale-x 1 :scale-y 1}
                               :pointed {:x 1460 :y 495 :scale-x 1.3 :scale-y 1.3}}}
   :card-z-image   {:type   "image"
                    :x      50
                    :y      50
                    :width  100
                    :height 100
                    :src    "/raw/img/elements/boots.png"
                    :origin {:type "center-top"}}
   :card-z-text    {:type           "text"
                    :text           "Z"
                    :x              0
                    :y              0
                    :width          100
                    :height         50
                    :font-size      64
                    :align          "center"
                    :vertical-align "middle"
                    :font-family    "Luckiest Guy"
                    :fill           "black"}})