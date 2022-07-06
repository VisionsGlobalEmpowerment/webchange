(ns webchange.parent.widgets.layout.footer.views
  (:require
    [webchange.ui.index :as ui]))

(defn- copyright
  []
  [:div {:class-name "footer-copyright"}
   "© 2022 Blue Brick School"])

(defn- links
  []
  (let [links [{:text "Terms"
                :href "https://bluebrickschool.org/terms-conditions/"}
               {:text "Privacy Policy"
                :href "https://bluebrickschool.org/privacy-policy/"}]]
    [:div {:class-name "footer-links"}
     (for [[idx {:keys [text href]}] (map-indexed vector links)]
       ^{:key idx}
       [ui/link {:href       href
                 :target     "_blank"
                 :class-name "footer-link"}
        text])]))

(defn- socials
  []
  (let [links [{:icon "tweeter"
                :href ""}
               {:icon "linkedin"
                :href ""}
               {:icon "facebook"
                :href ""}
               {:icon "instagram"
                :href ""}
               {:icon "youtube"
                :href ""}]]
    [:div {:class-name "footer-socials"}
     (for [[idx data] (map-indexed vector links)]
       ^{:key idx}
       [ui/button (merge {:color "blue-1"}
                         data)])]))

(defn footer
  []
  [:div.parent--layout--footer
   [copyright]
   [links]
   [socials]])
