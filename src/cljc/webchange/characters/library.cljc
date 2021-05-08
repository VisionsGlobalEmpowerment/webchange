(ns webchange.characters.library)

(def adults [{:title    "Adult"
              :skeleton :senoravaca
              :preview  "/images/characters/adult.png"
              :skins    [{:skin "vaca"
                          :name "Cow"}
                         {:skin "6_tall woman"
                          :name "Woman"}
                         {:skin "12 owl"
                          :name "Owl"}
                         {:skin "lion"
                          :name "Lion"}]}])

; Senoravaca available skins:
;      "default",
;      "3_cow2",
;      "4_cow3",
;      "5_cow4",
; +    "6_tall woman",
;      "7 tiger",
;      "8 rhino",
;      "9 arabian",
;      "10 deer",
;      "11mexican",
; +    "12 owl",
; +    "lion",
;      "lion2",
;      "lion3",
;      "lion4",
; +    "vaca"

(def children [{:title    "Child"
                :skeleton :vera
                :preview  "/images/characters/child.png"
                :skins    [{:skin    "01 Vera_1"
                            :name    "Cow"
                            :preview "/images/characters/child.png"}
                           {:skin "girl"
                            :name "Girl"}
                           {:skin "boy"
                            :name "Boy"}]}])

; Vera available skins:
; +    "01 Vera_1",
;      "02 Vera_2",
;      "03 Vera_3",
; +    "04 girl",
; +    "05 boy",
;      "06 girl2"

(def assistants-movable [{:title    "Assistant Movable"
                          :skeleton :mari
                          :preview  "/images/characters/assistants_movable.png"
                          :skins    []}])

(def assistants-static [{:title    "Assistant Static"
                         :skeleton :stone
                         :skins    []}])

(def characters (concat adults
                        children
                        assistants-movable
                        assistants-static))
