(ns webchange.utils.alphabets)

(def english ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"])
(def spanish (concat english
                     ["á" "ch" "é" "í" "ll" "ó" "ú" "ü" "ñ"]))
(def tamil ["ௐ" "அ" "ஆ" "இ" "ஈ" "உ" "ஊ" "எ" "ஏ" "ஐ" "ஒ" "ஓ" "ஔ" "ஃ" "க" "ங" "ச" "ஞ" "ட" "ண" "த" "ந" "ப" "ம" "ய" "ர" "ல" "வ" "ழ" "ள" "ற" "ன" "ஜ" "ஷ" "ஸ" "ஹ" "க்" "ங்" "ச்" "ஞ்" "ட்" "ண்" "த்" "ந்" "ப்" "ம்" "ய்" "ர்" "ல்" "வ்" "ழ்" "ள்" "ற்" "ன்"])
(def sepedi (concat english
                    ["ê" "ô" "š"]))

(defn lang->alphabet
  [lang]
  (case lang
    "english" english
    "spanish" spanish
    "tamil" tamil
    "sepedi" sepedi))

(defn options-for
  [lang]
  (->> lang
       lang->alphabet
       (map (fn [letter] {:text letter :value letter}))))
