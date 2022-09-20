(ns webchange.emails.templates.reset-password
  (:require
    [config.core :refer [env]]))

(def hostname (get env :platform-host "webchange.local"))

(defn- reset-password-link
  [code]
  (str "https://" hostname "/accounts/reset-password/" code))

(defn reset-password-template
  [{:keys [code]}]
  [:html 
   [:head 
    [:title "BLUE BRICK SCHOOL"] 
    [:meta {:http-equiv "Content-Type", :content "text/html; charset=UTF-8"}] 
    [:link {:href "https://fonts.googleapis.com/css2?family=Noto+Sans:wght@100;200;300;400;500;600;700;800;900&display=swap", :rel "stylesheet"}]] 
   [:body {:bgcolor "#e3e0d8"
           :style "#e3e0d8;"
           :leftmargin "0"
           :topmargin "0"
           :marginwidth "0"
           :marginheight "0"} 
    [:center 
     [:table {:width "600"
              :border "0"
              :cellspacing "0"
              :cellpadding "0"
              :align "center"
              :style "margin: 0 auto; display:block;"} 
      [:tbody 
       [:tr 
        [:td
         [:table {:width "600"
                  :cellspacing "0"
                  :cellpadding "0"
                  :border "0"
                  :style "border-collapse:collapse;background:#e9f5ff url(" (str "https://" hostname  "/images/auth/temp-bg-1.png") ") center no-repeat;background-size:100% 100%;padding:40px 0;"} 
          [:tbody 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 80px 60px;"} 
             [:p {:style "text-align:left;color:#3452A5;font-size:16px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0"}
              "Follow this link to reset the password" 
              [:br] (reset-password-link code)]]]]]
         
         [:table {:width "600"
                  :cellspacing "0"
                  :cellpadding "0"
                  :border "0"
                  :style "border-collapse:collapse;background:#80BFE5;"} 
          [:tbody 
           [:tr 
            [:td {:valign "top"
                  :height "30"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-size:14px;font-weight:700;font-family: 'Noto Sans', sans-serif;margin:0;padding:10px 0 0px 0"}
              "Blue Brick School"]]] 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 70px;"} 
             [:p {:style "text-align:center;color:#3452A5;font-size:14px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:30px;"}
              "18800 Von Karman Ave., Suite A, Irvine, CA, 92612"]]] 
           [:tr 
            [:td {:valign "top"
                  :height "20"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 70px;"} 
             [:p {:style "text-align:center;color:#3452A5;font-size:12px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:30px;"}
              "Want to unsubscribe?&nbsp;" 
              [:a {:href "#", :style "font-weight:700;color:#3452A5;text-decoration:none;"} 
               [:b "Click here."]
               ]]
             ]] 
           [:tr 
            [:td {:valign "top", :height "20"}]]]]]]]]]]])
