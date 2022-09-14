(ns webchange.ui.components.icon.layout.index
  (:require
    [webchange.ui.components.icon.layout.icon-page-image-only :as page-image-only]
    [webchange.ui.components.icon.layout.icon-page-text-at-top :as page-text-at-top]
    [webchange.ui.components.icon.layout.icon-page-text-big-at-bottom :as page-text-big-at-bottom]
    [webchange.ui.components.icon.layout.icon-page-text-only :as page-text-only]
    [webchange.ui.components.icon.layout.icon-page-text-over-image-at-bottom :as page-text-over-image-at-bottom]
    [webchange.ui.components.icon.layout.icon-page-text-over-image-at-top :as page-text-over-image-at-top]
    [webchange.ui.components.icon.layout.icon-page-text-small-at-bottom :as page-text-small-at-bottom]
    [webchange.ui.components.icon.layout.icon-spread-image-only :as spread-image-only]
    [webchange.ui.components.icon.layout.icon-spread-text-left-bottom :as spread-text-left-bottom]
    [webchange.ui.components.icon.layout.icon-spread-text-left-top :as spread-text-left-top]
    [webchange.ui.components.icon.layout.icon-spread-text-right-bottom :as spread-text-right-bottom]
    [webchange.ui.components.icon.layout.icon-spread-text-right-top :as spread-text-right-top]))

(def data {"page-image-only"                page-image-only/data
           "page-text-at-top"               page-text-at-top/data
           "page-text-big-at-bottom"        page-text-big-at-bottom/data
           "page-text-only"                 page-text-only/data
           "page-text-over-image-at-bottom" page-text-over-image-at-bottom/data
           "page-text-over-image-at-top"    page-text-over-image-at-top/data
           "page-text-small-at-bottom"      page-text-small-at-bottom/data
           "spread-image-only"              spread-image-only/data
           "spread-text-left-bottom"        spread-text-left-bottom/data
           "spread-text-left-top"           spread-text-left-top/data
           "spread-text-right-top"          spread-text-right-top/data
           "spread-text-right-bottom"       spread-text-right-bottom/data})
