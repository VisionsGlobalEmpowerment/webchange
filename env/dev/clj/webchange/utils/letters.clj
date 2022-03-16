(ns webchange.utils.letters
  (:require
    [webchange.renderer.letters-path :refer [letters-path]]))

" How draw a letter:

  - Open 'https://danmarshall.github.io/google-font-to-svg-path/' in browser;
  - Upload desired font, type the letter in 'Text' field;
  - Select svg element with rendered letter in Developer tools -> Elements (Ctrl + Shift + c).
    Right click on element in panel -> 'Capture node screenshot'.
    Image with letter will be downloaded;
  - Open svg editor: 'https://yqnn.github.io/svg-path-editor/'
  - Run code from 'Get letter frame' comment block in this file.
  - Paste the result into 'Path' input in svg editor. You should be able to see a frame of the future letter:
      - outer vertical lines: bounds on capital letter,
      - inner vertical lines: bounds of lower case letter,
      - top horizontal line: top bound of capital letter or letter with ascender, like 'h' or 'l',
      - second  horizontal line: top bound of lower case letter,
      - third: base line of any letters,
      - last one: bottom bound of letter with descender, like 'q' or 'j';
  - In the top right corner of the editor press button with image icon.
    Choose downloaded from 'google-font-to-svg-path' image and import it;
  - Place (move and scale) the image into frame.
    When done press image button again to switch off image management mode;
  - Draw letter svg path: press '...' of the last element in 'COMMANDS' block -> 'Insert After' -> ...
      - Use absolute coordinates for the first 'M' command and relative coordinates for the rest commands.
  - When svg is ready save (in `webchange.renderer.letters-path/letters-path`) the result without the frame."

(defn lower-case-letter?
  [letter]
  (-> (clojure.string/lower-case letter)
      (= letter)))

(def upper-case-letter? (complement lower-case-letter?))

(defn get-lower-case-letters
  "Get all lowercase letters defined in `webchange.renderer.letters-path`"
  []
  (->> letters-path
       (map first)
       (filter lower-case-letter?)))

(defn get-upper-case-letters
  "Get all capital letters defined in `webchange.renderer.letters-path`"
  []
  (->> letters-path
       (map first)
       (filter upper-case-letter?)))

(defn get-letters-path
  "Map letters into svg letters path"
  ([letters]
   (get-letters-path :shape letters))
  ([type letters]
   (->> letters
        (map #(get-in letters-path [% type])))))

(defn get-horizontal-line
  [left right y]
  (->> ["M" left y "L" right y]
       (clojure.string/join " ")))

(defn get-vertical-line
  [top bottom x]
  (->> ["M" x top "L" x bottom]
       (clojure.string/join " ")))

;; Special letters: á, é, í, ó, ú, ü, ñ

(comment
  "Get many letters paths"

  (->> (get-lower-case-letters)
       (filter #(= % "a"))
       (get-letters-path :trace)
       (clojure.string/join "\n")))

(comment
  "Get letter frame"

  (let [width-big 124                                       ; max width of capital letter
        width-small 78                                      ; max width if lower case letter
        center-x 124                                        ; x coordinate of letter's center

        ascent 33                                           ; top bound of capital letter or letter with ascender, like 'h' or 'l'
        median 109                                          ; top bound of lower case letter
        baseline 191                                        ; base line of any letter
        descent 263                                         ; bottom bound of letter with descender, like 'q' or 'j'

        left-big (- center-x (quot width-big 2))
        left-small (- center-x (quot width-small 2))
        right-big (+ center-x (quot width-big 2))
        right-small (+ center-x (quot width-small 2))

        v (partial get-vertical-line ascent descent)
        h (partial get-horizontal-line left-big right-big)]
    (->> [(h ascent)
          (h median)
          (h baseline)
          (h descent)

          (v left-big)
          (v left-small)
          (v right-small)
          (v right-big)]
         (clojure.string/join " "))))
