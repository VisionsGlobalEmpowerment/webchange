(ns webchange.dashboard.students.dashboard-students-list.utils)

(def stub-students-list
  [{:id         1
    :class-id   777
    :first-name "Mateo"
    :last-name  "Gomez"
    :gender 2
    :age        4
    :class      "A1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         2
    :class-id   777
    :first-name "Arun"
    :last-name  "Cole"
    :gender 1
    :age        4
    :class      "A1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         3
    :class-id   777
    :first-name "Stanislaw"
    :last-name  "Paine"
    :gender 1
    :age        5
    :class      "A1"
    :course     "This website uses cookies to ensure you get the best experience on this site"
    :tablet?    true}
   {:id         4
    :class-id   777
    :first-name "Nabilah"
    :last-name  "Castro"
    :age        5
    :class      "A2"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         5
    :class-id   777
    :first-name "Brandi"
    :last-name  "Dupont"
    :gender 2
    :age        4
    :class      "A2"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         6
    :class-id   777
    :first-name "Inayah"
    :last-name  "Cote"
    :gender 1
    :age        4
    :class      "B1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         7
    :class-id   777
    :first-name "Lavinia"
    :last-name  "Sandoval"
    :gender 2
    :age        6
    :class      "B1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         8
    :class-id   777
    :first-name "Wesley"
    :last-name  "Edge"
    :gender 1
    :age        4
    :class      "B1"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         9
    :class-id   777
    :first-name "Kieran"
    :last-name  "Barnard"
    :gender 1
    :age        6
    :class      "B2"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         10
    :class-id   777
    :first-name "Terrence"
    :last-name  "Farrell"
    :gender 2
    :age        4
    :class      "B3"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         11
    :class-id   777
    :first-name "Marina"
    :last-name  "Finney"
    :gender 2
    :age        3
    :class      "B3"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         12
    :class-id   777
    :first-name "Adem"
    :last-name  "Leon"
    :gender 1
    :age        4
    :class      "B3"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         13
    :class-id   777
    :first-name "Elouise"
    :last-name  "Yu"
    :gender 1
    :age        5
    :class      "C1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         14
    :class-id   777
    :first-name "Latisha"
    :last-name  "Sutherland"
    :gender 2
    :age        4
    :class      "C1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         15
    :class-id   777
    :first-name "Ursula"
    :last-name  "Braun"
    :gender 2
    :age        5
    :class      "C1"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         16
    :class-id   777
    :first-name "Marshall"
    :last-name  "Preece"
    :gender 1
    :age        5
    :class      "C2"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         17
    :class-id   777
    :first-name "Kya"
    :last-name  "Talley"
    :gender 2
    :age        5
    :class      "B3"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         18
    :class-id   777
    :first-name "Muskaan"
    :last-name  "Hardy"
    :gender 1
    :age        6
    :class      "C2"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         19
    :class-id   777
    :first-name "Ciaron"
    :last-name  "Dodd"
    :gender 1
    :age        6
    :class      "C2"
    :course     "Vera La Vaquita"
    :tablet?    true}
   {:id         20
    :class-id   777
    :first-name "Julian"
    :last-name  "Sparks"
    :gender 1
    :age        6
    :class      "C2"
    :course     "Vera La Vaquita"
    :tablet?    false}
   {:id         21
    :class-id   777
    :first-name "Hamid"
    :last-name  "Padilla"
    :gender 1
    :age        7
    :class      "C3"
    :course     "Vera La Vaquita"
    :tablet?    true}])

(defn map-students-list
  [_]
  stub-students-list)
