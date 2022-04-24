(ns test.core)
(use 'hickory.core)
(require '[clj-http.client :as client])
(require '[hickory.select :as s])
(require '[clojure.string :as str])

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn getHtml
  [url]
  (-> (client/get url) :body parse as-hickory
  ))

(defn getCategoriesUrls
  ([url]
   (getCategoriesUrls url nil))
  ([url filterUrls]
  (let
    [html (getHtml url)
     lst (-> (s/select
               (s/class "plp-refinements__section__list__item-link")
               html))
     urls (map #((% :attrs) :href) lst)
     urlsWithoutBreadcrumbs (map #(str/replace % "#breadcrumbs" "") urls)
     filterUrls_ (conj filterUrls url)
     filtered (remove (set filterUrls_) urlsWithoutBreadcrumbs)
     ]
     {:urls filtered :filterUrls filterUrls_}
    ))
  )

(def filterUrlsBase
  ["https://www.wiggle.co.uk/"])

