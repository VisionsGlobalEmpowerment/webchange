(ns webchange.editor-v2.components.character-form.data)

(defn get-background-color
  [{:keys [character skin]
    :or   {skin "01"}}]
  (let [colors {"boy"     {"01" "#FFD260"
                           "02" "#CFDE73"
                           "03" "#5FBA99"
                           "04" "#F07160"
                           "05" "#C6AA95"}
                "girl"    {"01" "#1EA3B6"
                           "02" "#89C57F"
                           "03" "#7FC5C1"
                           "04" "#7FA3C5"
                           "05" "#B095C6"}
                "adult"   {"01" "#BCCCD3"
                           "02" "#74C898"
                           "03" "#5D9BAE"
                           "04" "#B0AA55"
                           "05" "#E8AE43"}
                "teacher" "#CA215E"
                "student" "#E73434"
                "guide"   "#FFB053"}]
    (cond
      (some #{character} ["boy" "girl"]) (get-in colors [character skin])
      (some #{character} ["man" "woman"]) (get-in colors ["adult" skin])
      (some #{character} ["teacher" "student" "guide"]) (get colors character)
      :else "#FFFFFF")))

(def characters [{:name           "Man"
                  :value          "man"
                  :background     (get-background-color {:character "man" :skin "02"})
                  :combined-skin? true
                  :heads          ["01"]
                  :clothes        ["01"]
                  :defaults       {:skin  "02"
                                   :head  "01"
                                   :cloth "01"}}
                 {:name           "Woman"
                  :value          "woman"
                  :background     (get-background-color {:character "woman" :skin "03"})
                  :combined-skin? true
                  :heads          ["01"]
                  :clothes        ["01"]
                  :defaults       {:skin  "03"
                                   :head  "01"
                                   :cloth "01"}}
                 {:name           "Boy"
                  :value          "boy"
                  :background     (get-background-color {:character "boy" :skin "04"})
                  :combined-skin? true
                  :heads          ["01" "02" "03"]
                  :clothes        ["01" "02" "03"]
                  :defaults       {:skin  "04"
                                   :head  "02"
                                   :cloth "02"}}
                 {:name           "Girl"
                  :value          "girl"
                  :background     (get-background-color {:character "girl" :skin "01"})
                  :combined-skin? true
                  :heads          ["01" "02" "03"]
                  :clothes        ["01" "02" "03"]
                  :defaults       {:skin  "01"
                                   :head  "01"
                                   :cloth "01"}}
                 {:name       "Teacher"
                  :value      "teacher"
                  :background (get-background-color {:character "teacher"})
                  :defaults   {:skin "vaca"}}
                 {:name       "Student"
                  :value      "student"
                  :background (get-background-color {:character "student"})
                  :defaults   {:skin "01 Vera_1"}}
                 {:name       "Guide"
                  :value      "guide"
                  :background (get-background-color {:character "guide"})
                  :defaults   {:skin "01 mari"}}])

(def single-skin-characters {"teacher" "senoravaca"
                             "student" "vera"
                             "guide"   "mari"})

(defn single-skin-character?
  [character-name]
  (some #{character-name} (keys single-skin-characters)))

(defn skeleton->character
  [skeleton-name]
  (some (fn [[character skeleton]]
          (and (= skeleton skeleton-name) character))
        single-skin-characters))

(defn character->skeleton
  [character-name]
  (get single-skin-characters character-name))

(defn animation->character-data
  [animation-data]
  (let [skeleton (get animation-data :name)
        head (get-in animation-data [:skin-names :head])
        clothes (get-in animation-data [:skin-names :clothes])]
    {:character (case skeleton
                  "child" (-> head
                              (clojure.string/split #"-")
                              (second)
                              (clojure.string/lower-case))
                  "adult" (-> clothes
                              (clojure.string/split #"-")
                              (first)
                              (clojure.string/split #"/")
                              (second)
                              (clojure.string/lower-case))
                  (skeleton->character skeleton))
     :skin      (case skeleton
                  "child" (-> head
                              (clojure.string/split #"-")
                              (last))
                  "adult" (-> head
                              (clojure.string/split #"-")
                              (last))
                  "01")
     :head      (case skeleton
                  "child" (-> head
                              (clojure.string/split #"-")
                              (nth 2))
                  "01")
     :clothes   (case skeleton
                  "child" (-> clothes
                              (clojure.string/split #"-")
                              (second))
                  "01")}))

(def skins [{:name  "Tone 1"
             :value "01"
             :color "#F9B491"}
            {:name  "Tone 2"
             :value "02"
             :color "#E8835B"}
            {:name  "Tone 3"
             :value "03"
             :color "#D1645E"}
            {:name  "Tone 4"
             :value "04"
             :color "#964535"}
            {:name  "Tone 5"
             :value "05"
             :color "#592928"}])

(def heads [{:name  "Type 1"
             :value "01"}
            {:name  "Type 2"
             :value "02"}
            {:name  "Type 3"
             :value "03"}])

(def clothes [{:name  "Type 1"
               :value "01"}
              {:name  "Type 2"
               :value "02"}
              {:name  "Type 3"
               :value "03"}])

(def previews-path "/raw/img/characters/")

(defn local->absolute-path
  [path]
  (str previews-path path))

(defn get-preview
  [{:keys [entity character head skin clothes]
    :or   {head "01" skin "01" clothes "01"}}]
  {:pre [(some #{entity} ["character" "clothes" "head"])
         (some #{character} ["boy" "girl" "man" "woman" "teacher" "student" "guide"])
         (some #{head} ["01" "02" "03"])
         (some #{skin} ["01" "02" "03" "04" "05"])
         (some #{clothes} ["01" "02" "03"])]}
  (let [file-name (cond
                    (= entity "character") (str "character_" character ".png")
                    (= entity "clothes") (str "clothes_" character "_" clothes "_" skin ".png")
                    (= entity "head") (str "head_" character "_" head "_" skin ".png"))]
    (local->absolute-path file-name)))
