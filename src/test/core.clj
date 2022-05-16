(ns test.core)
(use 'hickory.core)
(require '[clj-http.client :as client])
(require '[hickory.select :as s])
(require '[clojure.string :as str])
(require '[clojure.core.match :refer [match]])

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn getHtml
  [url]
  (-> (client/get url) :body parse as-hickory
  ))

(def wigglePrefix
  "https://www.wiggle.co.uk/cycle/")

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
     urlModification (comp #(str/replace % wigglePrefix "") #(str/replace % "#breadcrumbs" ""))
     urlsWithoutBreadcrumbs (map urlModification urls)
     base (str/replace url wigglePrefix "")
     filterUrls_ (conj filterUrls url)
     filtered (remove (set filterUrls_) urlsWithoutBreadcrumbs)
     ]
     {:base base :urls filtered :filterUrls filterUrls_}
    ))
  )

(defn getAllLinks [arg]
  (match arg
         {:base base :urls urls :filterUrls filterUrls}
            ((let [new_val (map #(getCategoriesUrls (urlConcat startUrl %) filterUrls) urls)]
               (new_val "kek")
              ))
         {:base base :urls [] :filterUrls filterUrls}
            `kek
         :else
            :no-match))

(defn kek [maps]
  (match  maps
          {:lol []} 0
          {:lol a} a
          {:kek b} b))


(def startUrl "https://www.wiggle.co.uk/cycle/bike-parts")
(def filterUrlsBase
  ["https://www.wiggle.co.uk/"])

(def cycleUrlBase "https://www.wiggle.co.uk/cycle")

(defn urlConcat
  [prefix postfix]
  (str/join "/" [prefix postfix]))



