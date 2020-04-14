(ns webchange.ui.theme
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.core :refer [create-mui-theme]]
    [webchange.ui.utils :refer [deep-merge]]))

(def current-theme (atom nil))                              ;; "dark" / "light"

(def w-colors
  {:primary        "#222342"
   :primary-darken "#191a31"
   :default        "#ffffff"
   :secondary      "#fd4142"
   :disabled       "#bababa"})

(def color-themes
  {:dark  {:palette {:type        "dark"
                     :background  {:default "#212121"
                                   :darken  "#2a2a2a"
                                   :paper   "#323232"}
                     :border      {:default "#555555"}
                     :primary     {:main "#1272e6"}
                     :text        {:secondary "#555555"}
                     :flat-button {:background-color "#3c3c3c"}}}
   :light {:palette {:type        "light"
                     :background  {:default "#ffffff"
                                   :darken  "#f5f5f5"
                                   :paper   "#fff"}
                     :border      {:default "#cecece"}
                     :primary     {:main "#1272e6"}
                     :text        {:secondary "#555555"}
                     :flat-button {:background-color "#f2f2f7"}}}})

(defn mui-theme
  [type]
  (reset! current-theme type)
  (let [theme (keyword type)
        color-theme (get color-themes theme)
        common-theme {:typography {:h2                {:fontSize    "1.875rem"
                                                       :font-weight "900"}
                                   :use-next-variants true}
                      :overrides  {:MuiInputBase     {:root {:&:before {:display "none"}
                                                             :&:after  {:display "none"}}}
                                   :MuiButton        {:root      {:font-size      "0.85rem"
                                                                  :font-weight    "bold"
                                                                  :text-transform "capitalize"}
                                                      :contained {:border-radius "20px"
                                                                  :padding       "6px 40px"}
                                                      :flat      {:background-color (get-in color-themes [theme :palette :flat-button :background-color])
                                                                  :padding          "6px 40px"
                                                                  :border-radius    "15px"}}
                                   :MuiOutlinedInput {:input {:padding "11.5px 14px"}}
                                   :MuiInputLabel    {:formControl {:transform "translate(20px, 28px)"}
                                                      :outlined {:transform "translate(14px, 14px)"}}
                                   :MuiList          {:root {:background-color (get-in color-themes [theme :palette :background :paper])
                                                             :padding          "8px"}}
                                   :MuiMenu          {:paper {:margin-top  "-2px"
                                                              :margin-left "1px"}}
                                   :MuiMenuItem      {:root {:padding       "5px 11px"
                                                             :border-radius "6px"
                                                             :margin        "5px 0"}}
                                   :MuiSelect        {:selectMenu {:border-color  (get-in color-themes [theme :palette :border :default])
                                                                   :border-width  "1px"
                                                                   :border-style  "solid"
                                                                   :border-radius "6px"
                                                                   :padding       "10px 19px"
                                                                   :text-align    "start"
                                                                   :&:focus       {:border-radius "6px"}}
                                                      :icon       {:right "9px"
                                                                   :color (get-in color-themes [theme :palette :border :default])}}}}]
    (create-mui-theme (deep-merge common-theme color-theme))))

(defn get-in-theme
  [path]
  (get-in (-> @current-theme
              (mui-theme)
              (js->clj))
          (->> path
               (map #(if (= (type %) Keyword) (name %) %))
               (vec))))

(defn with-mui-theme
  ([children]
   (with-mui-theme {} children))
  ([{:keys [type theme]} children]
   (let [current-theme-type (if-not (nil? type)
                              type
                              "light")
         current-theme (if-not (nil? theme)
                         (create-mui-theme theme)
                         (mui-theme current-theme-type))]
     [ui/mui-theme-provider {:theme current-theme} children])))
