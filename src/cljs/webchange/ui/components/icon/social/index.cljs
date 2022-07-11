(ns webchange.ui.components.icon.social.index
  (:require
    [webchange.ui.components.icon.social.icon-facebook :as facebook]
    [webchange.ui.components.icon.social.icon-instagram :as instagram]
    [webchange.ui.components.icon.social.icon-linkedin :as linkedin]
    [webchange.ui.components.icon.social.icon-tweeter :as tweeter]
    [webchange.ui.components.icon.social.icon-youtube :as youtube]))

(def data {"facebook"  facebook/data
           "instagram" instagram/data
           "linkedin"  linkedin/data
           "tweeter"   tweeter/data
           "youtube"   youtube/data})
